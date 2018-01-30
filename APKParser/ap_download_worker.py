from threading import Thread
from queue import Queue, Empty
from ap_logging import *
import urllib
import os

logger = logging.getLogger(__name__)

headers = []

class ap_download_worker(Thread):
    
    ap_parser = None
    queue = None
    id = -1

    def __init__(self, queue, id):
        Thread.__init__(self)
        self.queue = queue
        self.id = id

    def set_parser(parser):
        self.ap_parser = parser

    def download_link(self, dir, link):
        logger.info("downloader {} start downloading {}".format(self.id, link))
       # file_name = 'tmp.apk'
       # local_file, headers = urllib.request.urlretrieve(link, os.path.join(dir, file_name))
       # return local_file
  

    def run(self):
        while True:
            try:
                dir, link = self.queue.get(timeout = 2)
            except Empty:
                logger.info("downloader {}'s queue is empty".format(self.id))
                break

            local_file = self.download_link(dir, link)
            if self.ap_parser:
                self.ap_parser(local_file)
            self.queue.task_done()


