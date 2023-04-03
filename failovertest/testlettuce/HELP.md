```
export JAVA_HOME=/root/jdk-12.0.2
export REDIS_URL=redis-12000.internal.k1.demo.redislabs.com:12000
../apache-maven-3.6.3/bin/mvn spring-boot:run
```

python failover_client.py --host redis-12000.internal.k1.demo.redislabs.com --port 12000

netstat -c -town|grep 12000

tcp6       0      0 10.142.0.15:34756       10.142.0.13:12000       ESTABLISHED keepalive (3.11/0/0)
tcp6       0      0 10.142.0.15:34756       10.142.0.13:12000       ESTABLISHED keepalive (2.11/0/0)
tcp6       0      0 10.142.0.15:34756       10.142.0.13:12000       ESTABLISHED keepalive (1.10/0/0)
tcp6       0      0 10.142.0.15:34756       10.142.0.13:12000       ESTABLISHED keepalive (0.10/0/0)
tcp6       0     14 10.142.0.15:34756       10.142.0.13:12000       ESTABLISHED on (0.75/2/0)
tcp6       0     14 10.142.0.15:34756       10.142.0.13:12000       ESTABLISHED on (1.40/3/0)
tcp6       0     14 10.142.0.15:34756       10.142.0.13:12000       ESTABLISHED on (0.39/3/0)
tcp6       0     14 10.142.0.15:34756       10.142.0.13:12000       ESTABLISHED on (1.50/4/0)
tcp6       0     14 10.142.0.15:34756       10.142.0.13:12000       ESTABLISHED on (0.50/4/0)
tcp6       0      1 10.142.0.15:35214       10.142.0.13:12000       SYN_SENT    on (0.87/0/0)
tcp6       0      1 10.142.0.15:35214       10.142.0.13:12000       SYN_SENT    on (1.88/1/0)
tcp6       0      1 10.142.0.15:35214       10.142.0.13:12000       SYN_SENT    on (0.88/1/0)

export JAVA_HOME=/root/jdk-12.0.2
../apache-maven-3.6.3/bin/mvn package -Dmaven.test.skip=true
docker build -t kamran/lettuce:1.0 .
docker run -it --rm -e REDIS_URL=redis-12000.internal.k1.demo.redislabs.com:12000 kamran/lettuce:1.0



cd /root/dockerized-redis-enterprise

docker-compose up
./setup-cluster.sh
docker cp fail.sh n1:/opt;docker cp fail.sh n2:/opt;docker cp fail.sh n3:/opt

docker exec -u root -it n1 bash

## create db
curl -k -vv -X "POST" "https://cluster.re.demo:9443/v1/bdbs" \
	 -H 'Content-Type: application/json' \
	 -u 'admin@re.demo:redis123' \
	 -d $'{
  "port": 12000,
  "version": "6.0",
  "name": "db",
  "replication": true, 
  "type": "redis",
  "memory_size": 1073741824
}'

rladmin cluster master set 3

docker run -it --name rediscli --network dockerized-redis-enterprise_re_cluster --dns 172.18.0.20   --rm redis redis-cli -h redis-12000.cluster.re.demo -p 12000


docker run -it --name redis --cap-add all --network dockerized-redis-enterprise_re_cluster --dns 172.18.0.20   --rm redis  redis-server

docker cp fail.sh redis:/data;docker exec -it redis apt update;apt install -y iptables

docker run -it --rm   --name lettuce --network dockerized-redis-enterprise_re_cluster --dns 172.18.0.20 -e REDIS_URL=redis:6379 kamran/lettuce:1.0


iptables -F INPUT
iptables -F OUTPUT

nsenter -t  6663  -n tcpdump -vvv -A -w testfail2.pcap
nsenter -t  27679 -n netstat -c -town

