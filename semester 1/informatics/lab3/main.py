import re
import argparse


class App:
    @staticmethod
    def complete_task1(string: str) -> int:
        return len(re.findall(r";<P", string))

    @staticmethod
    def complete_task2(string: str) -> str:
        string = string.replace("-", "ς")

        for repeat in re.findall(r"\b(\w+)\s+\1\b", string):
            string = string.split(repeat, 2)
            string[1] = repeat
            string = "".join(string)
        
        return string.replace("ς", "-")

    @staticmethod
    def complete_task3(string: str) -> str:
        return " ".join(re.findall(r"\b(К[^КРА\s]{1}Р[^КРА\s]{1}А)\b", string, flags=re.IGNORECASE))
            

if __name__ == "__main__":
    # Запуск: python main.py {номер задания} "{входные данные}"

    parser = argparse.ArgumentParser()
    parser.add_argument("task", choices=[1, 2, 3], type=int)
    parser.add_argument("text")
    args = parser.parse_args()

    complete_task = {
        1: App.complete_task1,
        2: App.complete_task2,
        3: App.complete_task3
    }

    print(f"Task {args.task}")
    print(f"Input: {args.text}")
    print(f"Output: {complete_task[args.task](args.text)}")
