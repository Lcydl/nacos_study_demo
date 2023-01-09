# Nacos学习项目

<!-- TOC -->
* [Nacos学习项目](#nacos学习项目)
  * [微服务](#微服务)
    * [定义](#定义)
    * [构成](#构成)
  * [SpringCloud](#springcloud)
    * [版本](#版本)
    * [组件](#组件)
  * [注册中心](#注册中心)
    * [Eureka](#eureka)
      * [服务治理](#服务治理)
      * [服务注册与发现](#服务注册与发现)
      * [Server与Client](#server与client)
      * [自我保护](#自我保护)
      * [使用教程](#使用教程)
        * [服务端](#服务端)
        * [客户端](#客户端)
    * [Consul](#consul)
      * [使用教程](#使用教程-1)
    * [三个注册中心异同点](#三个注册中心异同点)
    * [CAP原理](#cap原理)
  * [负载均衡](#负载均衡)
    * [Ribbon](#ribbon)
      * [Ribbon与Nginx的区别](#ribbon与nginx的区别)
      * [负载均衡策略](#负载均衡策略)
      * [负载均衡算法](#负载均衡算法)
      * [使用教程](#使用教程-2)
    * [OpenFeign](#openfeign)
      * [使用教程](#使用教程-3)
  * [服务降级](#服务降级)
    * [Hystrix](#hystrix)
      * [服务雪崩](#服务雪崩)
      * [使用教程](#使用教程-4)
<!-- TOC -->

## 微服务

### 定义

微服务架构是一种架构模式，它提倡将单一应用程序划分为**一组小的服务**，服务之间互相协调、互相配合，为用户提供最终价值。

每个服务运行在其**独立的进程**中，服务与服务间采用**轻量级的通信机制**互相协作（通常是基于HTTP协议的Restful API）。

每个服务都围绕着具体业务进行构建，并且能够被**独立的部署**到生产环境、类生产环境等。

另外，应当尽量避免统一的、集中式的服务管理机制，对具体的一个服务而言，应根据业务上下文，选择合适的语言、工具对其进行构建。


### 构成

- 服务注册与发现
- 服务调用
- 服务熔断
- 负载均衡
- 服务降级
- 服务消息队列
- 配置中心管理
- 服务网关
- 服务监控
- 全链路追踪
- 自动化构建部署
- 服务定时任务调度操作


## SpringCloud

### 版本

| Cloud版本                                                    | Boot版本                              |
| :----------------------------------------------------------- | :------------------------------------ |
| [2021.0.x](https://github.com/spring-cloud/spring-cloud-release/wiki/Spring-Cloud-2021.0-Release-Notes) aka Jubilee | 2.6.x                                 |
| [2020.0.x](https://github.com/spring-cloud/spring-cloud-release/wiki/Spring-Cloud-2020.0-Release-Notes) aka Ilford | 2.4.x, 2.5.x (Starting with 2020.0.3) |
| [Hoxton](https://github.com/spring-cloud/spring-cloud-release/wiki/Spring-Cloud-Hoxton-Release-Notes) | 2.2.x, 2.3.x (Starting with SR5)      |
| [Greenwich](https://github.com/spring-projects/spring-cloud/wiki/Spring-Cloud-Greenwich-Release-Notes) | 2.1.x                                 |
| [Finchley](https://github.com/spring-projects/spring-cloud/wiki/Spring-Cloud-Finchley-Release-Notes) | 2.0.x                                 |
| [Edgware](https://github.com/spring-projects/spring-cloud/wiki/Spring-Cloud-Edgware-Release-Notes) | 1.5.x                                 |
| [Dalston](https://github.com/spring-projects/spring-cloud/wiki/Spring-Cloud-Dalston-Release-Notes) | 1.5.x                                 |

> [版本查看](https://start.spring.io/actuator/info)

### 组件

| 注册中心         | 负载均衡          | 服务调用        | 服务降级           | 服务网关       | 服务配置         | 服务总线      |
|--------------|---------------|-------------|----------------|------------|--------------|-----------|
| ~~`Eureka`~~ | `Ribbon`      | ~~`Feign`~~ | ~~`Hystrix`~~  | ~~`Zuul`~~ | ~~`Config`~~ | ~~`Bus`~~ |
| `Zookeeper`  | `LoadBalance` | `OpenFeign` | `Resilience4j` | `Zuul2`    | `Nacos`      | `Nacos`   |
| `Consul`     |               |             | `Sentienl`     | `Gateway`  | `Apollo`     |           |
| `Nacos`      |               |             |                |            |              |           |


## 注册中心

### Eureka

#### 服务治理

Spring Cloud封装了Netflix公司开发的Eureka模块来实现服务治理
在传统了rpc远程调用框架中，管理每个服务与服务之间的依赖关系比较复杂，所以需要使用服务治理，可以实现服务调用、负载均衡、容错等，实现服务发现与注册

#### 服务注册与发现
Eureka采用了CS的设计架构，Eureka Server作为服务注册功能的服务器，它是服务注册中心。而系统中的其他微服务，使用Eureka的客户端连接到Eureka Server并维持心跳连接。这样系统的维护人员就可以通过Eureka Server来监控系统中各个微服务是否正常运行。

在服务注册与发现中，有一个注册中心，当服务器启动的时候，会把当前自己服务器的信息比如服务地址通讯地址等以别名方式注册到注册中心上。另一方（消费者|服务提供者）以该别名的方式去注册中心上获取到实际的服务通讯地址，然后再实现本地RPC远程调用框架核心设计思想：在于注册中心，因为使用注册中心管理每个服务与服务之间的依赖关系（服务治理概念）。在任何rpc远程调用框架中，都会有一个注册中心存放服务地址相关信息（接口地址）

#### Server与Client

**Eureka Server（提供注册服务）**
各个微服务节点通过配置后，会在Eureka Server中进行注册，这样Eureka Server的服务注册表中将会存储所有可用服务节点的信息，而且可以在界面中直观看到

**Eureka Client（通过注册中心访问）**
一个Java客户端，用于简化Eureka Server的交互，客户端具备一个内置的、使用轮询（round-robin）负载算法的负载均衡器。在应用启动后，将会向Eureka Server发送心跳（默认周期30秒）。如果Eureka Server在多个心跳周期内没有接收到某个节点的心跳，Eureka Server将会从服务注册表中把这个服务节点移除（默认90秒）

#### 自我保护
默认情况下EurekaClient定时向EurekaServer端发送心跳包，如果Eureka在server端在一定时间内（默认90s）没有收到EurekaClient发送的心跳包，便会直接从服务列表中剔除该服务，但是在短时间（90s）内丢失了大量的服务实例心跳，如果是在网络故障但是服务本身是健康的情况直接删除所有失联的服务就很危险，这时候EurekaServer会开启自我保护机制，不会剔除服务

#### 使用教程

##### 服务端

依赖
```xml
<!-- eureka server -->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
</dependency>
```

YML配置
```yaml
eureka:
  instance:
    hostname: localhost # eureka服务端的实例名称
  client:
    # false表示不向注册中心注册自己
    register-with-eureka: false
    # false表示自己端就是注册中心，我的职责就是维护服务实例，并不需要取检索服务
    fetch-registry: false
    service-url:
      # 设置与Eureka Server交互的地址查询服务和注册服务都需要依赖这个地址
      defaultZone: http://127.0.0.1:7002/eureka/
  server:
    # 关闭自我保护机制，保证不可用服务被即使剔除
    enable-self-preservation: false
    # 过期时间
    eviction-interval-timer-in-ms: 2000

```

启动类注解
```java
@SpringBootApplication
@EnableEurekaServer
public class EurekaMain7001 {
  public static void main(String[] args) { 
      SpringApplication.run(EurekaMain7001.class, args); 
  }
}
```

##### 客户端

依赖
```xml
<!-- eureka client -->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
```

YML配置
```yaml
eureka:
  # 客户端配置
  client:
    # 表示是否将自己注册进Eureka Server，默认为true
    register-with-eureka: true
    # 是否从Eureka Server抓取已有的注册信息，默认为true。单节点无所谓，集群必须设置为true才能配合ribbon使用负载均衡
    fetch-registry: true
    service-url:
      # 集群版
      defaultZone: http://127.0.0.1:7001/eureka/,http://127.0.0.1:7002/eureka/
  instance:
    instance-id: payment8002
    prefer-ip-address: true   # 房屋内路径可以显示IP地址
    # Eureka客户端向服务端发送心跳的时间间隔，单位位秒（默认是30秒）
    lease-renewal-interval-in-seconds: 1
    # Eureka服务端在收到最后一次心跳后等待时间上限，单位位秒（默认是90秒），超时将剔除服务
    lease-expiration-duration-in-seconds: 2
```

启动类注解
```java
@SpringBootApplication
@EnableEurekaClient
public class PaymentMain8002 {
  public static void main(String[] args) { 
      SpringApplication.run(PaymentMain8002.class, args);
  }
}
```


### [Consul](https://developer.hashicorp.com/consul/downloads)

Consul是一套开源的分布式服务发现和配套管理系统，由HashiCorp公司用Go语言开发

提供了微服务系统中的服务治理、配置中心、控制总线等功能，这些功能的每一个都可以根据需要单独使用，也可以一起使用用以构建全方位的服务网格，总之Consul提供了一种完整的服务网格解决方案

它具有很多有点。包括：基于raft协议，比较简洁；支持健康检查，同时支持HTTP和DNS协议，支持跨数据中心的WAN集群，提供图形界面，跨平台，支持Linux、Mac、Windows

#### 使用教程

依赖
```xml
<!-- SpringCloud consul-server -->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-consul-discovery</artifactId>
</dependency>
```

YML配置
```yaml
spring:
  application:
    name: consul-provider-payment
  # consul注册中心地址
  cloud:
    consul:
      host: localhost
      port: 8500
      discovery:
        # 开启心跳
        heartbeat:
          enabled: true
        # hostname: 127.0.0.1
        service-name: ${spring.application.name}
```

启动类注解
```java
@SpringBootApplication
@EnableDiscoveryClient
public class ConsumerMain80 {
    public static void main(String[] args) { 
        SpringApplication.run(ConsumerMain80.class, args); 
    }
}
```


### 三个注册中心异同点

| 组件名       | 语言   | CAP | 服务健康检查 | 对外暴露接口   | SpringCloud集成 |
|-----------|------|-----|--------|----------|---------------|
| Eureka    | Java | AP  | 可配支持   | HTTP     | 已集成           |
| Consul    | GO   | CP  | 支持     | HTTP/DNS | 已集成           |
| Zookeeper | JAva | CP  | 支持     | 客户端      | 已集成           |


### CAP原理

CAP理论关注粒度是数据，而不是整体系统设计的策略

一个分布式系统不可能同时很好地满足一致性，可用性和分区容错性这三个需求

- Consistency （强一致性）
- Availability（可用性）
- Partition tolerance（分区容错性）


## 负载均衡

### Ribbon

简单的说，Ribbon是Netflix发布的开源项目, 主要功能是提供客户端的软件负载衡算法和服务调用

Ribbon客户端组件提供一系列 完善的配置项如连接超时，重试等

简单的说，就是在配置文件中列出Load Balancer (简称LB) 后面所有的机器，Ribbon会自动的帮助你基于某种规则(如简单轮询,随机连接等)去连接这些机器

我们很容易使用Ribbon实现自定义的负载均衡算法

#### Ribbon与Nginx的区别

Nginx是服务器负载均衡，客户端所有请求都会交给nginx, 然后由nginx实现转发请求，即负载均衡是由服务端实现的

Ribbon本地负载均衡，在调用微服务接口时候，会在注册中心上获取注册信息服务列表之后缓存到JVM本地，从而在本地实现RPC远程服务调用技术。

#### 负载均衡策略

com.netflix.loadbalancer.IRule - 根接口
- com.netflix.loadbalancer.RoundRobinRule - 轮询
- com.netflix.loadbalancer.RandomRule - 随机
- com.netflix.loadbalancer.RetryRule - 先轮询，如果失败则在指定时间内重试
- WeightedResponseTimeRule - 对RoundRobinRule的扩展，响应速度越快权重越大
- BestAvailableRule - 过滤掉多次访问故障而处于断路器跳闸状态的服务，然后选择并发量最小的
- AvailabilityFilteringRule - 过滤掉故障实例，然后选择并发较小的
- ZoneAvoidanceRule - 默认规则，复合判断server所在区域的性能和server的可用性选择服务器

#### 负载均衡算法

rest接口第几次请求数 % 服务器集群总数量 = 实际调用服务器位置下标

每次服务重启后rest接口计数从1开始

#### 使用教程

依赖在Eureka里，一般不用主动引入

创建具有负载均衡能力的RestTemplate
```java
@Configuration
public class ApplicationContextConfig {
    /**
     * @LoadBalanced 负载均衡，本机名有下划线的情况下会报错
     * @return
     */
    @Bean
    @LoadBalanced
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }
}
```

使用Rest方式的路由调用
```java
@RestController
public class OrderController {

    public static final String PAYMENT_URL = "http://PAYMENT-SERVICE";

    @Resource
    private RestTemplate restTemplate;

    @GetMapping("/consumer/payment/create")
    public CommonResult<Payment> create(Payment payment) {
        return restTemplate.postForObject(PAYMENT_URL + "/payment/create", payment, CommonResult.class);
    }
}
```

### OpenFeign

Feign是一个声明式WebService客户端。使用Feign能让编写Web Service客户端更加简单

它的使用方法是定义一个服务接口然后在上面添加注解。Feign也支持可拔插式的编码器与解码器。Spring Cloud对Feign进行了封装，使其支持了Spring MVC标准注解和HttpMessageConverters。Feign可以与Eureka和Ribbon组合使用以支持负载均衡

#### 使用教程

依赖
```xml
<!-- openfeign -->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-openfeign</artifactId>
</dependency>
```

YML配置
```yaml
# 设置Feign客户端超时时间（OpenFeign默认支持Ribbon）
ribbon:
  # 指的是建立连接所用的时间，适用于网络状况正常的情况下，两端连接所用的时间
  ReadTimeout: 5000
  # 指的是建立连接后从服务器读取到可用资源的时间
  ConnectTimeout: 5000
```

启动类注解
```java
@SpringBootApplication
@EnableFeignClients
public class OrderFeignMain80 {

    public static void main(String[] args) {
        SpringApplication.run(OrderFeignMain80.class, args);
    }

}
```

服务提供者接口
```java
@Component
@FeignClient(value = "PAYMENT-SERVICE")
public interface PaymentFeignService {

    @GetMapping(value = "/payment/get/{id}")
    CommonResult<Payment> getPaymentById(@PathVariable("id") Long id);

}
```

调用接口
```java
@RestController
public class OrderFeignController {

    @Resource
    private PaymentFeignService paymentFeignService;

    @GetMapping(value = "/consumer/payment/get/{id}")
    public CommonResult<Payment> getPaymentById(@PathVariable("id") Long id) {
        return paymentFeignService.getPaymentById(id);
    }

}

```



## 服务降级

### Hystrix

Hystrix是一个用于处理分布式系统的延迟和容错的开源库，在分布式系统里，许多依赖不可避免的会调用失败，比如超时、异常等，Hystrix能够保证在一个依赖出问题的情况下，不会导致整体服务失败，避免级联故障，以提高分布式系统的弹性

“断路器”本身是一种开关装置，当某个服务单元发生故障之后，通过断路器的故障监控（类似熔断保险丝），向调用方返回一个符合预期的、可处理的备选响应（FallBack），而不是长时间的等待或者抛出调用方无法处理的异常，这样就保证了服务调用方的线程不会被长时间、不必要的占用，从而避免了故障在分布式系统中的蔓延，乃至雪崩

特性：
* 服务降级
* 服务熔断
* 服务限流

#### 服务雪崩

多个微服务之间调用的时候，假设微服务A调用微服务B和微服务C，微服务B和微服务C又调用其他的微服务，这就是所谓的 **“扇出”**

如果扇出的链路上某个微服务的调用响应时间过长或者不可用，对微服务A的调用就会占用越来越多的系统资源，进而引起系统崩溃，所谓的“雪崩效应”

对于高流量的应用来说，单一的后端依赖可能会导致所有服务器上的所有资源都在几秒钟内饱和，比失败更糟糕的是，这些应用程序还可能导致服务之间的延迟增加，备份队列、线程和其他系统资源紧张，进而导致整个系统发生更多的级联故障。这些都表示需要对故障和延迟进行隔离和管理，以便单个依赖关系的失败，不能取消整个应用程序或系统

所以，通常当你发现一个模块下的某个实例失败后，这时候这个模块依然还会接收流量，然后这个有问题的模块还调用了其他的模块，这样就就会发生级联故障，或者叫雪崩

#### 使用教程

依赖
```xml
<!-- hystrix -->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-hystrix</artifactId>
</dependency>
```


