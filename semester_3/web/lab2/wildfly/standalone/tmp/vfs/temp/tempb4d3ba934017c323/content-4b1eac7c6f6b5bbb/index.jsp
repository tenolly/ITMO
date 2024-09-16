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
        <form action="/server/check" method="get" id="form">
        <main class="main">
            <div class="main__left-column">
                <div class="main__block">
                    <img class="graph" src = "static/media/graph.svg"/>
                </div>
                <div class="main__block">
                    <div class="row">Параметры</div>
                    <div class="row">
                        <div>Выберете X:</div>
                        <div><input name="x" type="radio" value="-3">-3</div>
                        <div><input name="x" type="radio" value="-2">-2</div>
                        <div><input name="x" type="radio" value="-1">-1</div>
                        <div><input name="x" type="radio" value="0">0</div>
                        <div><input name="x" type="radio" value="1">1</div>
                        <div><input name="x" type="radio" value="2">2</div>
                        <div><input name="x" type="radio" value="3">3</div>
                        <div><input name="x" type="radio" value="4">4</div>
                        <div><input name="x" type="radio" value="5">5</div>
                    </div>
                    <div class="row">
                        <div>Выберете Y:</div>
                        <input name="y" id="y-input" type="text" placeholder="значение от -5 до 3" maxlength="7">
                    </div>
                    <div class="row">
                        <div>Выберете R:</div>
                        <div><input name="r" type="checkbox" value="1">1.0</div>
                        <div><input name="r" type="checkbox" value="1.5">1.5</div>
                        <div><input name="r" type="checkbox" value="2">2.0</div>
                        <div><input name="r" type="checkbox" value="2.5">2.5</div>
                        <div><input name="r" type="checkbox" value="3">3.0</div>
                    </div>
                </div>
                <button type="submit" class="main__block submit_button" style="margin-bottom: 15px;" id="submit">
                    Проверить
                </button>
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
        </form>
    </div>
    <script>
        $("input[type='checkbox']").on("change", function() {
            $("input[name='" + this.name + "']").not(this).prop("checked", false);
        });

        $("#form").submit("click", function() {
            let jsonData = {
                "x": $("input[name='x']:checked").val(),
                "y": $("input[name='y']").val(),
                "r": $("input[name='r']:checked").val()
            };

            if (isNaN(+jsonData.x) || isNaN(+jsonData.y) || isNaN(+jsonData.r) || +jsonData.y < -5 || +jsonData.y > 3) {
                alert("Некорректные данные");
                event.preventDefault();
                return;
            }
        })
    </script>
</body>

</html>
