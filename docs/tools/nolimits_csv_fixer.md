# NoLimits (2) CSV Fixer

## Prerequisites

- Installed Python 3 on your machine
- Downloaded a copy of this repository (or just the tool)
- Generated a CSV from your NoLimits 2 coaster
- Ran the jrides NoLimits (2) CSV Validator tool (Finding out the P-number of your CSV file)

## Usage
The NoLimits (2) CSV Validator tool can be found in the tools folder in the root directory of this repository. 

`python3 tools/nolimits_csv_fixer.py <csv_file>`

- csv_file should be the path towards the CSV file that was created with NoLimits 2.

## Result
The tool will try to bring the P-number from your CSV file as close to 0.050 as it can. (With interpolation)

This only works for CSV files with P-numbers HIGHER than 0.050.

Always try to use CSV files with a P-number equal to 0.050.

The output file will be in the same folder as the input file, but will be named `original_file.csv_fixed.csv`.