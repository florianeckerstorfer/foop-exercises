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
    $ ant run-client-1

Normally it should be enough to execute the `run-*` command and Ant should (re)compile the source files if necessary. However, if something in the `common` module was changed but nothing in an executable module, you need to manually clean `common` to force Ant to recompile `common`:

    $ ant clean-common run-client-1

There are multiple clients configured in the build file.

| Client |   Name   | Port |                          Note                         |
| :----- | :------- | :--- | :---------------------------------------------------- |
| 1      | Player 1 | 9501 |                                                       |
| 2      | Player 2 | 9502 |                                                       |
| 2b     | Player 2 | 9503 | If client 2 was already started, this should not work |




