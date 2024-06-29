# NoLimits (2) CSV Validator

## Prerequisites

- Installed Python 3 on your machine
- Downloaded a copy of this repository (or just the tool)
- Generated a CSV from your NoLimits 2 coaster

## Usage
The NoLimits (2) CSV Validator tool can be found in the tools folder in the root directory of this repository. 

`python3 tools/nolimits_csv_validator_script.py <csv_file>`

- csv_file should be the path towards the CSV file that was created with NoLimits 2.

## Result
The tool will output a number with the average distance between points on your track. jrides expects this number to be 0.050. 

The tool will output NOK/OK according to how close it is to this expected number.

If NOK, adjust your NoLimits CSV export settings as required.
