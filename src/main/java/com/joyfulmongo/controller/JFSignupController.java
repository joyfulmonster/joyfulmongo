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
import com.joyfulmongo.db.JFMongoCmdSignup;
import com.joyfulmongo.db.JFMongoUser;
import org.json.JSONObject;

public class JFSignupController extends
        JFController<JFSignupInput, JFSignupOutput> {
    @Override
    public JFSignupOutput process(JFSignupInput input) {
        JSONObject payload = input.getData();

        String username = input.getUsername();
        String password = input.getPassword();

        JFMongoCmdSignup cmd = new JFMongoCmdSignup(payload, username, password);

        JFMongoCmdResult commandResult = cmd.invoke();

        String sessionToken = JFMongoUser.generateSessionToken();

        JFSignupOutput result = new JFSignupOutput(sessionToken, commandResult);

        return result;
    }

}
