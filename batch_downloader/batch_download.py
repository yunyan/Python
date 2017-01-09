#!/usr/bin/python3

"""
batch downloads
"""

import sys
import getopt
import json
from netease import Netease
from xiami import Xiami


def main(url, dest, dry_run):
    """
    main function
    """
    if url.find("163") > 0:
        downloader = Netease(url, dest, dry_run)
        downloader.prepare()
        downloader.download()

    elif url.find("xiami") > 0:
        downloader = Xiami(url, dest, dry_run)
        downloader.prepare()
        downloader.download()


if __name__ == '__main__':

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

