import ap_logging

logger = ap_logging.logging.getLogger(__name__)
class ap_info_parser(object):
    apk_info = []
    def __init__(self):
        pass

    def get_apk_info(self):
        return self.apk_info
    
    def parse(self, file):
        logger.info(file)
        




