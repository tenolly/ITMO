import matplotlib.pyplot as plt

import numpy as np
import sympy as sp


def plot_system(x_symbol, y_symbol, system, result_v):
    f1 = sp.lambdify([x_symbol, y_symbol], system[0], "numpy")
    f2 = sp.lambdify([x_symbol, y_symbol], system[1], "numpy")
    
    X, Y = np.meshgrid(
        np.arange(result_v[0] - 4, result_v[0] + 4, 0.025),
        np.arange(result_v[1] - 4, result_v[1] + 4, 0.025)
    )
    
    plt.contour(X, Y, f1(X, Y), [0])
    plt.contour(X, Y, f2(X, Y), [0])
    plt.axhline(0, color="black", linewidth=0.5)
    plt.axvline(0, color="black", linewidth=0.5)
    plt.grid()
    plt.show()
    