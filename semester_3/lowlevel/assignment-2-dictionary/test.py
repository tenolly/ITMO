import os
import sys
import traceback
from subprocess import Popen, PIPE


def test_word_not_found():
    _, stderr = run_main("123")
    assert stderr == "word not found"


def test_word_found():
    data = {
        "": "",
        "12345678": "12345678",
        "key": "value",
        "second key": "another value"
    }

    for key, value in data.items():
        stdout, _ = run_main(key)
        assert stdout == value


def test_max_key():
    _, stderr = run_main("1" * 255)
    assert stderr != "key is too big"


def test_too_big_key():
    _, stderr = run_main("1" * 256)
    assert stderr == "key is too big"


def run_main(input_stream: str | None = None) -> str:
    filename = os.path.join(os.path.dirname(__file__), "build", "bin", "main")
    process = Popen([filename], stdin=PIPE, stdout=PIPE, stderr=PIPE, shell=None)

    if input_stream is None:
        input_stream = ""

    stdout, stderr = process.communicate(input_stream.encode("utf-8"))
    return stdout.decode("utf-8"), stderr.decode("utf-8")


if __name__ == "__main__":
    tests = [test_word_not_found, test_word_found, test_max_key, test_too_big_key]

    failed = 0
    for i, test in enumerate(tests):
        print(f"Ran {i + 1} test:", test.__name__)
        
        try:
            test()
            print("Result: Success")
        except Exception as e:
            failed += 1
            print("Result: Failed")
            print("Reason:", e.__class__.__name__)
            _, _, tb = sys.exc_info()
            traceback.print_tb(tb)

        print()
    
    print("Conclusion:")
    print(f"{len(tests) - failed} success / {failed} failed")