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

import com.joyfulmongo.db.JFMongoCmdDelete;
import com.joyfulmongo.db.JFMongoCmdResult;
import org.json.JSONObject;

public class JFDeleteController extends
        JFController<JFDeleteInput, JFDeleteOutput> {
    @Override
    public JFDeleteOutput process(JFDeleteInput input) {
        String collection = input.getClassname();

        JSONObject payload = input.getData();

        JFMongoCmdDelete cmd = new JFMongoCmdDelete(collection, payload);

        JFMongoCmdResult cmdResult = cmd.invoke();

        Boolean res = (cmdResult != null);

        JFDeleteOutput result = new JFDeleteOutput(res);

        return result;
    }

}
