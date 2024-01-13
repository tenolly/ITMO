import time

from task1 import yaml_to_json as solution1
from task1 import yaml_to_json as solution2


def benchmark(func, args, repeats=100):
    start_time = time.time()
    for _ in range(repeats):
        func(*args)
    return time.time() - start_time


file = open("schedule.yaml", mode="r", encoding="utf-8")
print(benchmark(solution1, args=(file,)))
print(benchmark(solution2, args=(file,)))
