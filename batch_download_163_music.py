#!/usr/bin/python3

import sys
import re
import getopt
from subprocess import call
from urllib.request import urlopen

def main(url, dest):
    url = url.replace("#/", "")
    download_list = []
    rs = urlopen(url).read().decode('utf8')
    l = re.findall(r'\/song\?id=\d+', rs)
    for i in l:
        download_list.append("http://music.163.com/#"+i)
    for i in download_list:
        call(["you-get", "-o", dest, i])
        


if __name__ == '__main__':
    try:
        opt, args = getopt.getopt(sys.argv[1:], "s:o:")
    except getopt.GetoptError as err:
        print (err)
        
    url = None
    dest = "."
    for o, v in opt:
        if o == "-s":
            url = v
        elif o == "-o":
            dest = v

    if url == None:
        print("Usage:")
        print(sys.argv[0], "-s url [-o destination]")
        sys.exit(1)


    main(url, dest)

            
