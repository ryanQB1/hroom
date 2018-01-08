var wsUri = "ws://" + document.location.host + document.location.pathname + "hroomendpoint";
var websocket = new WebSocket(wsUri);
var loggedin = false;
var admin = false;
var today = new Date();
var currus;

websocket.onerror = function(evt) { onError(evt); };

function onError(evt) {
    writeToScreen('<span style="color: red;">ERROR:</span> ' + evt.data);
}

function sendLoginRequest() {
    if(loggedin) loggedin = false;
    var pson = JSON.stringify({
        "requesttype": "login",
        "request": document.loginForm.username.value+"#"+document.loginForm.password.value
    });
    sendText(pson);
}

function newAccountRequest() {
    var pson = JSON.stringify({
        "requesttype": "newacc",
        "request": document.loginForm.username.value+"#"+document.loginForm.password.value
    });
    sendText(pson);
}

function sendRentRequest() {
    var usernae = currus;
    if(admin) usernae = document.orderForm.admus.value;
    var pson = JSON.stringify({
        "requesttype": "makeorder",
        "request": usernae+"#"+document.orderForm.datum.value+"#"+document.orderForm.room1.value+"#"+document.orderForm.room2.value+"#"+document.orderForm.room3.value
    });
    sendText(pson);
}

function sendRefreshRequest() {
    var usernae = currus;
    if(admin) usernae = document.orderForm.admus.value;
    var pson = JSON.stringify({
        "requesttype": "refresh",
        "request": usernae+"#"+document.orderForm.datum.value+"#"+admin
    });
    sendText(pson);
}

function handleInput(inp){
    console.log("Recieving and outputting input");
    var json = JSON.parse(inp);
    console.log(json.generaltext);
    console.log("LoggedIn: " + json.loggedin);
    console.log(json.loggedin==="true");
    if(!loggedin && json.loggedin) currus = json.username;
    loggedin=json.loggedin;
    document.getElementById("gentext").innerHTML = json.generaltext;
    document.getElementById("datum1").value = json.date;
    document.getElementById("datum1").min = today;
    document.getElementById("admus").hidden = !json.admin;
    document.getElementById("admusinf").hidden = !json.admin;
    document.getElementById("room1av").innerHTML = json.room1av;
    document.getElementById("room2av").innerHTML = json.room2av;
    document.getElementById("room3av").innerHTML = json.room3av;
    document.getElementById("room1my").value = json.room1my;
    document.getElementById("room2my").value = json.room2my;
    document.getElementById("room3my").value = json.room3my;
    admin = json.admin;
}
