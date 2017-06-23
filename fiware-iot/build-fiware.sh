# Creates network
docker network create -d bridge --subnet=172.18.0.0/16 fiware-network;

# Create and runs mysql
docker run -d -v `pwd`/volumes/mysql:/var/lub/mysql \\
    --name mysql -e MYSQL_ROOT_PASSWORD=r00t \\
    -p 3306 --net fiware-network --ip 172.18.0.2 mysql

# Creates and runs mongodb
docker run -d -v `pwd`/volumes/mongo:/data/db \\
    --name mongo -p 27017 --net fiware-network --ip 172.18.0.3 mongo:3.2 --nojournal


# Creates and runs Orion Context Broker
docker run -d --name cb --link mongo:mongo -p 1026 --net fiware-network --ip 172.18.0.4 fiware/orion -dbhost mongo

# Builds Cygnus
if [ `docker ps | grep cygnus` ]
    docker build ./cygnus/
fi

docker run -d -v `pwd`/volumes/cygnus:/usr/cygnus \\
    --name cygnus \\
    -p 5050:5050 -p 8081:8081 --net fiware-network --ip 172.18.0.5