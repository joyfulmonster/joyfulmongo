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

import com.joyfulmongo.monitor.MonitorManager;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("/console")
public class MonitorResources extends BaseResource {
    @Path("query")
    @POST
    @Consumes(Constants.JF_JSON)
    @Produces("text/plain")
    public Response queryLog(String params) {
        String result = null;
        try {
            result = MonitorManager.getInstance().diagnosticQueries();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getResponse(result);
    }
}  
