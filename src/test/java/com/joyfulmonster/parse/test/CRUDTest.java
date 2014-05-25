package com.joyfulmonster.parse.test;

import com.joyfulmongo.webservice.ParseProgramResources;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.grizzly.GrizzlyTestContainerFactory;
import org.glassfish.jersey.test.spi.TestContainerFactory;
import org.json.JSONObject;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;

public class CRUDTest extends JerseyTest {
    public CRUDTest() throws Exception {
        super();
    }

    @Override
    protected TestContainerFactory getTestContainerFactory() {
        return new GrizzlyTestContainerFactory();
    }

    @Override
    protected Application configure() {
        return new ResourceConfig(ParseProgramResources.class);
    }

    @Test
    public void testHelloWorld() {
        WebTarget webTarget = this.target();
        JSONObject json = new JSONObject();
        json.put("classname", "test");
        JSONObject data = new JSONObject();
        data.put ("name1", "val1");
        json.put("data", data);
        webTarget.path("/program/create").request(MediaType.APPLICATION_JSON_TYPE).
                post(Entity.entity(json.toString(), MediaType.APPLICATION_JSON), String.class);
    }
}

