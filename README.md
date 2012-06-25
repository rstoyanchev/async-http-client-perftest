
Run The Server
==============

You need to have [node and npm installed](https://github.com/joyent/node/wiki/Installation).

To start the node server:
````
cd client
./start.sh
````

This starts 10 instances of node.js at ports 1330 through 1339. This allows sending up to 1,000 concurrent requests to each instance. In my tests, that's roughly what each node instance can handle before it starts taking a very long time to respond.

Build and Run The Clients
=========================

The client sub-project contains two Java clients -- RestTemplateClient and HttpComponentsClient (NIO-based), which send a large number of concurrent requests (see the source code for details) to read JSON. Each client can be started as a Java application and have a `System.in.read()` at the end to allow you to inspect the memory with something like `jvisualvm` before exiting.

