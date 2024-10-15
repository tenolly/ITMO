function checkData(x, y, r) {
    let resp = {
        isValid: true,
        reason: "Корректные данные"
    }

    if (isNaN(+x) || isNaN(+y) || isNaN(+r)) {
        resp.isValid = false;
        resp.reason = "Невалидные данные"
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

function validateForm() {
    let x = document.getElementById("form:x_input").value;
    let y = document.getElementById("form:y").value;
    let r = $('input[name="form:r"]:checked').val();

    let result = checkData(x, y, r)
    if (!result.isValid) {
        alert(result.reason);
        return false;
    }

    return true;
};

$(document).on("click", "#graph", function(e) {
    let rValue = $('input[name="form:r"]:checked').val();

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
    
    document.getElementById("checkForm:hiddenX").value = calculatedX;
    document.getElementById("checkForm:hiddenY").value = calculatedY;
    document.getElementById("checkForm:hiddenR").value = rValue;

    document.getElementById("checkForm:checkButton").click();
});
