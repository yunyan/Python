import threading
import urllib2
import ssl
import datetime
import json

tokens  =  {}

class token_thread(threading.Thread):
    _conn = None 
    _addr = None 
    _operation = None
    _app_id = None
    _app_sec = None 
    _lock = threading.Lock()

    def __init__(self, net_info):
        threading.Thread.__init__(self)
        [self._conn, self._addr] = net_info 

    
    def refresh_token(self):
        request_token_cmd = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid={APPID}&secret={APPSECRET}".format(APPID=self._app_id, APPSECRET=self._app_sec)

        try:
            response = urllib2.urlopen(request_token_cmd).read()
        except ssl.CertificateError:
            context = ssl._create_unverified_context()
            response = urllib2.urlopen(request_token_cmd, context = context).read()

        result = json.loads(response)
        access_token = result[u'access_token']
        expires_in = result[u'expires_in']
        expires_time = datetime.datetime.now() + datetime.timedelta(seconds=expires_in)
        self._lock.acquire()
        tokens[self._app_id] = [access_token, expires_time]
        self._lock.release()
        return tokens[self._app_id][0]


    def apply_token(self):
        if not tokens.has_key(self._app_id):
            self.refresh_token()

        [token, expires_time] =  tokens[self._app_id]
        now = datetime.datetime.now()
        time_rest = expires_time - now
        if abs(time_rest.total_seconds()) < 10:
            self.refresh_token()

        return tokens[self._app_id][0]

    def run(self):
        data = self._conn.recv(1024)
        [self._operation, self._app_id, self._app_sec] = data.split('|')
        try:
            if self._operation == "APPLY":
                token = self.apply_token()
            elif self._operation == "REFRESH":
                token = self.refresh_token()
            print("token is {0}".format(token))
            self._conn.send(token)
        except Exception as err:
            print(err)
        finally:
            self._conn.close()
