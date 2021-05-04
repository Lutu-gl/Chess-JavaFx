import os
files = os.listdir()

for file in files:
    extension = file[file.rfind("."):]
    if extension == ".zip" or (extension == ".pgn" and file[file.rfind(".")-3:] != "NEW.pgn"):
        os.remove(file)
