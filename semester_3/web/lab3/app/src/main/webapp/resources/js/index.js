function updateClocks() {
    const date = new Date()
    let hours = date.getHours();
    let minutes = date.getMinutes();
    let day = date.getDate();
    let month = date.getMonth() + 1;
    let year = date.getFullYear();

    hours = hours < 10 ? "0" + hours : hours;
    minutes = minutes < 10 ? "0" + minutes : minutes;
    day = day < 10 ? "0" + day : day;
    month = month < 10 ? "0" + month : month;

    document.getElementById("time").innerHTML =  `${hours}:${minutes} ${day}:${month}:${year}`;
    setTimeout(updateClocks, 11000);
}

function formatTime(i) {
    if (i < 10) {i = "0" + i};
    return i;
}

window.onload = updateClocks;