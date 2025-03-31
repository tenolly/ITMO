import matplotlib.pyplot as plt
import numpy as np


def plot_function(f, a, b):
    x_vals = np.linspace(a, b, int((b - a) * 200))
    y_vals = [f(x) for x in x_vals]
    plt.plot(x_vals, y_vals, label=f"f(x)")

    plt.axhline(0, color="black", linewidth=0.5)
    plt.axvline(0, color="black", linewidth=0.5)
    plt.grid()
    
    plt.legend()
    plt.show()