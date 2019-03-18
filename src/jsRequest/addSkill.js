var XMLHttpRequest = require("xmlhttprequest").XMLHttpRequest;
var xhr = new XMLHttpRequest();
function readBody(xhr) {
    var data;
    if (!xhr.responseType || xhr.responseType === "text") {
        data = xhr.responseText;
    } else if (xhr.responseType === "document") {
        data = xhr.responseXML;
    } else {
        data = xhr.response;
    }
    return data;
}

xhr.onreadystatechange = function() {
    if (xhr.readyState == 4) {
        console.log(readBody(xhr));
    }
}
xhr.open('POST', 'http://localhost:8080/user/skill?userId=1', true);
var body = {
    "name":"CSS",
    "point":10
}
xhr.send(JSON.stringify(body))