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

parser = argparse.ArgumentParser(description='Nolimits CSV fixer for jrides. Will try to bring points as close to 0.050 as possible.')
parser.add_argument('csv_file')
args = parser.parse_args()

csv_file = args.csv_file
if not os.path.exists(csv_file):
    print(f"Path '{csv_file}' does not exist")
    exit(1)

requested_d = 0.050

def write_to_file(output_file, line):
    output_file.write(line + "\n")

counter = 1

def generate_line_from_row(row):
    x           = float(row[1])
    y           = float(row[2])
    z           = float(row[3])
    front_x     = float(row[4])
    front_y     = float(row[5])
    front_z     = float(row[6])
    left_x      = float(row[7])
    left_y      = float(row[8])
    left_z      = float(row[9])
    back_x      = float(row[10])
    back_y      = float(row[11])
    back_z      = float(row[12])
    return generate_line(x, y, z, front_x, front_y, front_z, left_x, left_y, left_z, back_x, back_y, back_z)

def generate_line(x, y, z, front_x, front_y, front_z, left_x, left_y, left_z, back_x, back_y, back_z):
    global counter
    generated_line = "{}\t{:.6f}\t{:.6f}\t{:.6f}\t{:.6f}\t{:.6f}\t{:.6f}\t{:.6f}\t{:.6f}\t{:.6f}\t{:.6f}\t{:.6f}\t{:.6f}".format(counter,
                                                                                                                                 x,
                                                                                                                                 y,
                                                                                                                                 z,
                                                                                                                                 front_x,
                                                                                                                                 front_y,
                                                                                                                                 front_z,
                                                                                                                                 left_x,
                                                                                                                                 left_y,
                                                                                                                                 left_z,
                                                                                                                                 back_x,
                                                                                                                                 back_y,
                                                                                                                                 back_z)
    counter += 1
    return generated_line

def write_interpolation_to_csv_file(output_file, from_row, to_row, requested_d):
    from_x          = float(from_row[1])
    from_y          = float(from_row[2])
    from_z          = float(from_row[3])
    from_front_x    = float(from_row[4])
    from_front_y    = float(from_row[5])
    from_front_z    = float(from_row[6])
    from_left_x     = float(from_row[7])
    from_left_y     = float(from_row[8])
    from_left_z     = float(from_row[9])
    from_back_x     = float(from_row[10])
    from_back_y     = float(from_row[11])
    from_back_z     = float(from_row[12])

    to_x            = float(to_row[1])
    to_y            = float(to_row[2])
    to_z            = float(to_row[3])
    to_front_x      = float(to_row[4])
    to_front_y      = float(to_row[5])
    to_front_z      = float(to_row[6])
    to_left_x       = float(to_row[7])
    to_left_y       = float(to_row[8])
    to_left_z       = float(to_row[9])
    to_back_x       = float(to_row[10])
    to_back_y       = float(to_row[11])
    to_back_z       = float(to_row[12])

    dx          = to_x - from_x
    dy          = to_y - from_y
    dz          = to_z - from_z
    dfront_x    = to_front_x - from_front_x
    dfront_y    = to_front_y - from_front_y
    dfront_z    = to_front_z - from_front_z
    dleft_x     = to_left_x - from_left_x
    dleft_y     = to_left_y - from_left_y
    dleft_z     = to_left_z - from_left_z
    dback_x     = to_back_x - from_back_x
    dback_y     = to_back_y - from_back_y
    dback_z     = to_back_z - from_back_z

    d = math.sqrt(dx*dx + dy*dy + dz*dz)

    if d < requested_d: raise Exception("Cannot handle interpolate on a CSV with distances that are too small")

    if d == requested_d:
        write_to_file(output_file, to_row)
        return

    # Interpolate
    extra_detail = int(d / requested_d)

    dx_interpolate_increment        = dx / extra_detail
    dy_interpolate_increment        = dy / extra_detail
    dz_interpolate_increment        = dz / extra_detail
    dfront_x_interpolate_increment  = dfront_x / extra_detail
    dfront_y_interpolate_increment  = dfront_y / extra_detail
    dfront_z_interpolate_increment  = dfront_z / extra_detail
    dleft_x_interpolate_increment   = dleft_x  / extra_detail
    dleft_y_interpolate_increment   = dleft_y  / extra_detail
    dleft_z_interpolate_increment   = dleft_z  / extra_detail
    dback_x_interpolate_increment   = dback_x  / extra_detail
    dback_y_interpolate_increment   = dback_y  / extra_detail
    dback_z_interpolate_increment   = dback_z  / extra_detail

    for i in range(extra_detail):
        new_x           = from_x        + (dx_interpolate_increment * (i+1))
        new_y           = from_y        + (dy_interpolate_increment * (i+1))
        new_z           = from_z        + (dz_interpolate_increment * (i+1))
        new_front_x     = from_front_x  + (dfront_x_interpolate_increment * (i+1))
        new_front_y     = from_front_y  + (dfront_y_interpolate_increment * (i+1))
        new_front_z     = from_front_z  + (dfront_z_interpolate_increment * (i+1))
        new_left_x      = from_left_x   + (dleft_x_interpolate_increment * (i+1))
        new_left_y      = from_left_y   + (dleft_y_interpolate_increment * (i+1))
        new_left_z      = from_left_z   + (dleft_z_interpolate_increment * (i+1))
        new_back_x      = from_back_x   + (dback_x_interpolate_increment * (i+1))
        new_back_y      = from_back_y   + (dback_y_interpolate_increment * (i+1))
        new_back_z      = from_back_z   + (dback_z_interpolate_increment * (i+1))

        generated_line = generate_line(new_x,
                                        new_y,
                                        new_z,
                                        new_front_x,
                                        new_front_y,
                                        new_front_z,
                                        new_left_x,
                                        new_left_y,
                                        new_left_z,
                                        new_back_x,
                                        new_back_y,
                                        new_back_z)

        write_to_file(output_file, generated_line)


with open(f"{csv_file}_fixed.csv", 'w') as output_file:
    write_to_file(output_file, "\"No.\"\t\"PosX\"\t\"PosY\"\t\"PosZ\"\t\"FrontX\"\t\"FrontY\"\t\"FrontZ\"\t\"LeftX\"\t\"LeftY\"\t\"LeftZ\"\t\"UpX\"\t\"UpY\"\t\"UpZ\"")

    with open(csv_file, 'r') as f:
        reader = csv.reader(f, delimiter='\t')

        prev_row = None
        for i, row in enumerate(reader):
            if i == 0: continue # Skip header
            if i == 1:
                write_to_file(output_file, generate_line_from_row(row)) # When interpolating, always write the first item
                prev_row = row
                continue

            if i%10 == 0:
                print(f"Interpolation progress: {i}")
            write_interpolation_to_csv_file(output_file, prev_row, row, requested_d)

            prev_row = row

print(f"Done, written to {output_file}")
# print(f"P-Distance should be around {requested_d}")

# if float(d_display) == requested_d:
#     print("Result: OK")
# else:
#     print("Result: Not OK")
