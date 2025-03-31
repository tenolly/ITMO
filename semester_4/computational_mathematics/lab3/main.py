import sympy as sp

from solvers.simpsons_method import SimpsonsMethod
from solvers.trapezoid_method import TrapezoidMethod
from solvers.rectangle_method import RectangleMethod, RectangleMethodType


def ask_numbers(label, amount, ntype=float):
    while True:
        try:
            numbers = tuple(map(ntype, input(label).replace(",", ".").split()))
            
            if len(numbers) != amount:
                raise ValueError()
            
            return numbers
        except ValueError:
            print("Некорректный ввод, проверьте соответствие формату.")


def solve_by_rectangle_method(f):
    methods = {
        "1": {
            "name": "Метод левых прямоугольников",
            "value": RectangleMethodType.LEFT
        },
        "2": {
            "name": "Метод средних прямоугольников",
            "value": RectangleMethodType.MIDDLE
        },
        "3": {
            "name": "Метод правых прямоугольников",
            "value": RectangleMethodType.RIGHT
        }
    }
    
    print("Выберите тип метода:")
    for i, data in methods.items():
        print(f"{i}. {data['name']}")
    
    while (method_choice := input("Введите номер метода: ").strip()) not in methods:
        print("Некорректный номер. Попробуйте снова.\n")
        
    a, b = ask_numbers("Введите пределы интегрирования a b: ", 2)
    error = ask_numbers("Введите точность вычисления: ", 1)[0]
    
    res, n =  RectangleMethod.solve(f, a, b, error, m_type=methods[method_choice]["value"])
    print(f"Результат: {res} (найден за {n} разбиений)")


def solve_by_trapezoid_method(f):
    a, b = ask_numbers("Введите пределы интегрирования a b: ", 2)
    error = ask_numbers("Введите точность вычисления: ", 1)[0]
    
    res, n =  TrapezoidMethod.solve(f, a, b, error)
    print(f"Результат: {res} (найден за {n} разбиений)")


def solve_by_simpsons_method(f):
    a, b = ask_numbers("Введите пределы интегрирования a b: ", 2)
    error = ask_numbers("Введите точность вычисления: ", 1)[0]
    
    res, n =  SimpsonsMethod.solve(f, a, b, error)
    print(f"Результат: {res} (найден за {n} разбиений)")


def main():
    x = sp.Symbol("x")
    functions = {
        "1": x**2 - 0.5,
        "2": x**3 - 4*x + 1,
        "3": 2**x - 4,
        "4": x**3 + 2 * x**2 - 3 * x - 12
    }
    
    print("Выберите функцию:")
    for i, expr in functions.items():
        print(f"{i}. {expr}")
    
    while (func_choice := input("Введите номер функции: ").strip()) not in functions:
        print("Некорректный номер. Попробуйте снова.\n")
    
    methods = {
        "1": {
            "name": "Метод прямоугольников",
            "func": solve_by_rectangle_method
        },
        "2": {
            "name": "Метод трапеций",
            "func": solve_by_trapezoid_method
        },
        "3": {
            "name": "Метод Симпсона",
            "func": solve_by_simpsons_method
        }
    }
    
    print("Выберите метод:")
    for i, data in methods.items():
        print(f"{i}. {data['name']}")
    
    while (method_choice := input("Введите номер метода: ").strip()) not in methods:
        print("Некорректный номер. Попробуйте снова.\n")
    
    methods[method_choice]["func"](sp.lambdify(x, functions[func_choice]))

if __name__ == "__main__":
    main()
