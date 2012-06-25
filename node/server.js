
var http = require('http');
var port = process.argv[2];

http.createServer(function (request, response) {
  if(request.url = '/products') {
    response.writeHead(200, {'Content-Type': 'application/json'});
//    response.end(JSON.stringify(getJsonResponse()));
    setTimeout(function () {
      response.end(JSON.stringify(getJsonResponse()));
    }, 1000)
  }
}).listen(port);

console.log('Server running at http://127.0.0.1:' + port);

function getJsonResponse() {
  return {
      "name" : "3x2 chair",
      "colors" : [
        "white", "orange", "lavendor", "white", "orange", "lavendor", "white", "orange", "lavendor", 
        "white", "orange", "lavendor", "white", "orange", "lavendor", "white", "orange", "lavendor", 
        "white", "orange", "lavendor", "white", "orange", "lavendor", "white", "orange", "lavendor", 
        "white", "orange", "lavendor", "white", "orange", "lavendor", "white", "orange", "lavendor", 
        "white", "orange", "lavendor", "white", "orange", "lavendor", "white", "orange", "lavendor", 
        "white", "orange", "lavendor", "white", "orange", "lavendor", "white", "orange", "lavendor", 
        "white", "orange", "lavendor", "white", "orange", "lavendor", "white", "orange", "lavendor", 
        "white", "orange", "lavendor", "white", "orange", "lavendor", "white", "orange", "lavendor", 
        "white", "orange", "lavendor", "white", "orange", "lavendor", "white", "orange", "lavendor", 
        "white", "orange", "lavendor", "white", "orange", "lavendor", "white", "orange", "lavendor", 
        "white", "orange", "lavendor", "white", "orange", "lavendor", "white", "orange", "lavendor", 
        "white", "orange", "lavendor", "white", "orange", "lavendor", "white", "orange", "lavendor", 
        "white", "orange", "lavendor", "white", "orange", "lavendor", "white", "orange", "lavendor", 
        "white", "orange", "lavendor", "white", "orange", "lavendor", "white", "orange", "lavendor", 
        "white", "orange", "lavendor", "white", "orange", "lavendor", "white", "orange", "lavendor"
      ],
      "dimensions" : [
        20, 19.75, 17.75, 20, 19.75, 17.75, 20, 19.75, 17.75, 20, 19.75, 17.75, 
        20, 19.75, 17.75, 20, 19.75, 17.75, 20, 19.75, 17.75, 20, 19.75, 17.75, 
        20, 19.75, 17.75, 20, 19.75, 17.75, 20, 19.75, 17.75, 20, 19.75, 17.75, 
        20, 19.75, 17.75, 20, 19.75, 17.75, 20, 19.75, 17.75, 20, 19.75, 17.75, 
        20, 19.75, 17.75, 20, 19.75, 17.75, 20, 19.75, 17.75, 20, 19.75, 17.75, 
        20, 19.75, 17.75, 20, 19.75, 17.75, 20, 19.75, 17.75, 20, 19.75, 17.75, 
        20, 19.75, 17.75, 20, 19.75, 17.75, 20, 19.75, 17.75, 20, 19.75, 17.75, 
        20, 19.75, 17.75, 20, 19.75, 17.75, 20, 19.75, 17.75, 20, 19.75, 17.75, 
        20, 19.75, 17.75, 20, 19.75, 17.75, 20, 19.75, 17.75, 20, 19.75, 17.75, 
        20, 19.75, 17.75, 20, 19.75, 17.75, 20, 19.75, 17.75, 20, 19.75, 17.75, 
        20, 19.75, 17.75, 20, 19.75, 17.75, 20, 19.75, 17.75, 20, 19.75, 17.75, 
        20, 19.75, 17.75, 20, 19.75, 17.75, 20, 19.75, 17.75, 20, 19.75, 17.75
      ],
      "designer" : "marcor marran"
    };

}

