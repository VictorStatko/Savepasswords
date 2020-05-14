**Requirements:**  
Docker-ce 18.09.7  
Browser with Web Crypto API and IndexedDB support  
Node js (for local development)

**Before development/production deploy:**
1) clone content of file .env.example to new file .env 
2) change environment variables at .env file (if needed)
3) sudo chown -R 1001:root {zookeeper volume}
example: sudo chown -R 1001:root /home/victor/development/data/docker/zookeeper1/data
4) sudo chown -R 1001:root {kafka volume}
example: sudo chown -R 1001:root /home/victor/development/data/docker/kafka1/data
5) mvn clean package for every microservice

**Development:** 
sudo docker-compose -f docker-compose.yml -f docker-compose-dev.yml up --build  
cd frontend  
npm start  
(Intelij debug https://www.jetbrains.com/help/idea/run-and-debug-a-spring-boot-application-using-docker-compose.html)  
don't forget in compose debug config set=true "--build/force build images

**Testing:**  
For testing use mvn test (only unit) or mvn verify (unit and integration). Currently tests only in personalAccountService.

**Production:**
pass show docker-credential-helpers/docker-pass-initialized-check
start: sudo docker-compose -f docker-compose.yml -f docker-compose-prod.yml up --detach --force-recreate --no-deps --build
pull: docker-compose -f docker-compose.yml -f docker-compose-prod.yml pull
stop: sudo docker-compose -f docker-compose.yml -f docker-compose-prod.yml stop
**Restart single service (example):**
docker-compose -f docker-compose.yml -f docker-compose-debug.yml up --detach --force-recreate --no-deps --build email-service

