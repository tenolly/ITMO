import os

import sympy as sp

from ploting import plot_system, plot_function
from solvers.system_methods import newton_system_method
from solvers.linear_methods import bisection_linear_method, newton_linear_method, simple_iteration_linear_method
    

def ask_numbers(label, amount, ntype=float):
    while True:
        try:
            user_input =  input(label)
            
            if os.path.isfile(user_input):
                user_input = open(user_input, mode="r").read()
                print(f"Произведено чтение из файла: {user_input}")
            else:
                print(f"Получен ручной ввод: {user_input}")
                
            numbers = tuple(map(ntype, user_input.replace(",", ".").split()))
            
            if len(numbers) != amount:
                raise ValueError()
            
            return numbers
        except Exception:
            print("Некорректный ввод, проверьте соответствие формату или корректность введенного пути.\n")


def choose_type_of_equation():
    print(
        "Введите номер типа решаемой задачи:\n"
        "1. Нелинейное уравнение\n"
        "2. Система нелинейных уравнений\n"
    )

    expected_types = ["1", "2"]
    while (type_of_equation := input("Введите номер: ").strip()) not in expected_types:
        print("Некорректный номер. Попробуйте снова.\n")
        
    return type_of_equation


def solve_linear_equation():
    print(
        "Выберите метод:\n"
        "1. Метод половинного деления\n"
        "2. Метод Ньютона\n"
        "3. Метод простой итерации\n"
    )

    expected_methods = ["1", "2", "3"]
    while (method := input("Введите номер метода: ").strip()) not in expected_methods:
        print("Некорректный номер. Попробуйте снова.\n")
    
    x = sp.Symbol("x")
    functions = {
        "1": x**2 - 0.5,  # Корень на [-1, 0] и [0, 1]
        "2": x**3 - 4*x + 1,  # Корень на [-3, -1], [-1, 1] и [1, 3]
        "3": x**20 - 10 * x**19 - 3 * x ** 14 + 0.124 * x ** 10 + x ** 7 - x ** 3 - 1,  # Корень на [-1, 0]
        "4": 2**x - 4  # Корень на [1, 3]
    }
    
    print("Выберите функцию:")
    for i, expr in functions.items():
        print(f"{i}. {expr}")
    
    while (func_choice := input("Введите номер функции: ").strip()) not in functions:
        print("Некорректный номер. Попробуйте снова.\n")
    
    a, b = ask_numbers("Введите границы интервала a и b через пробел (либо введите путь до файла с числами): ", 2)
    error = ask_numbers("Введите точность вычисления (либо введите путь до файла с этим числом): ", 1)[0]
    
    match method:
        case "1":
            root, value, iterations = bisection_linear_method(x, functions[func_choice], a, b, error)
        case "2":
            root, value, iterations = newton_linear_method(x, functions[func_choice], a, b, error)
        case "3":
            root, value, iterations = simple_iteration_linear_method(x, functions[func_choice], a, b, error)
    
    print(f"Найденный корень: {root}, f(корень) = {value}, итераций: {iterations}")
    plot_function(sp.lambdify(x, functions[func_choice]), a, b)


def solve_system_equation():
    x, y = sp.symbols("x y")
    systems = {
        "1": [  # Первое приближение (0, 0)
            x + (0.4 + sp.sin(y)),
            y - (sp.cos(x + 1)) / 2
        ],
        "2": [  # Первое приближение (1, 2)
            x ** 2 + y ** 2 - 4,
            y - 3 * x ** 2 
        ],
        "3": [
            sp.cos(x) - y,
            x ** 2 - y
        ]
    }
    
    print("Выберите систему:")
    for i, system in systems.items():
        print(f"{i})\n" + "\n".join(f"  {expr} = 0" for expr in system))
    print()
    
    while (system_choice := input("Введите номер системы: ").strip()) not in systems:
        print("Некорректный номер. Попробуйте снова.\n")
    
    print("Система будет решена методом Ньютона.")
        
    x0, y0 = ask_numbers("Введите начальное приближение x0 y0 (либо введите путь до файла с числами): ", 2)
    error = ask_numbers("Введите точность вычисления (либо введите путь до файла с этим числом): ", 1)[0]
    
    result_v, error_v, iterations = newton_system_method(x, y, systems[system_choice], x0, y0, error)
    print(
        f"Вектор неизвестных: {result_v}\n"
        f"Вектор погрешностей: {error_v}\n"
        f"Число итераций: {iterations}"
    )
    
    plot_system(x, y, systems[system_choice], result_v)


if __name__ == "__main__":
    try:
        match choose_type_of_equation():
            case "1":
                solve_linear_equation()
            case "2":
                solve_system_equation()
    except ValueError as e:
        print(f"Возникла ошибка во время вычислений: {e}")
    