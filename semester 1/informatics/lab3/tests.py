from main import App


def test_task1_correct_answer():
    tests = {
        "": 0,
        ";<P I don't wanna studying ;<P": 2,
        "Some ;<P very;<P interesting ;<Ptext;<P": 4,
        "For sale:<P baby shoes;P never worn;<": 0,
        " ; <P ;< P I don't like this emoji actually ; < P P<; ": 0
    }

    for string, answer in tests.items():
        assert App.complete_task1(string) == answer


def test_task2_correct_answer():
    tests = {
        "": "",
        "Пиу-пиу пиу-пиу": "Пиу-пиу пиу-пиу",
        "Повтор Повтор Повтор": "Повтор Повтор",
        "Если для вашей задачи нужны регулярные выражения, то либо готовое решение есть в интернете, либо вам не нужны регулярные выражения": "Если для вашей задачи нужны регулярные выражения, то либо готовое решение есть в интернете, либо вам не нужны регулярные выражения",
        "Довольно распространённая ошибка ошибка – это лишний повтор повтор слова слова. Смешно, не не правда ли? Не нужно портить хор хоровод.": "Довольно распространённая ошибка – это лишний повтор слова. Смешно, не правда ли? Не нужно портить хор хоровод.",
        "А что если. если мы сделаем так . так - внезапная точка точка.": "А что если. если мы сделаем так . так - внезапная точка."
    }
    
    for string, answer in tests.items():
        assert App.complete_task2(string) == answer


def test_task3_correct_answer():
    tests = {
        "": "",
        "К Р А": "",
        "ККРРААККРРАА ККРРАА": "",
        "\n".join(["КоРмА", "КоРкА", "КоРчмА"]): "КоРмА",
        "\n".join(["КормА", "КОРма", "коРмА"]): "КормА КОРма коРмА"
    }
    
    for string, answer in tests.items():
        assert App.complete_task3(string) == answer


# Запуск: pytest tests.py