Network Connections
===================

This document explains how network connections work in `snake-java`.

Starting the server
-------------------

The servers listens to a TCP socket for messages from client. You have to configure the port when starting the server:

    $ java -jar snake-server.jar 9400

When a client wants to join a game he first has to register at a server. Therefore the client requires the IP address (or hostname) and port of the server:

    $ java -jar snake-client.jar localhost 9400

Now a TCP connection between the client and the server will be established and the client registers itself.


