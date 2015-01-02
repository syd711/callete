#callete

Callete is a framework for developing (Java) media clients for embedded systems (Raspberry Pi).

# Latest Updates

02.01.2015 - Adding documentation

## Prequisites

This documentation is written for the following setup:

* Raspberry Pi Model B 512MB Ram with
* 2014-06-20-wheezy-raspbian.zip, 4gb SD installed and (http://www.raspberrypi.org/downloads/)
* enabled ssh access (*sudo raspi-config*)
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

I recommend to overclock the Pi, you can do set in the setup menu by calling

* *sudo raspi-config*

Apply the settings:
* overclocked to 900Mhz (recommended)
* shared graphic memory to 128MB

Optional:
Disable screensaver for console (used when a TFT with UI is connected): Disable text terminals from blanking
change two settings in /etc/kbd/config

* *BLANK_TIME=0*
* *POWERDOWN_TIME=0*

Adjust settings of your custom UI (e.g. a JavaFX or AWT GUI):
Edit the /boot/config.txt file and customize the overscan properties, e.g.:

* *overscan_bottom=16*

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

Clone the callete repository into your pi's home directory and execute the install script.
The installation may take a while, so drink a coffee or two:

* *git clone git@github.com:syd711/callete.git*
* *cd callete*
* *sudo chmod 777 install.sh*
* *./install.sh*

If the installation fails for some reasons, check the install script and check the separate steps.
The install script installs maven into the home directory, the default configuration of callete
expects it to be there. This is a little bit dirty, I hope I'm gonna fix this in a future release.

## Fix mpd Setup

To be able to connect to MPD from any host, it is necessary to configure the mpd conf:

* *sudo vi /etc/mpd.conf*
* Find and change the line: *bind_to_address        "any"*
* *sudo service mpd restart*

## Starting the Deployment Server

Before we can start the deployment server, we have to configure the IP address of the Raspberry Pi:

* edit *~/callete/conf/callete.properties*
* *deployment.host = <YOUR_IP>*

Afterwards, start the server:

* *cd callete*
* *cd callete-deployment*
* *sudo ./startServer.sh*

# Example Project

callete comes with a project template. It can be cloned from here: https://github.com/syd711/callete-template

The project comes with a setup for the callete deployment server and documentation how to use the framework.


