### reactor-netty-ext
#### https://github.com/reactor/reactor-netty 的扩展
#### 支持jersey 注解，支持get?p=1 的方式访问，支持静态资源不加配置前缀（如不加: `/static` ）
###  增加对原有路由全桥接，支持静态文件访问
```
 final Path resource = Paths.get(NettyServerTest.class.getResource("/public").toURI());
 HttpServer.create(8080)
         .startAndAwait(JerseyBasedHandler.builder()
                 .withClassPath("com.stoney.reactor.jerysey.router")
                 .addValueProvider(JacksonProvider.class)
                 .addRouter(routes -> {
                     routes.get("/get", (req, resp) -> resp.sendString(Mono.just("asdfasdf")))
                     .directory("/static", resource);
                 }).build()
         );

@Path("/hot")
public class ServiceHot {
    @POST()
    @Path("/post")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public UserTest post(UserTest body) {
        System.out.println("body = " + body);
        return new UserTest("keke", 200, "li");
    }
}



curl -X POST \
  http://localhost:8080/hot/post \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -H 'postman-token: f202fc59-94b9-f0e3-307d-35cb0fe885d6' \
  -d '{"first_name":"li","name":"le","id":200}'



  {
      "name": "keke",
      "id": 200,
      "first_name": "li"
  }
```
##test
```

wrk -H 'Connection: keep-alive' -t12 -c400 -d30s http://localhost:8082/get

Running 30s test @ http://localhost:8082/get
  12 threads and 400 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     6.99ms   31.79ms 892.82ms   99.18%
    Req/Sec     7.29k     0.93k   23.47k    93.04%
  2606144 requests in 30.10s, 248.54MB read
Requests/sec:  86582.83
Transfer/sec:      8.26MB
```