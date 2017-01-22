#!/usr/bin/python3

"""
batch downloads
"""

import sys
import re
import getopt
import subprocess
import urllib.request
import urllib.error
import shutil
import json

def download(ls, dest, dry_run):
    """
    download
    """
#    cmd = ["you-get"]
    cmd = ["python", "F:\github\you-get\you-get"]
    target = ""
    if dest != None:
        cmd.append("-o")
        cmd.append(dest)
    if dry_run:
        cmd.append("-i")
    cmd.append(target)

    for name, song_id in ls.items():
        cmd[len(cmd)-1] = song_id
        print(cmd)
        subprocess.call(cmd)

def download_netease(url, dest, dry_run):
    """
    download_netease
    """
    url = url.replace("#/", "")
    dl = {}
    rs = urllib.request.urlopen(url).read().decode('utf8')

    #<a href="/song?id=5276807">悲怆奏鸣曲</a>
    dl_list = re.findall(r'<a href\=\"(\/song\?id=\d+)\">([\w\s\(\)]+)', rs)
    for i in dl_list:
        dl[i[1]] = "http://music.163.com/#"+i[0]

    download(dl, dest, dry_run)

def download_from_xiami_by_album(url, dest, dry_run):
    """
    http://www.xiami.com/song/playlist/id/1021909911/type/1/cat/json
    """
    album_index = url.rfind('album/')
    user_agent = 'Mozilla/5.0 (Windows NT 6.1; Win64; x64)'
    headers = {'User-Agent' : user_agent}
    album_link = "http://www.xiami.com/song/playlist/id/"+url[album_index+6:]+"/type/1/cat/json"
    data = None
    dl_list = {}
    req = urllib.request.Request(album_link, data, headers)
    try:
        rs = urllib.request.urlopen(req).read().decode('utf8')
    except urllib.error.HTTPError as err:
        print(err.code, ": ", err.reason)
        print(err.headers)
        sys.exit(1)
    js = json.loads(rs)
    for track in js['data']['trackList']:
        print(track['name'])
        for audio in track['allAudios']:
#            print(audio)
#            if audio['quality'] == 'h':
#                print(audio['filePath'])
            print(audio['quality'], ':', audio['rate'], ":" , audio['filePath'])
   #         try:
   #             dl_list[track['name']] = audios['filePath']
            print("")



def download_xiami(url, dest, dry_run):
    """
    download_xiami
    """
    if url.find('album') > 0:
        download_from_xiami_by_album(url, dest, dry_run)
    else:
        # download_link should be 'http://www.xiami.com/song/playlist/id/'+{song id}+'/object_id/0/cat/json'
        user_agent = 'Mozilla/5.0 (Windows NT 6.1; Win64; x64)'
        headers = {'User-Agent' : user_agent}
        data = None
        dl = {}
        req = urllib.request.Request(url, data, headers)
        try:
            rs = urllib.request.urlopen(req).read().decode('utf8')
        except urllib.error.HTTPError as err:
            print(err.code, ": ", err.reason)
            print(err.headers)
            sys.exit(1)

        # <a href="/song/bj0xd47b7" title="我真的受伤了">
        dl_list = re.findall(r'<a href\=\"(\/song\/[\d\w]+?)\".+>([\w\s\(\)]+?)<\/a>', rs)

        for l in dl_list:
            dl[l[1]] = "www.xiami.com"+l[0]

        download(dl, dest, dry_run)


def main(url, dest, dry_run):
    """
    main function
    """
    if url.find("163") > 0:
        download_netease(url, dest, dry_run)
    elif url.find("xiami") > 0:
        download_xiami(url, dest, dry_run)


if __name__ == '__main__':

    # check if "you-get" exist
    if shutil.which('you-get') is None:
        print("Please install '''you-get''' first: pip3 install you-get")
        sys.exit(1)
    if sys.version_info[0] != 3:
        print("Python3 is needed!")
        sys.exit(1)

    try:
        opt, args = getopt.getopt(sys.argv[1:], "s:o:i")
    except getopt.GetoptError as err:
        print(err)

    page_to_download = None
    dest = "."
    dry_run = False

    for o, v in opt:
        if o == "-s":
            page_to_download = v
        elif o == "-o":
            dest = v
        elif o == "-i":
            dry_run = True

    if page_to_download is None:
        print("Usage:")
        print(sys.argv[0], "-s url [-o destination]")
        sys.exit(1)

    main(page_to_download, dest, dry_run)

