$(document).on("change", 'input[name="r-value"]', function() {
    processPoints();
});

function processPoints() {
    const r = $('input[name="r-value"]:checked').val();

    $("#graph circle").remove();

    const rows = document.querySelectorAll("#form\\:table tbody tr");
    rows.forEach(row => {
        const cells = row.querySelectorAll("td");
        drawPoint(+cells[0].innerText, +cells[1].innerText, r, cells[3].innerText === "Да");
    });
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

function checkData(x, y, r) {
    let resp = {
        isValid: true,
        reason: "Корректные данные"
    }

    if (isNaN(+x) || isNaN(+y) || isNaN(+r)) {
        resp.isValid = false;
        resp.reason = "Невалидные данные"
    }

    if (+x < -4) {
        resp.isValid = false;
        resp.reason = "y должен быть больше или равен -4 (y=" + +x + ")";
    }
    if (+x > 4) {
        resp.isValid = false;
        resp.reason = "y должен быть меньше или равен 4 (y=" + +x + ")";
    }

    if (+y < -5) {
        resp.isValid = false;
        resp.reason = "y должен быть больше или равен -5 (y=" + +y + ")";
    }
    if (+y > 3) {
        resp.isValid = false;
        resp.reason = "y должен быть меньше или равен 3 (y=" + +y + ")";
    }

    if (+r < 0) {
        resp.isValid = false;
        resp.reason = "y должен быть больше или равен 0 (y=" + +r + ")";
    }
    if (+r > 4) {
        resp.isValid = false;
        resp.reason = "y должен быть меньше или равен 4 (y=" + +r + ")";
    }
    
    return resp;
}

function validateForm() {
    let x = $('input[name="x-value"]:checked').val();
    let y = document.getElementById("y-value").value;
    let r = $('input[name="r-value"]:checked').val();

    let result = checkData(x, y, r)
    if (!result.isValid) {
        alert(result.reason);
        return false;
    }

    return true;
};

$(document).on("click", "#graph", function(e) {
    let rValue = $('input[name="r-value"]:checked').val();

    if (rValue == null) {
        alert("Невозможно вычислить попадание");
        return;
    }

    let calculatedX = (e.pageX - $(this).offset().left - $(this).width() / 2) / 135 * rValue;
    let calculatedY = ($(this).height() / 2 - (e.pageY - $(this).offset().top)) / 135 * rValue;

    let result = checkData(calculatedX, calculatedY, rValue)
    if (!result.isValid) {
        alert(result.reason);
        return;
    }
});
