import os


for filename in os.listdir():
    if (filename.startswith("parse_") or filename.startswith("string_") or 
        filename.startswith("read_") or filename.startswith("print_") or
        filename == "report.xml"):
        os.remove(filename)
