FOOP Exercise 1: Multiplayer Snake in Java
==========================================

Usage
-----

There are two executables in the project, `server` and `client`. Both can be compiled and executed using [Ant](http://ant.apache.org).

Let's start with the server:

**Compile the server:**

    $ ant compile-server

**Create a JAR:**

    $ ant dist-server

**Run the server:**

    $ ant run-server

The commands for the client are very similar:

    $ ant compile-client
    $ ant dist-client
    $ ant run-client

Normally it should be enough to execute the `run-*` command and Ant should (re)compile the source files if necessary.




