#!/usr/bin/python3
"""
"""

import re
import sys
import subprocess
import urllib.request
import urllib.error
import shutil

class Netease(object):
    """
    class to manage netease download
    """

    src_ = ""
    dest_ = "."
    dry_run_ = False
    dl_ = {}

    def __init__(self, url, dest, dry_run):
        self.src_ = url
        self.dest_ = dest
        self.dry_run_ = dry_run

    def prepare(self):
        """
        prepare download link
        """
        # check if you-get exist
        if shutil.which('you-get') is None:
            print("Please install '''you-get''' first: pip3 install you-get")
            sys.exit(1)

        url = self.src_.replace("#/", "")
        rs = urllib.request.urlopen(url).read().decode('utf8')

        if self.src_.find("song") > 0:
            self.dl_["song"] = self.src_
        else:
            #<a href="/song?id=5276807">song name</a>
            dl_list = re.findall(r'<a href\=\"(\/song\?id=\d+)\">([\w\s\(\)]+)', rs)
            for i in dl_list:
                self.dl_[i[1]] = "http://music.163.com/#"+i[0]


    def download(self):
        """
        download
        """
        cmd = ["you-get"]
        cmd.append("-o")
        cmd.append(self.dest_)

        if self.dry_run_:
            cmd.append("-i")

        target = ""
        cmd.append(target)

        for name, song_id in self.dl_.items():
            cmd[len(cmd)-1] = song_id
            subprocess.call(cmd)

