import os
import argparse
from queue import Queue
from ap_logging import *
import ap_download_worker as apd
import ap_info_parser as api



if __name__ == '__main__':
    logger = logging.getLogger("APKParser")
    optparser = argparse.ArgumentParser(description = "Download and retract apk information")
    optparser.add_argument('-d', '--dest', help = "Directory to store apk files, default to where the script runs in", default = '.')
    optparser.add_argument('-w', '--workers', help = "# of worker thread to download simultaneously", default = 4, type = int)
    optparser.add_argument('-v', '--verbose', help = "verbose mode", action = 'store_true')
    args = optparser.parse_args()
    
    download_dir = os.path.join(args.dest)
    workers = args.workers
    verbose_mode = args.verbose

    if verbose_mode:
        logger.setLevel(logging.DEBUG)
    else:
        logger.setLevel(logging.INFO)

    logger.debug(download_dir)
    logger.debug(workers)

    URLS = ["first link",
            "second link",
            "third link",
            "forth link",
            "fifth link"]

    queue = Queue()
    apkparser = api.ap_info_parser()
    

    for x in range(workers):
        worker = apd.ap_download_worker(queue, x)
        worker.set_parser(apkparser.parse)
        worker.deamon = True
        worker.start()

    for url in URLS:
        queue.put((dir, url))

    queue.join()
    apkparser.get_apk_info()