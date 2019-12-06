#!/bin/sh

#monInterface=$1
#testPID=$2

connectWifi () {
	#sudo kill -TSTP $testPID
	echo "test.sh stopped"
	sudo airmon-ng stop $monInterface
	sudo systemctl start dhcpcd.service
        sleep 3
	curl -X PUT -d '"Detection"' 'https://thesis-775f8.firebaseio.com/users/Demo/Alert.json'
	sudo airmon-ng start $monInterface
	#sudo kill -CONT $testPID

}




inotifywait -m -e create -e modify /home/pi/Desktop/thesis --format %f | while read FILE
do 
	echo "file created/modified: $FILE"
	sudo tcpdump -A -r $FILE > /home/pi/Desktop/thesis/dump.txt

	if grep DeAuthentication /home/pi/Desktop/thesis/dump.txt; then
		echo "DEAUTH DETECTED"
		#mkdir detected
		#mv home/pi/Desktop/thesis/dumps/dump.txt detected
		#rm /home/pi/Desktop/thesis/dumps/dump.txt
		connectWifi $monInterface $testPID
	else
		echo "NO DEAUTH Detected"
		rm /home/pi/Desktop/thesis/dumps/dump.txt
		#connectWifi $monInterface $testPID
	fi
done
