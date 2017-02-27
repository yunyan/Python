import socket
import sys
import getopt

class tokenHelper(object):
    _server_addr = None
    _server_port = None
    _app_id = None
    _app_sec = None
    _token_server_sock = None

    def __init__(self, server_param, app_param):
        [self._server_addr, self._server_port] = server_param
        [self._app_id, self._app_sec] = app_param 


    def _connect_token_server(self):
        for res in socket.getaddrinfo(self._server_addr, self._server_port, socket.AF_UNSPEC, socket.SOCK_STREAM):
            af, socktype, proto, canonname, sa = res
            try:
                s = socket.socket(af, socktype, proto)
            except socket.error as msg:
                print msg
                s = None
                continue
            try:
                s.connect(sa)
            except socket.error as msg:
                print msg
                s.close()
                s = None
                continue
            break
        self._token_server_sock = s


    def get_token(self, get_type):
        if self._token_server_sock is None:
            self._connect_token_server()

        cmd = "{0}|{1}|{2}".format(get_type, self._app_id, self._app_sec)
        if self._token_server_sock:
           self._token_server_sock.sendall(cmd)
           data = self._token_server_sock.recv(512)
        else:
            print "_sock is None!"
        self._disconnect_token_server()

        return data 
    
    def _disconnect_token_server(self):
        if self._token_server_sock:
            self._token_server_sock.close()
            self._token_server_sock = None
            


def main():
    server_addr = socket.gethostname()
    server_port = '50007'
    app_id = "wx1bac8d84dd5ee0bd"
    app_sec = "bcf4bf4d888dbeca4342da50836cc461"

    try:
        opt, args = getopt.getopt(sys.argv[1:], "s:p:")
    except getopt.GetoptError as err:
        print err

    for o, v in opt:
        if o == "-s":
            server_addr = v
        if o == "-p":
            server_port = v
    
    server_info = [server_addr, server_port]
    app_info = [app_id, app_sec]
    th   =  tokenHelper(server_info, app_info)
    tk = th.get_token("APPLY")
    print tk

if __name__ == '__main__':
    main()
