#!/bin/sh
deauth_detected=false



#Displays wireless interfaces to user
sudo airmon-ng

#Captures user specified interface to put into monitor mode
read -p "Enter which interface to use: " userInterface

#Starts interface in monitor mode
sudo airmon-ng start $userInterface
sudo airmon-ng check kill

#Captures user home network name
read -p "Enter your home network name: " userEssid


#Captures interace in monitor mode, couldnt figure out how to append the
#original variable so this is a temp work around
sudo airmon-ng
read -p "Enter the interface in monitor mode: " monInterface

#Begin monitoring output directory for new/modified files
thisPid=$$
export monInterface
export testPID
bash monitor.sh &

sudo airodump-ng --essid $userEssid -o pcap -w /home/pi/Desktop/thesis/dump --write-interval 10 $monInterface
