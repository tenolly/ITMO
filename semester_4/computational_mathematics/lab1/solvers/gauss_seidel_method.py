from typing import List, Any
from .core import AbstractIterMethod


class GaussSeidelMethod(AbstractIterMethod):
    @classmethod
    def solve(cls, accuracy: float, matrix: List[List[float]], max_iterations: int = 100000) -> Any:
        A, b = [], []
        for row in matrix:
            A.append(row[:-1])
            b.append(row[-1])
        
        if (res := cls.make_diagonally_dominant(A)) is None:
            return "Невозможно привести матрицу в диагональный вид"
        
        order, A = res

        x = [0] * len(A)
        for iteration_number in range(1, max_iterations + 1):
            x_old = x.copy()
            for i in range(len(A)):
                if A[i][i] == 0:
                    return "Методом Гаусса-Зейделя решить эту систему невозможно"
                
                sum_new = sum(A[i][j] * x[j] for j in range(i))
                sum_old = sum(A[i][j] * x_old[j] for j in range(i + 1, len(A)))
                x[i] = (b[i] - sum_new - sum_old) / A[i][i]
                
            e_vector = list(abs(x[i] - x_old[i]) for i in range(len(A)))
            if max(e_vector) < accuracy:
                norma = max([sum(abs(x) for x in row) for row in A])
                return (
                    cls.transform_answer_order(x, order),
                    norma,
                    iteration_number,
                    e_vector
                )

        return "Метод не сошелся за заданное количество итераций"
