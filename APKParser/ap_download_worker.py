from threading import Thread
from queue import Queue, Empty
from ap_logging import *
import urllib
import os

logger = logging.getLogger("APKParser")

headers = []

class ap_download_worker(Thread):
    
    ap_parser = None
    queue = None
    id = -1

    def __init__(self, queue, id):
        Thread.__init__(self)
        self.queue = queue
        self.id = id

    def set_parser(self, parser):
        self.ap_parser = parser

    def download_link(self, directory, link):
        logger.debug("worker {} start downloading {}".format(self.id, link))
        file_name = 'tmp.apk'
        #local_file = os.path.join(directory, file_name)
       # local_file, headers = urllib.request.urlretrieve(link, os.path.join(dir, file_name))
        return file_name
  

    def run(self):
        while True:
            try:
                dir, link = self.queue.get(timeout = 2)
            except Empty:
                logger.debug("No tasks for worker {}".format(self.id))
                break

            local_file = self.download_link(dir, link)
            if self.ap_parser:
                self.ap_parser(local_file)
            self.queue.task_done()


