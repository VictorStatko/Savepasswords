_Requirements:_  
Docker-ce 18.09.7  


_Before running:_
1) clone content of file .env.example to new file .env 
2) change environment variables at .env file (if needed)
3) change data volume mount for auth-redis, personal-accounts-postgres, auth-postgres, user-postgres, zookeeper and kafka services(docker-compose.yml)
4) sudo chown -R 1001:root {zookeeper volume}
example: sudo chown -R 1001:root /home/victor/development/data/docker/zookeeper1/data
5) mvn clean package for every microservice

_Running:_  
**For simple run** use docker-compose.yml   
sudo docker-compose -f docker-compose.yml up --build

_Testing:_  
**For testing** use mvn test

_Debug:_  
**For debug** use docker-compose-debug.yml  
sudo docker-compose -f docker-compose.yml -f docker-compose-debug.yml up --build  
(Intelij debug https://www.jetbrains.com/help/idea/run-and-debug-a-spring-boot-application-using-docker-compose.html)  
don't forget in compose debug config set=true "--build/force build images

_Restart single service (example):_
docker-compose -f docker-compose.yml -f docker-compose-debug.yml up --detach --force-recreate --no-deps --build user-service

