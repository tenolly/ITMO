import sympy as sp


def bisection_method(symbol, function, a, b, error=1e-6):
    f = sp.lambdify(symbol, function)
    
    if f(a) * f(b) >= 0:
        raise ValueError("Функция должна иметь разные знаки на концах отрезка")
    
    iterations = 0
    while (b - a) / 2 > error:
        c = (a + b) / 2
        if f(c) == 0:
            break
        elif f(a) * f(c) < 0:
            b = c
        else:
            a = c
        iterations += 1
    
    return c, f(c), iterations
