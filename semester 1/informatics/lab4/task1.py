def count_indents(line):
    index = 0
    while line[index] == " ":
        index += 1
    
    if line[index] == "-":
        index += 2

    return index // 2


def yaml_to_json(yaml_file):
    yaml_file = yaml_file.read().splitlines()
    json_file = "{"

    current_indent = 0
    brackets = ["}"]
    for i, line in enumerate(yaml_file):
        key, value = line.split(":", 1)
        if current_indent == 0:
            json_file += "\"" + key + "\":"
        elif key[(current_indent - 1) * 2] == "-":
            if brackets[-1] == "]":
                json_file = json_file[:-1] + "{\"" + key[current_indent * 2:] + "\":"
                brackets.append("}")
            else:
                json_file = json_file[:-1] + "},{\"" + key[current_indent * 2:] + "\":"
        elif key[current_indent * 2] != " " and key[current_indent * 2 - 1] == " ":
            json_file += "\"" + key[current_indent * 2:] + "\":"
        else:
            json_file = json_file[:-1]
            for _ in range(current_indent - count_indents(key) + 1):
                json_file += brackets.pop()
            json_file += ",\"" + key + "\":"

        value = value.strip()
        if value:
            if value.isdigit():
                json_file += value + ","
            elif value.startswith("'") or value.startswith("\""):
                json_file += "\"" + value[1:len(value) - 1] + "\","
            elif value.startswith("["):
                json_file += value + ","
            else:
                json_file += "\"" + value + "\","
        else:
            if i + 1 == len(yaml_file):
                json_file += "null,"
            elif yaml_file[i + 1].startswith("  " * current_indent + "-"):
                json_file += "[{"
                brackets.extend(["]"])
                current_indent += 1
            elif yaml_file[i + 1].startswith("  " * (current_indent + 1)):
                json_file += "{"
                brackets.append("}")
            else:
                json_file += "null,"

    json_file = json_file[:-1] + "}"

    with open("result1.json", mode="w+", encoding="utf-8") as file:
        file.write(json_file)

if __file__ == "__main__":
    yaml_to_json(open("schedule.yaml", mode="r", encoding="utf-8"))
