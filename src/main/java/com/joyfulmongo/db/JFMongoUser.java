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

import org.apache.commons.lang.RandomStringUtils;
import org.json.JSONObject;

public class JFMongoUser extends JFMongoObject {
    public static final String S_USER_COLLNAME = "_User";

    public JFMongoUser() {
        super(S_USER_COLLNAME);
    }

    public JFMongoUser(JSONObject json) {
        super(S_USER_COLLNAME, json);
    }

    public static JFMongoCmdQuery.Builder getQuery() {
        JFMongoCmdQuery.Builder builder = new JFMongoCmdQuery.Builder(
                S_USER_COLLNAME).projectionExclude(Constants.Props.password.toString());
        return builder;
    }

    public static String generateSessionToken() {
        String sessionToken = RandomStringUtils.randomAlphanumeric(25);
        return sessionToken;
    }
}
