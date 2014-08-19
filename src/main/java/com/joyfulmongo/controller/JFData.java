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
package com.joyfulmongo.controller;

import org.json.JSONObject;

public class JFData extends JSONObject {
    protected JFData() {
        super();
    }

    public JFData(JSONObject payload) {
        super();
        setPayload(payload);
    }

    public void setPayload(JSONObject payload) {
        put(JFCConstants.Props.data.toString(), payload);
    }
}
