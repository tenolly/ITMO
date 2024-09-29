function saveData(x, y, r) {
    localStorage.setItem("x", x);
    localStorage.setItem("y", y);
    localStorage.setItem("r", r);
}

function loadData() {
    if (localStorage.getItem("x")) {
        $("input[name='x']").val(localStorage.getItem("x"));
    }

    if (localStorage.getItem("y")) {
        $("select[name='y']").val(localStorage.getItem("y"));
    }

    if (localStorage.getItem("r")) {
        $("input[name='r'][value='" + localStorage.getItem("r") + "']").prop("checked", true);
    }
}

function checkData(x, y, r) {
    let resp = {
        isValid: true,
        reason: "Корректные данные"
    }

    if (isNaN(+x) || isNaN(+y) || isNaN(+r)) {
        resp.isValid = false;
        resp.reason = "Невалидные данные"
    }

    if (+x < -5) {
        resp.isValid = false;
        resp.reason = "x должен быть больше или равен -5 (x=" + +x + ")"
    }

    if (+x > 5) {
        resp.isValid = false;
        resp.reason = "x должен быть меньше или равен 5 (x=" + +x + ")";
    }

    if (+y < -5) {
        resp.isValid = false;
        resp.reason = "y должен быть больше или равен -5 (y=" + +y + ")";
    }
    if (+y > 3) {
        resp.isValid = false;
        resp.reason = "y должен быть меньше или равен 3 (y=" + +y + ")";
    }
    
    return resp;
}

function drawPoint(x, y, r, result) {
    let circle = document.createElementNS("http://www.w3.org/2000/svg", "circle");
    circle.setAttribute("cx", x * 170 / r + 200);
    circle.setAttribute("cy", -y * 170 / r + 200);
    circle.setAttribute("r", 4);

    circle.style.stroke = "black";
    circle.style["stroke-width"] = "1px";
    circle.style.fill = result? "#0ecc14" : "#d1220f";
    
    $("#graph").append(circle);
}

$("input[type='checkbox']").on("change", function() {
    $("input[name='" + this.name + "']").not(this).prop("checked", false);
});

$("#form").submit(function(e) {
    let jsonData = {
        "x": $("input[name='x']").val(),
        "y": $("option[name='y']:selected").val(),
        "r": $("input[name='r']:checked").val()
    };
    
    let result = checkData(jsonData.x, jsonData.y, jsonData.r)
    if (!result.isValid) {
        e.preventDefault();
        alert(result.reason);
        return;
    }

    saveData(+jsonData.x, +jsonData.y, +jsonData.r)
});

$("#graph").on("click", function(e) {
    let rValue = $("input[name='r']:checked").val();

    if (rValue == null) {
        alert("Невозможно вычислить попадание");
        return;
    }

    let calculatedX = (e.pageX - $(this).offset().left - $(this).width() / 2) / 150 * rValue;
    let calculatedY = ($(this).height() / 2 - (e.pageY - $(this).offset().top)) / 150 * rValue;

    let result = checkData(calculatedX, calculatedY, rValue)
    if (!result.isValid) {
        alert(result.reason);
        return;
    }
    
    window.location.href = "/server/check?x=" + calculatedX + "&y=" + calculatedY + "&r=" + rValue
});
