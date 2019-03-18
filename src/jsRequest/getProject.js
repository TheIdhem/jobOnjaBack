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
xhr.open('GET', 'http://localhost:8080/project/20955d46-0aac-4a6a-b546-d1581026663f?userId=1', true);
xhr.send(null);