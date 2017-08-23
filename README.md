GTD Inbox: A Simple To-Do Application
=====================================

This project provides a simple to-do list application that rougly follows the
["Get Things Done" (GTD) methodology](https://en.wikipedia.org/wiki/Getting_Things_Done).

![Screenshot](https://raw.githubusercontent.com/cmiles74/gtdinbox/master/documentation/screenshot.png)

This is application is written in Java and leverages Swing for the user interface. I 
had written it a long, long time ago (back when my day job involved Swing) and I had
forgotten all about it. Recently there's been a lot of kerfuffle about 
[Electron](https://electron.atom.io/) and there's been some talk about how there's 
no reliable, cross-platform UI toolkit. While Swing is a far cry from Electron in
almost every way, it started me thinking about this application.

In any case, I migrated the project from Ant to Maven and moved to Hibernate 5. Much
to my surprise, this wasn't nearly as much work as I had feared.

## Building the Project

To build this project you need the 
[Java Developer's Kit](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
installed and configured as well as [Apache Maven](https://maven.apache.org/install.html).
With those installed, simply build the application like so...

    mvn install
    
This will run compile the application, run the tests and the build the final 
distribution.

## Running the Application

The final distribution is the ZIP archive named `gtdinbox-1.0-distribution.zip` (the
version number will change). When you unzip that archive you'll get a directory called 
`gtdinbox-1.0` that contains the project README and a folder for each distribution 
(Mac OSX, Unix-like and Windows). Inside each one is a reasonable executable for
that environment (on a Unix-like, you can run the included shell script).