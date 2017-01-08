import hashlib
import sys
import os


def main(argv):
    """ main function"""
    path = argv[0]
    md5_dict = {}
    dels = []
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
                    print("adding {0}:{1} to md5_dict...".format(md5_value, file_with_path))
                else:
                    print("deleting {0} ...".format(file_with_path))
                    dels.append(file_with_path)
        for f in dels:
            os.remove(f)

if __name__ == '__main__':
    main(sys.argv[1:])
