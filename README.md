#callete

Callete is a framework for developing (Java) media clients for embedded systems (Raspberry Pi).

## Prequisites

This documentation is written for the following setup:

* Raspberry Pi Model B 512MB Ram with
* 2014-06-20-wheezy-raspbian.zip, 4gb SD installed and
* enabled ssh access
* overclocked to 900Mhz (recommended)
* shared graphic memory to 128MB
* enabled internet access

## Raspberry Pi Setup

Updating the existing Java installation:

* *sudo apt-get update*
* *sudo apt-get remove oracle-java7-jdk*

Download the latest JDK8 for Linux Arm and extract it: 

* *sudo gunzip jdk-8u6-linux-arm-vfp-hflt.gz*
* *sudo tar xvf jdk-8u6-linux-arm-vfp-hflt -C /opt*

Set default java and javac to the new installed jdk8 (the command failed when I executed it, caused by copy'n paste errors, so I inputted them manually): 

* *sudo update-alternatives -–install /usr/bin/javac javac /opt/jdk1.8.0_06/bin/javac 1*
* *sudo update-alternatives -–install /usr/bin/java java /opt/jdk1.8.0_06/bin/java 1*
* *sudo update-alternatives –config javac*
* *sudo update-alternatives –config java*

## Git Setup

Ensure ssh agent is running: 

* *eval $(ssh-agent)*

Generate new public key: 

* *ssh-keygen -t rsa -C "your_email@example.com"*

Then add your new key to the ssh-agent: 

* *ssh-add ~/.ssh/id_rsa*

Run the following code to copy the key to your clipboard: 

* *cat ~/.ssh/id_rsa.pub* 

Add key to github: https://github.com/settings/ssh

## Download and Install

Clone the callete repository into your pi's home directory and execute the install script:

* *git clone git@github.com:syd711/callete.git*
* *cd callete*
* *sudo chmod 777 install.sh*
* *sudo ./install.sh*


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


mpd:
bind_to_address        "any"
