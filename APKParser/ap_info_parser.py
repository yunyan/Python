from ap_logging import *



class ap_info_parser(object):
    apk_info = []
    def __init__(self):
        pass

    def __call__(self):
        self.parse()

    def get_apk_info(self):
        return self.apk_info
    
    def parse(self, file):
        logger.debug(file)
        




