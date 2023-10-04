import redis
import json

#  docker run -it -e REDIS_ARGS="--maxmemory 50mb" --name redis-stack -p 6379:6379 -p 8001:8001  --rm redis/redis-stack:7.2.0-v0
def R(host="127.0.0.1", port=6379):
    return redis.StrictRedis(host=host, port=port)


if __name__ == '__main__':
    redis= R()

    with open('listingsAndReviews.json', 'r') as file:
        try:
            for line in file:
                j = json.loads(line)
                redis.set(j["_id"], line)
                #redis.json().set(j["_id"], '$', j)
        except Exception as e:
            print(e)
    print(redis.info("Keyspace"))
    print(redis.info("Memory"))
    redis.close()
