#!/usr/bin/python

"""

Calculates the md5 value for the files in directory and delete the one which has the same md5 value.

"""

import hashlib
import sys
import os


def main(argv):
    """ main function"""
    path = argv[0]
    md5_dict = {}
    for dirpath, dirname, filelist in os.walk(path):
      #  print(dirpath, dirname, filelist)
        for infile in filelist:
            file_with_path = os.path.join(dirpath, infile)
            with open(file_with_path, 'rb') as openfile:
                m = hashlib.md5()
                m.update(openfile.read())
                md5_value = m.hexdigest()
                if md5_value not in md5_dict:
                    md5_dict[md5_value] = file_with_path
                    print "adding {0}:{1} ...".format(md5_value, file_with_path)
                else:
                    print "deleting {0} ...".format(file_with_path)
                    #os.remove(file_with_path)

if __name__ == '__main__':
    if len(sys.argv) > 1:
        main(sys.argv[1:])
    else:
        print "{} <Directory>".format(sys.argv[0])
