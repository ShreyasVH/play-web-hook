if test -f "dist/RUNNING_PID"
then
	echo "Stopping Webhooks Server";
	kill -9 $(cat dist/RUNNING_PID);
	rm dist/RUNNING_PID;
fi