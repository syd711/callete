#!/bin/sh
cd ~
sudo apt-get install mpd
sudo apt-get install mpc
cd ~
wget http://apache.openmirror.de/maven/maven-3/3.1.1/binaries/apache-maven-3.1.1-bin.tar.gz
tar -xvf apache-maven-3.1.1-bin.tar.gz
git clone https://github.com/jkiddo/gmusic.api.git
cd gmusic.api
cd desk.gmusic.api
sudo /home/pi/apache-maven-3.1.1/bin/mvn install -DskipTests
cd ~
cd callete
sudo /home/pi/apache-maven-3.1.1/bin/mvn install -DskipTests
cd callete-deployment
sudo chmod 777 startServer.sh
