from enum import IntEnum
from typing import Tuple, Callable

from .core import AbstractSolveIntegralMethod


class RectangleMethodType(IntEnum):
    LEFT = 0
    MIDDLE = 1
    RIGHT = 2


class RectangleMethod(AbstractSolveIntegralMethod):
    @classmethod
    def solve(cls, integral_func: Callable[[float], float], a: float, b: float, e: float, max_iter_count: int = 10**6, m_type: RectangleMethodType = RectangleMethodType.MIDDLE) -> Tuple[int, int]:
        results, n = [], 4
        while len(results) < 2 or not cls._check_calc_is_end(results[-2], results[-1], 2, e):
            if n >= max_iter_count:
                raise ValueError(f"Не удалось найти решение с удовлетворяющей точностью за {max_iter_count} разбиений")
            
            h = (b - a) / n
            match m_type:
                case RectangleMethodType.LEFT:
                    start = a
                case RectangleMethodType.MIDDLE:
                    start = a + h / 2
                case RectangleMethodType.RIGHT:
                    start = a + h
            
            s = 0
            for i in range(n):
                s += integral_func(start + h * i)
            
            results.append(s * h)
            n += 1
        
        return results[-1], n
