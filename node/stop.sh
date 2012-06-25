
ps a | grep "node server.js" | grep -v grep | sed 's/ [a-z].*//' | xargs kill -9

