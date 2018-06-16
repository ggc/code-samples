docker run -d --name mysql-local -e MYSQL_ROOT_PASSWORD=r00t -v `pwd`/volumes/mysql:/var/lib/mysql -p 3306:3306 mysql
