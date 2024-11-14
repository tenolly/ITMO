import requests


response = requests.post("http://localhost:41742/api/auth/refresh")

print(response.status_code)
print(response.cookies.get("access_token"), response.cookies.get("refresh_token"))
