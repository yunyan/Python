#!/usr/bin/python3

import sys
import re
from subprocess import call
from urllib.request import urlopen

def main(url):
    add = url[0]
    add = add.replace("#/", "")
    download_list = []
    rs = urlopen(add).read().decode('utf8')
    l = re.findall(r'\/song\?id=\d+', rs)
    for i in l:
        download_list.append("http://music.163.com/#"+i)
    for i in download_list:
        call(["you-get", i])
        


if __name__ == '__main__':
    main(sys.argv[1:])
