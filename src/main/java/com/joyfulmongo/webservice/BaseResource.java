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
package com.joyfulmongo.webservice;

import com.joyfulmongo.controller.JFResult;

import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;

public class BaseResource {
    protected Response getResponse(JFResult result) {
        GenericEntity<String> entity = new GenericEntity<String>(result + "") {
        };
        Response res = Response.ok().entity(entity).build();
        return res;
    }

    protected Response getResponse(String result) {
        GenericEntity<String> entity = new GenericEntity<String>(result) {
        };
        Response res = Response.ok().entity(entity).build();
        return res;
    }
}
