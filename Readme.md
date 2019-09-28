_Requirements:_  
Docker-ce 18.09.7  

_Before running:_
1) clone content of file .env.example to new file .env 
2) change environment variables at .env file (if needed)

_Running:_  
**For simple run** use docker-compose.yml  
sudo docker build -t dependencies .  
sudo docker-compose -f docker-compose.yml up --build

_Testing:_
**For testing** use docker-compose-test.yml  
sudo docker build -t dependencies .  
sudo docker-compose -f docker-compose.yml -f docker-compose-test.yml up --build

**For debug** use docker-compose-debug.yml  
sudo docker build -t dependencies .  
sudo docker-compose -f docker-compose.yml -f docker-compose-debug.yml up --build  
(Intelij debug https://www.jetbrains.com/help/idea/run-and-debug-a-spring-boot-application-using-docker-compose.html)  
don't forget in compose debug config set=true "--build/force build images
