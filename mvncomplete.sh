mvn clean install > mvn.log && mvn sonar:sonar >> mvn.log && mvn site:site >> mvn.log && stageSite.sh >> mvn.log &
tailColor.sh mvn.log
