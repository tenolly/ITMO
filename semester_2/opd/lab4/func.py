def f(num):
    _730 = 1213
    _731 = 233

    if num <= 0 or num >= _730:
        return _730
    return num * 3 - _731


_57F = -100  # Z
_580 = 1200  # Y
_581 = 1500  # X
_582 = 2196

print(f(1201) + f(1500) + f(-100))
# F(Y + 1) + F(Z) - F(X)
