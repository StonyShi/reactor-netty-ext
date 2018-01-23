package com.stoney.reactor.jerysey.router;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * <p>reactorDome
 * <p>com.stony.reactor.router
 *
 * @author stony
 * @version 下午6:56
 * @since 2018/1/18
 */
@Path("/hot")
public class ServiceHot {


    @GET
    public String get(){
        return "hot";
    }


    @POST()
    @Path("/post")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public UserTest post(UserTest body) {
        System.out.println("body = " + body);
        return new UserTest("keke", 200, "li");
    }
}
