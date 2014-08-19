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

import com.joyfulmongo.db.JFMongoCmdResult;
import com.joyfulmongo.db.JFMongoCmdUpdate;
import org.json.JSONObject;

public class JFUpdateController extends
        JFController<JFUpdateInput, JFUpdateOutput> {
    @Override
    public JFUpdateOutput process(JFUpdateInput input) {
        String collection = input.getClassname();

        JSONObject payload = input.getData();

        JFMongoCmdUpdate dbObj = new JFMongoCmdUpdate(collection, payload);

        JFMongoCmdResult commandResult = dbObj.invoke();

        JFUpdateOutput result = new JFUpdateOutput(commandResult);

        return result;
    }
}
