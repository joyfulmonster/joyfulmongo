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

import org.json.JSONObject;

public class OpCmdIncrease extends OpCmd {
    public static final String CMD_INC = "$inc";

    public enum IncProps {
        Increment, amount,
    }

    public OpCmdIncrease(String key, JSONObject json) {
        super(key, json);
    }

    @Override
    public void onCreate(String colname, JSONObject joyObject) {
        onUpdate(colname, joyObject);
    }

    @Override
    public void onUpdate(String colname, JSONObject joyObject) {
        Double amount = mObj.getDouble(IncProps.amount.toString());
        JSONObject field = new JSONObject();
        field.put(key, amount);

        joyObject.remove(key);
        joyObject.put(CMD_INC, field);
    }

    @Override
    public void onQuery(String collectionName, JSONObject joyObject) {
        // TODO Auto-generated method stub

    }
}
