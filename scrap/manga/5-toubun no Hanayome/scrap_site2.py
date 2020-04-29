# encoding: utf-8

import re
import requests
import os
import zipfile

WEBSITE = "https://go-toubun-no-hanayome.com/"
REGEX_CHAPTERS = r"<li><a href=\"(.*?)\">Go Toubun No Hanayome, Chapter ([\d]+)<\/a><\/li>"
REGEX_IMAGES = r"<img.*?src=\"(.*?)\".*?\/>"

STORAGE = "C:\\Users\\cacer\\Pictures\\Manga\\The Quintessential Quintuplets\\"

STORAGE_ZIPS = os.path.join(STORAGE, "zips")
if not os.path.exists(STORAGE_ZIPS):
    os.makedirs(STORAGE_ZIPS)

last = 99999.0
for match in re.finditer(REGEX_CHAPTERS, requests.get(WEBSITE).text, re.MULTILINE):
    chapter = match.group(2)
    chapter_url = match.group(1)

    # Avoid 'last chapter' section
    try:
        if float(chapter) > last:
            continue
        last = float(chapter)
    except:
        pass

    chapter_beautiful_name = "Ch. {}".format(chapter)

    chapter_directory_images = os.path.join(STORAGE, "images", chapter_beautiful_name)

    if not os.path.exists(chapter_directory_images):
        os.makedirs(chapter_directory_images)

    # Create a clickable windows .URL file to quickly open the web version
    with open(os.path.join(chapter_directory_images, "Online.url"), 'w+') as file:
        file.write("[InternetShortcut]\n")
        file.write("URL=" + chapter_url + "\n")

    print(chapter_beautiful_name)

    chapter_file_zip = os.path.join(STORAGE_ZIPS, chapter_beautiful_name + ".zip")
    with zipfile.ZipFile(chapter_file_zip, 'w') as zip_file:
        for match2 in re.finditer(REGEX_IMAGES, requests.get(chapter_url).text, re.MULTILINE):
            page_url = match2.group(1)
            page_name = page_url[page_url.rindex("/") + 1:]
    
            page_path = os.path.join(chapter_directory_images, page_name)
    
            print("  -> " + page_path)
    
            with open(page_path, 'wb') as file:
                file.write(requests.get(page_url).content)
            
            zip_file.write(page_path, page_name, compress_type=zipfile.ZIP_STORED)
