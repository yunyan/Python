import urllib.request
import urllib.error
import json
import re
import os

class Xiami(object):
    """
    Xiami downloader
    """

    src_ = ""
    dest_ = "."
    dry_run_ = False
    dl_ = {}

    def __init__(self, url, dest, dry_run):
        self.src_ = url
        self.dest_ = dest
        self.dry_run_ = dry_run

    def get_xiami_response(self, link = None):
        """
        """

        user_agent = 'Mozilla/5.0 (Windows NT 6.1; Win64; x64)'
        headers = {'User-Agent' : user_agent}
        data = None

        req = urllib.request.Request(link, data, headers)
        try:
            res = urllib.request.urlopen(req).read().decode('utf8')
        except urllib.error.HTTPError as err:
            print(err.code, ": ", err.reason)
            print(err.headers)
            sys.exit(1)
        
        return res


    def generate_download_link_from_album_real_link(self, real_link):
        dl_list = {}
        quality = {"LOSSLESS": 3, "HIGH": 2, "LOW" : 1}

        res = self.get_xiami_response(real_link)
        # album real link returns json string
        js = json.loads(res)

        if js['data']['trackList'] is not None:
            # has tracks
            for track in js['data']['trackList']:
                for audio in track['allAudios']:
                    if track['name'] not in dl_list or quality[dl_list[track['name']]['audioQualityEnum']] < quality[audio['audioQualityEnum']]:
                        dl_list.update({track['name'] : audio})
        else:
            # no track found, print message
            print(js['message'])
        
        return dl_list


    def generate_download_link_from_album(self, link = None):
        """
        generate download link
        """

        if link is not None:
            album_id = re.findall(r'album\/([\d\w]+).*', link)
        else:
            album_id = re.findall(r'album\/([\d\w]+).*', self.src_)

        real_link = "http://www.xiami.com/song/playlist/id/"+ album_id[0] + "/type/1/cat/json"

        return self.generate_download_link_from_album_real_link(real_link)

       
    def generate_download_link_from_song(self, link_to_song=None):

        song_download_link = {}

        if link_to_song is None:
            res = self.get_xiami_response(self.src_)
        else:
            res = self.get_xiami_response(link_to_song)

        try:
            album_id = re.findall(r'href\=\"(\/album\/[\w\d]+)\"', res)[0]
            song_name = re.findall(r'\<h1\>([\w\s]+)\<*.', res)[0]
            print("Genrate download link for {0} ...".format(song_name))
            album_links = self.generate_download_link_from_album(album_id)
            song_download_link[song_name] = album_links[song_name]
        except re.error as err:
            print("Can not find songs!")
            print(err.code, ": ", err.reason)
            print(err.headers)
         
        return song_download_link 
        

    def generate_download_link_from_list(self, link_to_list=None):
        dl_list = {}
        song_link = ""
        if link_to_list is None:
            res = self.get_xiami_response(self.src_)
        else:
            res = self.get_xiami_response(link_to_list)

        #<a href="/song/bj2rd8a32" title="">她来听我的演唱会</a>
        songs = re.findall(r'<a href\=\"(\/song\/[\d\w]+)\".*>([\w\d]+)<', res)
        for song in songs:
            song_link = "http://www.xiami.com" + song[0]
            dl_list.update(self.generate_download_link_from_song(song_link))

        return dl_list


    def prepare(self):
        """
        prapare downloading
        """
        if self.src_.find('album') > 0:
            # Album, generate download link directly
            self.dl_ = self.generate_download_link_from_album()
        elif self.src_.find('song') > 0:
            self.dl_ = self.generate_download_link_from_song()
            # artist, song, collect, generate download link through album
        else:
            self.dl_ = self.generate_download_link_from_list()


    def download(self):
        """
        download
        """
 #       print(self.dl_)
        if self.dry_run_:
            print (self.dl_)
            return 

        user_agent = 'Mozilla/5.0 (Windows NT 6.1; Win64; x64)'
        headers = {'User-Agent' : user_agent}
        data = None

        for name, detail in self.dl_.items():
 #           print(detail)
            received = 0
            total_size = detail['fileSize']
            file_type = detail['format']
            file_name = name + '.' + file_type 
            tmp_name = file_name + ".download"
            file_name = os.path.join(self.dest_, file_name)
            req = urllib.request.Request(detail['filePath'], data, headers)
            try:
                res = urllib.request.urlopen(req)
            except urllib.error as err:
                print(err)
            
            if os.path.isfile(file_name):
                print("{0} exists, skiping..".format(file_name))
                continue
            
            with open(tmp_name, "wb+") as fp:
                print("")
                while True:
                    buffer = res.read(1024*256)
                    if not buffer:
                        break
                    fp.write(buffer) 
                    received += len(buffer)
                    print("Downloading.... {0} received {1:3.1f}%...".format(file_name, (received/total_size)*100), end="\r")

            if received == total_size:
                os.rename(tmp_name, file_name)
