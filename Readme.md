#Run redis
docker run --name demo-redis -p 6379:6379 --rm redis
#Run rabbitMQ
docker run --name demo-rabbitmq -p 5672:5672 --rm rabbitmq

#Build 
mvn clean package

#Run
forego start
