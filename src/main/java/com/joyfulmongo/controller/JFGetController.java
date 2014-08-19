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

import com.joyfulmongo.db.JFMongoCmdQuery;
import com.joyfulmongo.db.JFMongoObject;
import org.json.JSONObject;

import java.util.List;

public class JFGetController extends JFController<JFGetInput, JFGetOutput> {
    @Override
    public JFGetOutput process(JFGetInput input) {
        String collection = input.getClassname();

        JSONObject getCondition = input.getData();

        JFMongoCmdQuery query = new JFMongoCmdQuery.Builder(collection)
                .constraints(getCondition).build();

        List<JFMongoObject> results = query.find();
        JFMongoObject result = null;
        if (results.size() > 0) {
            result = results.get(0);
        }

        if (result == null) {
            throw new JFUserError(101, "object not found for get");
        } else {
            JFGetOutput output = new JFGetOutput(result);
            return output;
        }
    }
}
