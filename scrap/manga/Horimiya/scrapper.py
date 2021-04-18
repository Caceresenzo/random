# encoding: utf-8

import regex as re  # because python is garbage
import requests
import os
import zipfile
import sys
import time

WEBSITE = "https://read-horimiya.com"

REGEX_CHAPTERS = r"<li id=\"su-post-\d+\" class=\"su-post\"><a href=\"(.*?)\">horimiya,(?: Vol\.\d+)? (.*?)<\/a><\/li>"
REGEX_IMAGES = r"<noscript><img (?:class=\"aligncenter\" )?src=\"(.*?)\" alt="

STORAGE = "C:\\Users\\cacer\\Pictures\\Manga\\horimiya\\"
STORAGE_IMAGES = os.path.join(STORAGE, "images")
STORAGE_ZIPS = os.path.join(STORAGE, "zips")

for directory in [ STORAGE, STORAGE_IMAGES, STORAGE_ZIPS ]:
    if not os.path.exists(directory):
        os.makedirs(directory)

matches = [ match for match in re.finditer(REGEX_CHAPTERS, requests.get(WEBSITE).text, re.MULTILINE | re.DOTALL) ]
matches.reverse()

while "vol-1-chapter-1-page-1" not in matches[0].group(1): # skip 'LATEST CHAPTERS' section
    matches = matches[1:]

for match in matches:
    chapter = match.group(2)
    chapter_url = match.group(1)
    
    if chapter_url.startswith("/"):
        chapter_url = WEBSITE + chapter_url

    chapter_beautiful_name = chapter.replace(":", " - ")\
                                    .replace("&#8217;", "'")\
                                    .replace("&#8230;", "…")\
                                    .replace("  ", " ")\
                                    .replace("ã©", "é")\
                                    .replace("ã", "à")\
                                    #.replace("Î±", "") # not sure about what it is

    chapter_directory_images = os.path.join(STORAGE_IMAGES, chapter_beautiful_name)

    if not os.path.exists(chapter_directory_images):
        os.makedirs(chapter_directory_images)

    # Create a clickable windows .URL file to quickly open the web version
    with open(os.path.join(chapter_directory_images, "Online.url"), 'w+') as file:
        file.write("[InternetShortcut]\n")
        file.write("URL=" + chapter_url + "\n")

    print(chapter_beautiful_name)

    chapter_file_zip = os.path.join(STORAGE_ZIPS, chapter_beautiful_name + ".zip")
    
    with zipfile.ZipFile(chapter_file_zip, 'w') as zip_file:
        retry = 0
        while retry < 3 and retry != -1:
            page_content = requests.get(chapter_url).text
            smaller_page_content = re.sub("[\w ]+, ", "", page_content)
            
            for page, match in enumerate(re.finditer(REGEX_IMAGES, smaller_page_content, re.MULTILINE), 1):
                image_url = match.group(1)
                page_name = str(page) + "." + image_url[image_url.rfind('.') + 1:]
    
                page_path = os.path.join(chapter_directory_images, page_name)
    
                print("  -> " + page_path, end="")
                sys.stdout.flush()
    
                start = time.time()
                retry_page = 0
                while retry_page < 3 and retry_page != -1:
                    with open(page_path, 'wb') as file:
                        file.write(requests.get(image_url, headers={"Referer": chapter_url}).content)
                        retry_page = -1
                        
                    if retry_page != -1:
                        time.sleep(2)
                
                end = time.time()
                
                print(" ({:.2f} MB, took {} ms)".format(round(os.stat(page_path).st_size / (1024 * 1024), 2), int((end - start) * 1000)))
    
                zip_file.write(page_path, page_name, compress_type=zipfile.ZIP_STORED)
                
                retry = -1
            
            if retry != -1:
                time.sleep(2)
