import zipfile
import os

# unzip files
files = os.listdir()
for file in files:
    if file[file.rfind("."):] == ".zip":
        with zipfile.ZipFile(file, "r") as zip_ref:
            zip_ref.extractall()

# start parsing files
os.system("python parser.py")
