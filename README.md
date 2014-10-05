callete
====

Framework for developing (Java) clients for embedded systems (Raspberry Pi)

Installation
2014-06-20-wheezy-raspbian.zip, 4gb SD
ssh access
wlan

sudo apt-get update
sudo apt-get install mpd
sudo apt-get install mpc

sudo apt-get remove oracle-java7-jdk



mpd:
bind_to_address        "any"


git setup
Ensure ssh agent is running: eval "$(ssh-agent)" (raspberry pi)
Generate new public key: ssh-keygen -t rsa -C "your_email@example.com"
Then add your new key to the ssh-agent: ssh-add ~/.ssh/id_rsa
Run the following code to copy the key to your clipboard: cat ~/.ssh/id_rsa.pub
Add key to github: https://github.com/settings/ssh

maven setup
wget http://apache.openmirror.de/maven/maven-3/3.2.3/binaries/apache-maven-3.2.3-bin.tar.gz
tar -xvf apache-maven-3.2.3-bin.tar.gz

sudo /home/pi/apache-maven-3.2.3/bin/mvn install exec:java -Dexec.mainClass="callete.deployment.server.DeploymentService"


vi .bashrc
export M2_HOME=/home/pi/apache-maven-3.2.3
export M2=$M2_HOME/bin
export PATH=$M2:$PATH
bash

restart putty

build!

mvn install -Pdeployment


git clone https://github.com/jkiddo/gmusic.api.git
cd gmusic.api\desk.gmusic.api
mvn install -DskipTests

sudo  /home/pi/apache-maven-3.2.3/bin/mvn install
sudo  /home/pi/apache-maven-3.2.3/bin/mvn install -Pdeployment

