### reactor-netty-ext
#### 支持jersey
```
 HttpServer.create(8080)
                .startAndAwait(JerseyBasedHandler.builder()
                        .withClassPath("com.stoney.reactor.jerysey.router")
                        .addValueProvider(JacksonProvider.class)
                        .build());

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