import random
import string


def generate_random_login(size):
    return generate_random_text(size, string.digits)


def generate_random_password(size):
    return generate_random_text(size, string.digits)


def generate_random_text(size, letters=string.ascii_letters + string.digits):
    return "".join(random.choices(letters, k=size))
