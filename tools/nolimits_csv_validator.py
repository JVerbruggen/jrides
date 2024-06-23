#  GPLv3 License
#
#  Copyright (c) 2024-2024 JVerbruggen
#  https://github.com/JVerbruggen/jrides
#
#  This software is protected under the GPLv3 license,
#  that can be found in the project's LICENSE file.
#
#  In short, permission is hereby granted that anyone can copy, modify and distribute this software.
#  You have to include the license and copyright notice with each and every distribution. You can use
#  this software privately or commercially. Modifications to the code have to be indicated, and
#  distributions of this code must be distributed with the same license, GPLv3. The software is provided
#  without warranty. The software author or license can not be held liable for any damages
#  inflicted by the software.

import csv
import argparse
import os
import math

parser = argparse.ArgumentParser(description='Nolimits CSV validator for jrides')
parser.add_argument('csv_file')
args = parser.parse_args()

csv_file = args.csv_file
if not os.path.exists(csv_file):
    print(f"Path '{csv_file}' does not exist")
    exit(1)

prev_x = None
prev_y = None
prev_z = None

final_d = 0

with open(csv_file, 'r') as f:
    reader = csv.reader(f, delimiter='\t')
    
    for i, row in enumerate(reader):
        if i == 0: continue
        
        x = float(row[1])
        y = float(row[2])
        z = float(row[3])
        
        if prev_x is not None:
            dx = abs(x - prev_x)
            dy = abs(y - prev_y)
            dz = abs(z - prev_z)
            
            # Distance 3D
            d = math.sqrt(dx*dx + dy*dy + dz*dz)
            
            d_display = "{:.3f}".format(d)
            
            print("P-Distance: {}".format(d_display))
            
            if i > 5:
                final_d = d
                break
        
        prev_x = x
        prev_y = y
        prev_z = z

print("P-Distance should be around 0.050")

if float(d_display) == 0.050:
    print("Result: OK")
else:
    print("Result: Not OK")
