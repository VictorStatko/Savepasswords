_Requirements:_  
Docker-ce 18.09.7  
OpenJDK 11

_Before running:_
mvn clean install (on parent project)

_Running:_  
**For simple run** use docker-compose.yml  
sudo docker-compose -f docker-compose.yml up

**For debug** use docker-compose-debug.yml  
(Intelij debug https://www.jetbrains.com/help/idea/run-and-debug-a-spring-boot-application-using-docker-compose.html)  
don't forget in compose debug config set=true "--build/force build images
