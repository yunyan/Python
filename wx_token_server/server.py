import socket
import select
import sys
import token_thread
import os
import signal

token_dict = {}
close_socks_flag = False

def sigint_handler(signum, frame):
    print ("you press ctrl-c")
    close_socks_flag =  True

def main():
    HOST = socket.gethostname()
    PORT = 50007
    potential_readers = []
    potential_writers = []
    potential_errors = []
    threads = []
    server_sock = None

#    signal.signal(signal.SIGINT, sigint_handler)

    for res in socket.getaddrinfo(HOST, PORT, socket.AF_UNSPEC, socket.SOCK_STREAM, 
            0, socket.AI_PASSIVE):
        af, socktype, proto, cannoname, sa = res
        try:
            server_sock =  socket.socket(af, socktype, proto)
            server_sock.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
        except socket.error as msg:
            server_sock =  None
            continue
        try:
            server_sock.bind(sa)
            server_sock.listen(5)
            potential_readers.append(server_sock)
        except socket.error as msg:
            server_sock.close()
            server_sock =  None
            continue
        break
    if server_sock is None:
        print( "Could not open socket")
        sys.exit(1)

    while True:
        print ("waiting for connection")
        ready_to_read, ready_to_write, in_error = \
                select.select(
                        potential_readers,
                        potential_writers,
                        potential_errors,
                        5)

        for s in ready_to_read:
            conn, addr = s.accept()
            t = token_thread.token_thread([conn, addr])
            t.start()
            threads.append(t)

        for t in threads:
            t.join()



if __name__ == '__main__':
    main()
