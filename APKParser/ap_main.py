import os
from queue import Queue
from threading import Thread

from apk_parser_logging import *


class DownloadWorkder(Thread):
    def __init__(self, queue):
        Thread.__init__(self)
        self.queue = queue

    def run(self):
        while True:
            dir, link = self.queue.get()
            download_link(dir, link)
            self.queue.task_done()

class APKParser():
    pass

if __name__ == '__main__':
    logger.info("Hello")
    logger.warning("Hello")