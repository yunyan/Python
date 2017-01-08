#!/usr/bin/python3

import sys
import re
import getopt
import subprocess 
import urllib.request
import urllib.error

def download(ls, dest, dry_run):
    cmd = ["you-get"]
    target = ""
    if dest != None:
        cmd.append("-o")
        cmd.append(dest)
    if dry_run:
        cmd.append("-i")
    cmd.append(target)    
    
    for name, id in ls.items():
        cmd[len(cmd)-1] = id
        print (cmd)
        subprocess.call(cmd)

def download_netease(url, dest, dry_run):
    url = url.replace("#/", "")
    download_list = []
    dl = {}
    rs = urllib.request.urlopen(url).read().decode('utf8')

    #<a href="/song?id=5276807">悲怆奏鸣曲</a>
    dl_list = re.findall(r'<a href\=\"(\/song\?id=\d+)\">([\w\s\(\)]+)', rs)
    for i in dl_list:
        dl[i[1]] = "http://music.163.com/#"+i[0]

    download(dl, dest, dry_run)    

def download_xiami(url, dest, dry_run):
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
    if url.find("163") > 0:
        download_netease(url, dest, dry_run)
    elif url.find("xiami") > 0:
        download_xiami(url, dest, dry_run)


if __name__ == '__main__':
    try:
        opt, args = getopt.getopt(sys.argv[1:], "s:o:i")
    except getopt.GetoptError as err:
        print (err)
        
    url = None
    dest = "."
    dry_run = False

    for o, v in opt:
        if o == "-s":
            url = v
        elif o == "-o":
            dest = v
        elif o == "-i":
            dry_run = True

    if url == None:
        print("Usage:")
        print(sys.argv[0], "-s url [-o destination]")
        sys.exit(1)

    main(url, dest, dry_run)

            
