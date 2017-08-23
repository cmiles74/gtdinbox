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
    
## Running the Project
    
The `target` directory will contain the application. If you are on Windows, simply 
double-click the `gtdinbox.exe` file to launch. If you are on Mac OS X, you'll find
the application bundle in the `gtdinbox-1.0` directory. If you are on Linux, you can 
make the launcher script executable and then run that.

    chmod +x ./gtdinbox.sh
    ./gtdinbox.sh
    
There are some minor bugs and the UI defaults to the old Metal theme on Linux. The
GTK theme works, but the dark variant looks absolutely awful; the application needs
to handle that more gracefully.