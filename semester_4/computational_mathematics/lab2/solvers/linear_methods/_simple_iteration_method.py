import numpy as np
import sympy as sp


def _compute_lambda(df, a, b):
    max_value = df(a)
    for x in np.linspace(a, b, 1000):
        if abs(dfx_value := df(x)) > abs(max_value):
            max_value = dfx_value
            
    return 1 / max_value if max_value != 0 else 1


def simple_iteration_method(symbol, function, a, b, error=1e-6, max_iter=10_000):
    f = sp.lambdify(symbol, function)
    df = sp.lambdify(symbol, sp.diff(function, symbol))
    d2f = sp.lambdify(symbol, sp.diff(function, symbol, 2))
    f_phi = sp.lambdify(symbol, symbol - _compute_lambda(df, a, b) * f(symbol))
    
    for x in np.linspace(a, b, 1000):
        if f(x) * d2f(x) > 0:
            break
    else:
        raise ValueError("Невозможно найти начальное приближение.")
    
    iterations = 0
    for _ in range(max_iter):
        x_new = f_phi(x)
        
        if abs(x_new - x) < error:
            if x_new > b or x_new < a:
                raise ValueError(f"На промежутке [{a}, {b}] нет корня. Ближайший корень: {x_new}")
            
            return x_new, f(x_new), iterations
        
        x = x_new
        iterations += 1
    
    raise ValueError("Метод не сошелся за заданное число итераций")
