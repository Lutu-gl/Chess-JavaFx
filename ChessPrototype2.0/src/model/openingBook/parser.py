import os
files = os.listdir()

# start parsing files
for file in files:
    extension = file[file.rfind("."):]
    filename = file[:file.rfind(".")]
    if extension == ".pgn":
        newFile = open(filename+"NEW"+extension, "w")
        book = open(file, "r")
        for line in book.readlines():
            if line[0] != "[":
                newFile.write(line)
        newFile.close()
        book.close()

# start removing files
os.system("python remover.py")
