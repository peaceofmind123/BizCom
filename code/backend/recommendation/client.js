var net = require('net');
var client = net.connect({port: 8080}, function() {
    console.log('connected to server!');
});
client.on('data', function(data) {
    console.log(data.toString());
    client.end();
});
client.on('end', function() {
    console.log('disconnected from server');
});

var usr = 23
var top_n = 10
client.write('Hello from NodeJS \r\n');
 client.write('train_recommender\r\n')
client.write('get_top,'+usr+','+top_n+',\r\n')
// client.end()
client.write('end\r\n')


