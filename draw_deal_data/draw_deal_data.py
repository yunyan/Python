import json
import sys
import glob
import os
from pyecharts import Line

def load_js(jsfile):
    x_axis = []
    points = []
    with open(jsfile, 'r') as fp:
        while True:
            l = fp.readline()
            if not l:
                break
            deal_dict = json.loads(l)
            x_axis.append(deal_dict['dealDate'][0])
            points.append(deal_dict['unitPrice'][0])
    return x_axis, points

if __name__ == '__main__':
    dir_path = sys.argv[1]
    curve_name = ''
    line = Line("Deal Curve")
    cwd = os.getcwd()
    os.chdir(dir_path)
    for js in glob.glob("*.json"):
        x_axis, points = load_js(js)
        curve_name = js[:js.find(".json")]
        line.add(curve_name, x_axis, points, is_stack=True, is_label_show=True)
    os.chdir(cwd)
    line.render()