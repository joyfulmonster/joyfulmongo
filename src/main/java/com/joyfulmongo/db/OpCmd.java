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
package com.joyfulmongo.db;

import com.joyfulmongo.controller.JSONObjectSupport;
import org.json.JSONObject;

abstract class OpCmd extends JSONObjectSupport implements ContainerObject {
    protected String key;

    OpCmd(String key, JSONObject json) {
        super(json);
        this.key = key;
    }

    public String getKey() {
        return this.key;
    }

    public Object onCreateOutput() {
        return null;
    }

    public Object onUpdateOutput() {
        return null;
    }
}
