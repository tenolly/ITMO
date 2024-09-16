<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="ru-RU">

<head>
    <meta charset="UTF-8">
    <title>Лабораторная работа №2</title>
    <script src="static/js/jquery-3.7.1.min.js"></script>
    <link rel="stylesheet" href="static/css/index.css">
    <link rel="icon" type="image/jpg" href="static/media/favicon.ico">
</head>

<body>
    <div class="content-container">
        <header class="header">
            <div class="header-container">
                <div>Пышкин Никита Сергеевич P3213</div>
                <div></div>
                <div>409429</div>
            </div>
        </header>
        <main class="main">
            <div class="main__left-column">
                <div class="main__block">
                    <a class="link-to-form" href="/server/index">Вернуться к форме</a>
                </div>
            </div>
            <div>
                <div class="result-title">Результат</div>
                <div class="result-container">
                    <div style="border-top: 1px solid #000000;">X</div>
                    <div style="border-top: 1px solid #000000;">Y</div>
                    <div style="border-top: 1px solid #000000;">R</div>
                    <div style="border-top: 1px solid #000000;">Попал?</div>
                </div>
                <div class="result-container" id="result"></div>
            </div>
        </main>
    </div>
</body>

</html>
