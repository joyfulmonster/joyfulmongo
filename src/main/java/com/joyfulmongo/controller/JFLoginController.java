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

import com.joyfulmongo.db.JFMongoCmdLogin;
import com.joyfulmongo.db.JFMongoCmdResult;
import com.joyfulmongo.db.JFMongoObject;
import com.joyfulmongo.db.JFMongoUser;

public class JFLoginController extends
        JFController<JFLoginInput, JFLoginOutput> {
    @Override
    public JFLoginOutput process(JFLoginInput input) {
        String username = input.getUsername();
        String password = input.getPassword();

        JFMongoCmdLogin cmd = new JFMongoCmdLogin(username, password);

        JFMongoCmdResult commandResult = cmd.invoke();

        JFLoginOutput result;
        if (commandResult == null) {
            result = new JFLoginOutput();
        } else {
            String sessionToken = JFMongoUser.generateSessionToken();
            JFMongoObject obj = (JFMongoObject) commandResult
                    .get(JFCConstants.Props.data.toString());
            result = new JFLoginOutput(sessionToken, obj);
        }

        return result;
    }

}
