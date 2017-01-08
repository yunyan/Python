import sys
import re

try:
    from urllib.request import urlopen
except:
    from urllib2 import urlopen
#import urllib2

def main(url):
    add = url[0]
    add = add.replace("#/", "")
    download_list = []
    rs = urlopen(add).read().decode('utf8')
    l = re.findall(r'\/song\?id=\d+', rs)
    for i in l:
        download_list.append("http://music.163.com/#"+i)
    for i in download_list:
        print(i)


if __name__ == '__main__':
    main(sys.argv[1:])
