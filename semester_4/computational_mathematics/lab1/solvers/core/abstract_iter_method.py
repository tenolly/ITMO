from abc import ABC, abstractmethod
from typing import List, Any, Optional, Tuple


class AbstractIterMethod(ABC):
    @classmethod
    @abstractmethod
    def solve(cls, accuracy: float, matrix: List[List[float]], max_iterations: int) -> Any:
        pass
    
    @classmethod
    def make_diagonally_dominant(cls, matrix: List[List[float]]) -> Optional[Tuple[List[int], List[List[float]]]]:
        def swap_columns(order: List[int], matrix: List[List[float]], i: int, j: int) -> List[List[float]]:
            order[i], order[j] = order[j], order[i]
            for row in matrix:
                row[i], row[j] = row[j], row[i]
        
        order = [i for i in range(len(matrix))]
        for row_index, row in enumerate(matrix):
            max_value, max_index = float("-inf"), None
            for i, value in enumerate(row):
                if value > max_value:
                    max_value = value
                    max_index = i
                
            if max_index is None:
                return
            
            swap_columns(order, matrix, row_index, max_index)
        
        if cls.is_diagonally_dominant(matrix):
            return order, matrix
    
    @classmethod
    def transform_answer_order(cls, vector: List[float], order: List[int]) -> List[float]:
        ordered_x = [0] * len(vector)
        for i, elem in enumerate(vector):
            ordered_x[order.index(i)] = elem
        
        return ordered_x
    
    @classmethod
    def is_diagonally_dominant(cls, matrix: List[List[float]]) -> bool:
        n = len(matrix)
        
        if n == 1:
            return True
        
        strict_equality = False
        for i in range(n):
            row_sum = sum(abs(matrix[i][j]) for j in range(n) if i != j)
            if abs(matrix[i][i]) < row_sum:
                return False
            
            if not strict_equality and abs(matrix[i][i]) > row_sum:
                strict_equality = True
            
        return strict_equality