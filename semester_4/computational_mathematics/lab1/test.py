import random

matrix = []
for i in range(20):
    row = [random.randint(1, 10) for _ in range(21)]
    row[i] = random.randint(1000, 2000)
    matrix.append(" ".join(map(str, row)))
    
print("\n".join(matrix))
