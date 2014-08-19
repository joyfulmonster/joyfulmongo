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

import org.json.JSONArray;
import org.json.JSONObject;

public class OpCmdAdd extends OpCmd {
    public static String CMD_ADD_TO_SET = "$addToSet";
    public static String CMD_EACH = "$each";

    public enum AddProps {
        objects,
    }

    public OpCmdAdd(String key, JSONObject json) {
        super(key, json);
    }

    @Override
    public void onCreate(String colname, JSONObject joyObject) {
        JSONArray a = mObj.optJSONArray(AddProps.objects.toString());
        joyObject.put(key, a);
    }

    @Override
    public Object onCreateOutput() {
        JSONArray a = mObj.optJSONArray(AddProps.objects.toString());
        return a;
    }

    @Override
    public void onUpdate(String colname, JSONObject joyObject) {
        JSONArray a = mObj.optJSONArray(AddProps.objects.toString());
        JSONObject each = new JSONObject();
        each.put(CMD_EACH, a);

        JSONObject addToSet = joyObject.optJSONObject(CMD_ADD_TO_SET);
        if (addToSet == null) {
            JSONObject field = new JSONObject();
            field.put(key, each);

            joyObject.remove(key);
            joyObject.put(CMD_ADD_TO_SET, field);
        } else {
            joyObject.remove(key);
            addToSet.put(key, each);
        }
    }

    @Override
    public Object onUpdateOutput() {
        JSONArray a = mObj.optJSONArray(AddProps.objects.toString());
        return a;
    }

    @Override
    public void onQuery(String collectionName, JSONObject joyObject) {
        // TODO Auto-generated method stub

    }

}
