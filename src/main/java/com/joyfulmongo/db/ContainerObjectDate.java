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

import java.text.ParseException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ContainerObjectDate extends JSONObjectSupport implements
        ContainerObject {
    private static Logger LOGGER = Logger.getLogger(ContainerObjectDate.class.getName());
    private String key;

    public ContainerObjectDate(String key, JSONObject json) {
        super(json);
        this.key = key;
    }

    @Override
    public void onCreate(String collectionName, JSONObject joyObject) {
    }

    @Override
    public void onUpdate(String collectionName, JSONObject joyObject) {
    }

    @Override
    public void onQuery(String collectionName, JSONObject theChild) {
        JSONObject childJson = theChild.getJSONObject(key);

        String iso = childJson.getString("iso");

        try {
            Date date = Utils.getParseDateFormat().parse(iso);
            theChild.put(key, date);
        } catch (ParseException e) {
            // do nothing
            LOGGER.log(Level.WARNING, "Wrong format of date string, skip convert.");
        }

    }
}
