# java-loom-test
Some example classes to test Java Project Loom / Virtual Threads. 

# How to run

1. Install a Loom preview build from here: http://jdk.java.net/loom/
2. Clone this repo or just copy paste to a new file. Each class has void main, and all are independent examples.
3. Run from either JDK / bin folder, OR edit your JAVA_HOME and Path variables to point to the Loom Preview JDK - currently JDK18+Loom, the examples use JDK17+Loom
4. Example: C:\Program Files\Java\jdk-17\bin>java HttpServerSimpleThread.java

# Attribution
The WaitAndHurry example is borrowed from this article: https://mbien.dev/blog/entry/taking-a-look-at-virtual which is released under this license: https://creativecommons.org/licenses/by-nc-sa/4.0/ . The original example has been changed - like adding an index for tasks to print.

The HttpServer ones are originals :) and are released under the MIT license. 
