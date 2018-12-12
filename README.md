### reactor-netty-ext
#### 1. [reactor-netty](https://github.com/reactor/reactor-netty)  jersey1.x的扩展
#### 2. 扩展get方法支持get?p=1 的方式访问
#### 3. 支持静态资源不加配置前缀（如不加: `/res` ）
#### 4. 增加对原有路由全桥接，支持静态文件访问
### 代码示例：
```
 final Path resource = Paths.get(NettyServerTest.class.getResource("/public").toURI());
 HttpServer.create(8080)
         .startAndAwait(JerseyBasedHandler.builder()
                 .withClassPath("com.stoney.reactor.jerysey.router")
                 .addValueProvider(JacksonProvider.class)
                 .addRouter(routes -> {
                     routes.get("/get", (req, resp) -> resp.sendString(Mono.just("asdfasdf")))
                     .directory("/res", resource);
                 }).build()
         );

@Path("/api")
public class ServiceHot {
    @POST()
    @Path("/post")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public UserTest post(UserTest body) {
        System.out.println("body = " + body);
        return new UserTest("bai", 200, "li");
    }
    @GET()
    @Path("/get")
    @Produces(MediaType.APPLICATION_JSON)
    public UserTest get() {
        return new UserTest("bai", 199, "li");
    }
}



curl -X POST \
  http://localhost:8080/hot/post \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -H 'postman-token: f202fc59-94b9-f0e3-307d-35cb0fe885d6' \
  -d '{"first_name":"li","name":"jiu","id":200}'



  {
      "name": "bai",
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

wrk -H 'Connection: keep-alive' -t20 -c400 -d30s http://localhost:8082/api/get?id=3887099059629981696
Running 30s test @ http://localhost:8082/api/get?id=3887099059629981696
  20 threads and 400 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    54.04ms    9.31ms 142.88ms   71.88%
    Req/Sec   370.86     53.38   676.00     71.84%
  221881 requests in 30.08s, 107.92MB read
  Socket errors: connect 0, read 221881, write 0, timeout 0
Requests/sec:   7376.17
Transfer/sec:      3.59MB
```