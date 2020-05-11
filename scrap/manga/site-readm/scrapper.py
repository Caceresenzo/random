# encoding: utf-8

import re
import requests
import os
import zipfile
import sys
import time

WEBSITE = "https://readm.org/"
MANGA_FORMAT = WEBSITE + "manga/{}"

REGEX_CHAPTERS = r"<td id=\"table-episodes-title\" class=\"table-episodes-title\"><h6 class=\"truncate\"><a href=\"(.*?)\" data-navigo>Chapter ([\d.]+)[\s]*<\/a><\/h6><\/td>"
REGEX_IMAGES = r"<img src=\"(.*?)\" class=\"img-responsive scroll-down\" style=\".*?\">"

target_manga_id = None
target_manga_name = None

if len(sys.argv) <= 2:
    target_manga_id = input("Target Manga ID> ")
    target_manga_name = input("Target Manga Name> ")
else:
    target_manga_id = sys.argv[1]
    target_manga_name = sys.argv[2]

STORAGE = "C:\\Users\\cacer\\Pictures\\Manga\\READM-OUT\\"
STORAGE_TARGET = os.path.join(STORAGE, target_manga_name)
STORAGE_IMAGES = os.path.join(STORAGE_TARGET, "images")
STORAGE_ZIPS = os.path.join(STORAGE_TARGET, "zips")

for directory in [ STORAGE, STORAGE_TARGET, STORAGE_IMAGES, STORAGE_ZIPS ]:
    if not os.path.exists(directory):
        os.makedirs(directory)

for match in re.finditer(REGEX_CHAPTERS, requests.get(MANGA_FORMAT.format(target_manga_id.lower())).text, re.MULTILINE | re.DOTALL):
    chapter = match.group(2)
    chapter_url = match.group(1)
    
    if chapter_url.startswith("/"):
        chapter_url = WEBSITE + chapter_url

    chapter_beautiful_name = ("Ch. {}").format(chapter)

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
        while retry < 3 and retry is not -1:
            for match2 in re.finditer(REGEX_IMAGES, requests.get(chapter_url).text, re.MULTILINE):
                page_url = match2.group(1)
                page_name = page_url[page_url.rindex("/") + 1:]
                
                if page_url.startswith("/"):
                    page_url = WEBSITE + page_url
    
                page_path = os.path.join(chapter_directory_images, page_name)
    
                print("  -> " + page_path, end="")
                sys.stdout.flush()
    
                start = time.time()
                retry_page = 0
                while retry_page < 3 and retry_page is not -1:
                    with open(page_path, 'wb') as file:
                        file.write(requests.get(page_url, headers = {"Referer": chapter_url}).content)
                        retry_page = -1
                        
                    if retry_page is not -1:
                        time.sleep(2)
                
                end = time.time()
                
                print(" ({:.2f} MB, took {} ms)".format(round(os.stat(page_path).st_size / (1024 * 1024), 2), int((end - start) * 1000)))
    
                zip_file.write(page_path, page_name, compress_type = zipfile.ZIP_STORED)
                
                retry = -1
            
            if retry is not -1:
                time.sleep(2)
