package com.example.demo;

import io.lettuce.core.ClientOptions;
import io.lettuce.core.ClientOptions.DisconnectedBehavior;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisFuture;
import io.lettuce.core.SocketOptions;
import io.lettuce.core.SocketOptions.KeepAliveOptions;
import io.lettuce.core.TimeoutOptions;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.resource.DefaultClientResources;
import io.lettuce.core.resource.DnsResolvers;
import io.lettuce.core.resource.NettyCustomizer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.epoll.EpollChannelOption;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LettucePing {

  @Value("${redis.dns-resolver:UNRESOLVED}")
  private DnsResolvers dsnResolver;

  @Value("${tcp.keepidle:10}")
  private Integer tcpKeepIdle;

  @Value("${tcp.keepintvl:1}")
  private Integer tcpKeepIntvl;

  @Value("${tcp.keepcnt:1}")
  private Integer tcpKeepCnt;

  @Value("${redis-url:127.0.0.1:6379}")
  private String redisUrl;

  private RedisCommands<String, String> redisCommands;
  private RedisAsyncCommands<String, String> redisAsyncCommands;
  

  @PostConstruct
  public void startup() {
    log.info ("Version " + "1.0");
    log.info("Iniializing Lettuce client...");
    createRedisCommands(redisUrl);
    redisCommands.set("foo", "bar");
  }

  @Scheduled(fixedRate = 5000)
  public void ping() {
    try {
      String pong = redisCommands.ping();
      log.info("PING: {}", pong);
      String v = redisCommands.get("foo");
      log.info("foo: {}", v);

      
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Scheduled(fixedRate = 5000)
  public void asyncPing() {
    try {
      RedisFuture<String> pong = redisAsyncCommands.ping();
      log.info("Async PING: {}", pong.get(1,TimeUnit.SECONDS));
      RedisFuture<String> v = redisAsyncCommands.get("foo");
      log.info("Async foo: {}", v.get(1, TimeUnit.SECONDS));

      
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void createRedisCommands(String redisClusterNode) {
    RedisClient redisClient = getRedisClient(redisClusterNode);
    StatefulRedisConnection<String, String> redisConnection = redisClient.connect();
    redisCommands = redisConnection.sync();
    redisAsyncCommands = redisConnection.async();
  }

  private RedisClient getRedisClient(String redisClusterNode) {
    String redisUri = String.format("redis://%s", redisClusterNode);
    log.info("Redis Client: {} ", redisUri);

    ClientOptions socketOptions =
        ClientOptions.builder()
            .timeoutOptions(TimeoutOptions.builder().fixedTimeout(Duration.ofSeconds(5)).build())
            .disconnectedBehavior(DisconnectedBehavior.REJECT_COMMANDS)
            .socketOptions(
                SocketOptions.builder()
                    .connectTimeout(Duration.ofSeconds(10))
                    .keepAlive(
                        KeepAliveOptions.builder()
                           .enable()
                            .idle(Duration.ofSeconds(tcpKeepIdle))
                            .interval(Duration.ofSeconds(tcpKeepIntvl))
                            .count(tcpKeepCnt)
                            .build())
                    .build())
            .build();

    RedisClient client =
        RedisClient.create(
            DefaultClientResources.builder()
                .dnsResolver(dsnResolver)
                .nettyCustomizer(
                    new NettyCustomizer() {
                      @Override
                      public void afterBootstrapInitialized(Bootstrap bootstrap) {
                        //bootstrap.option(EpollChannelOption.TCP_KEEPIDLE, tcpKeepIdle);
                        //bootstrap.option(EpollChannelOption.TCP_KEEPINTVL, tcpKeepIntvl);
                        //bootstrap.option(EpollChannelOption.TCP_KEEPCNT, tcpKeepCnt);
                        bootstrap.option(EpollChannelOption.TCP_USER_TIMEOUT,5000);

                        //                        bootstrap.option(
                        //
                        // NioChannelOption.of(ExtendedSocketOptions.TCP_KEEPIDLE), tcpKeepIdle);
                        //                        bootstrap.option(
                        //
                        // NioChannelOption.of(ExtendedSocketOptions.TCP_KEEPINTERVAL),
                        //                            tcpKeepIntvl);
                        //                        bootstrap.option(
                        //
                        // NioChannelOption.of(ExtendedSocketOptions.TCP_KEEPCOUNT), tcpKeepCnt);
                      }
                    })
                .build(),
            redisUri);

    client.setOptions(socketOptions);

    return client;
  }
}
