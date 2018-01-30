import os
import argparse
from queue import Queue
from ap_logging import *
import ap_download_worker as apd



if __name__ == '__main__':
    logger = logging.getLogger(__name__)
    parser = argparse.ArgumentParser(description = "Download and retract apk information")
    parser.add_argument('-d', '--dest', help = "Directory to store apk files, default to where the script runs in", default = '.')
    parser.add_argument('-w', '--workers', help = "# of worker thread to download simultaneously", default = 4, type = int)
    args = parser.parse_args()

    download_dir = os.path.join(args.dest)
    workers = args.workers

    logger.debug(download_dir)
    logger.debug(workers)

    URLS = ["first link",
            "second link",
            "third link",
            "forth link",
            "fifth link"]

    queue = Queue()

    for x in range(workers):
        worker = apd.ap_download_worker(queue, x)
        worker.deamon = True
        worker.start()

    for url in URLS:
        queue.put((dir, url))

    queue.join()