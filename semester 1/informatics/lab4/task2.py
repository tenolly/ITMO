import yaml
import json


def yaml_to_json(yaml_file):
    yaml_file = yaml.safe_load(yaml_file)
    with open("result2.json", "w+", encoding="utf-8") as json_file:
        json_file.write(json.dumps(yaml_file, ensure_ascii=False))


if __file__ == "__main__":
    yaml_to_json(open("schedule.yaml", encoding="utf-8"))
