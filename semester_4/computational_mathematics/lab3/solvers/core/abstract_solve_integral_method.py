from typing import Tuple, Callable
from abc import ABC, abstractmethod


class AbstractSolveIntegralMethod(ABC):
    @classmethod
    @abstractmethod
    def solve(cls, integral_func: Callable[[float], float], a: float, b: float, e: float, max_iter_count: int = 10**6) -> Tuple[int, int]:
        pass
    
    @classmethod
    def _check_calc_is_end(cls, first_value: float, second_value: float, k: int, e: float) -> bool:
        return abs(first_value - second_value) / (2**k + 1) < e
    