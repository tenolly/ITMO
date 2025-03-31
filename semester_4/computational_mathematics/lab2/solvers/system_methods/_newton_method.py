import numpy as np
import sympy as sp


def newton_method(x_symbol, y_symbol, system, x0, y0, error, max_iter=10_000):
    f1 = sp.lambdify([x_symbol, y_symbol], system[0])
    f2 = sp.lambdify([x_symbol, y_symbol], system[1])
    df1dx = sp.lambdify([x_symbol, y_symbol], sp.diff(system[0], x_symbol))
    df1dy = sp.lambdify([x_symbol, y_symbol], sp.diff(system[0], y_symbol))
    df2dx = sp.lambdify([x_symbol, y_symbol], sp.diff(system[1], x_symbol))
    df2dy = sp.lambdify([x_symbol, y_symbol], sp.diff(system[1], y_symbol))
    
    for i in range(max_iter):
        c1, c2, a1 = df1dx(x0, y0), df1dy(x0, y0), -f1(x0, y0)
        c3, c4, a2 = df2dx(x0, y0), df2dy(x0, y0), -f2(x0, y0)
        
        determinant = c1 * c4 - c2 * c3
        if abs(determinant) < 1e-9:
            raise ValueError("Якобиан вырожден, метод Ньютона не применим. Вы можете попробовать другое приближение.")
        
        delta_y = (a2 / c4 - c3 * a1 / (c1 * c4)) / (1 - c3 * c2 / (c1 * c4))
        delta_x = (a1 - c2 * delta_y) / c1
        
        x1 = x0 + delta_x
        y1 = y0 + delta_y
        
        if abs(x1 - x0) <= error and abs(y1 - y0) <= error:
            return [x1, y1], [x1 - x0, y1 - y0], i
        
        x0, y0 = x1, y1
    
    raise ValueError("Метод не сошелся за заданное число итераций.")
