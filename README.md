# Surveillance Center

Surveillance Center is an open source, self-hosted, web-based video surveillance software.

It accepts images using the built-in FTP server and has a nice interface to 
browse and manage surveillance snapshots.


## Features

* Written in Java. Runs on Windows, Mac OS X and Linux (Ubuntu, Raspbian, etc.)
* Built-in FTP server to receive new surveillance images
* Thumbnail overview of images by date
* Mobile device friendly layout (responsive design)
* Ability to archive already seen images
* Auto removal of old archived images
* Configurable push notifications (via pushover.net)
* Optional image copy to remote server (FTP, WebDAV)
* Camera health check
* Simple livestream and snapshot view of cameras

Surveillance Center doesn't do any kind of motion detection. 
You either have to use the build-in motion detection of your IP camera or 
a third party software like [motion](https://github.com/Motion-Project/motion).


## Screenshots

[![screenshot1](https://raw.githubusercontent.com/1element/sc/master/docs/screenshots/screenshot1-thumbnail.png)](https://raw.githubusercontent.com/1element/sc/master/docs/screenshots/screenshot1.png)
[![screenshot2](https://raw.githubusercontent.com/1element/sc/master/docs/screenshots/screenshot2-thumbnail.png)](https://raw.githubusercontent.com/1element/sc/master/docs/screenshots/screenshot2.png)
[![screenshot3](https://raw.githubusercontent.com/1element/sc/master/docs/screenshots/screenshot3-thumbnail.png)](https://raw.githubusercontent.com/1element/sc/master/docs/screenshots/screenshot3.png)


## Installation

1. Make sure you have the Java 8 Runtime Environment installed on 
your system. If not you can [download it at Oracle](http://www.oracle.com/technetwork/java/javase/downloads/jre8-downloads-2133155.html).

2. Download the latest release of Surveillance Center (jar file) from 
[the releases page](https://github.com/1element/sc/releases).

3. Save a copy of [application-example.properties](https://raw.githubusercontent.com/1element/sc/master/src/main/resources/application-example.properties) 
as `application.properties` in the same directory where the downloaded 
jar file is located.

   For instance:

   ```
   wget https://raw.githubusercontent.com/1element/sc/master/src/main/resources/application-example.properties -O application.properties
   ```

4. Edit the `application.properties` file. This is the main configuration file 
where your IP cameras, FTP server credentials, push notifications, etc. are 
configured.

   Most settings can be left at the default value. But there are a few required 
ones, see the [configuration document](https://github.com/1element/sc/blob/master/docs/configuration.md) 
for details.

5. Run the following command in a terminal to start the embedded Apache Tomcat 
application server:

   ```
   java -jar suveillance-center-<VERSION>.jar
   ```

   Once you see the output line `Started SurveillanceCenterApplication in X.X seconds` 
you should be able to visit [http://localhost:8080/sc/](http://localhost:8080/sc/) 
in your browser.


## Further documentation

[configuration.md](https://github.com/1element/sc/blob/master/docs/configuration.md): 
Some notes on how to configure the application (`application.properties`).

[developers.md](https://github.com/1element/sc/blob/master/docs/developers.md): 
For developers only. How to setup a local development environment and build 
the project from the source code.


## Contributing

Contributions are welcome. Please read [CONTRIBUTING.md](https://github.com/1element/sc/blob/master/CONTRIBUTING.md) 
for details.


## License

This project is licensed under the terms of the GNU Affero General Public License 
as published by the Free Software Foundation, either version 3 of the License, 
or (at your option) any later version.

For more information, see [LICENSE.md](https://github.com/1element/sc/blob/master/LICENSE.md).