/*
 * Copyright 2014 Weifeng Bao
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
*/
package com.joyfulmonster.webservice;

import com.joyfulmongo.webservice.PostProgramResources;
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
        return new ResourceConfig(PostProgramResources.class);
    }

    @Test
    public void testHelloWorld() {
        WebTarget webTarget = this.target();
        JSONObject json = new JSONObject();
        json.put("classname", "test");
        JSONObject data = new JSONObject();
        data.put ("name1", "val1");
        json.put("data", data);
        webTarget.path("/2/create").request(MediaType.APPLICATION_JSON_TYPE).
                post(Entity.entity(json.toString(), MediaType.APPLICATION_JSON), String.class);
    }
}

