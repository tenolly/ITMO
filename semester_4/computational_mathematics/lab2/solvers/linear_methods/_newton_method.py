import numpy as np
import sympy as sp


def newton_method(symbol, function, a, b, error=1e-6, max_iter=10_000):
    f = sp.lambdify(symbol, function)
    df = sp.lambdify(symbol, sp.diff(function, symbol))
    d2f = sp.lambdify(symbol, sp.diff(function, symbol, 2))
    
    for x in np.linspace(a, b, 1000):
        if f(x) * d2f(x) > 0:
            break
    else:
        raise ValueError("Невозможно найти начальное приближение.")
    
    iterations = 0
    for _ in range(max_iter):
        fx = f(x)
        dfx = df(x)
        
        if dfx == 0:
            raise ValueError("Производная равна нулю. Метод не применим.")
        
        x_new = x - fx / dfx
        if abs(x_new - x) < error:
            if x_new > b or x_new < a:
                raise ValueError(f"На промежутке [{a}, {b}] нет корня. Ближайший корень: {x_new}")
            
            return x_new, f(x_new), iterations
        
        x = x_new
        iterations += 1
    
    raise ValueError("Метод не сошелся за заданное число итераций")
