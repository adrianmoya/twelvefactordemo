#Run redis
docker run --name demo-redis -p 6379:6379 --rm redis:3.2.1-alpine
#Run rabbitMQ
docker run --name demo-rabbitmq -p 5672:5672 --rm rabbitmq:3.6.3
#Nginx load balancer
docker run --name demo-nginx-lb -p 80:80 --rm nginx:1.10.1-alpine


#Build 
mvn clean package

#Run
forego start
