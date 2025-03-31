from typing import Tuple, Callable

from .core import AbstractSolveIntegralMethod


class SimpsonsMethod(AbstractSolveIntegralMethod):
    @classmethod
    def solve(cls, integral_func: Callable[[float], float], a: float, b: float, e: float, max_iter_count: int = 10**6) -> Tuple[int, int]:
        results, n = [], 4
        while len(results) < 2 or not cls._check_calc_is_end(results[-2], results[-1], 4, e):
            if n >= max_iter_count:
                raise ValueError(f"Не удалось найти решение с удовлетворяющей точностью за {max_iter_count} разбиений")
            
            h = (b - a) / n
            
            s = 0
            for i in range(n):
                result = integral_func(a + h * i)
                if i % 2 == 0 and i != 0 and i != n - 1:
                    result *= 4
                elif i % 2 != 0 and i != 0 and i != n - 1:
                    result *= 2
                
                s += result
            
            results.append(s * (h / 3))
            n += 2
        
        return results[-1], n
