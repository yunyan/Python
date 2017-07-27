import json
import sys
from pyecharts import Line

json_file = '/tmp/export.json'

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

def draw_line(x_axis, points):
    line = Line("Deal Curve")
    line.add("Deal", x_axis, points, is_stack=True, is_label_show=True)
    line.show_config()
    line.render()


if __name__ == '__main__':
    jsfile = sys.argv[1]
    x_axis, points = load_js(jsfile)
    draw_line(x_axis, points)
