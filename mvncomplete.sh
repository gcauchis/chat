mvn clean install && mvn sonar:sonar && mvn site:site && stageSite.sh > mvn.log &
tailColor.sh mvn.log
