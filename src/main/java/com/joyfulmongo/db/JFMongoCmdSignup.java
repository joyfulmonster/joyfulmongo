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

import com.joyfulmongo.controller.JFUserError;
import com.mongodb.util.Util;
import org.json.JSONObject;

import java.util.List;

public class JFMongoCmdSignup extends JFMongoCmdCreate {
    private String username;
    private String password;

    public JFMongoCmdSignup(JSONObject json, String username, String password) {
        super(JFMongoUser.S_USER_COLLNAME, json);
        this.username = username;
        this.password = password;
    }

    @Override
    protected void beforeExecute() {
        super.beforeExecute();
    }

    @Override
    protected JFMongoCmdResult execute() {
        JFMongoCmdResult result;
        boolean usernameOkay = verifyUsernameUnique(username);
        if (usernameOkay) {
            System.out.println("The password is =" + password);
            String md5Password = Util.hexMD5(password.getBytes());
            getPayload().put(Constants.Props.password.toString(), md5Password);
            result = super.execute();
        } else {
            throw new JFUserError(202, "username " + username + " already taken");
        }

        return result;
    }

    @Override
    protected JFMongoCmdResult afterExecute(JFMongoCmdResult executeResult) {
        return super.afterExecute(executeResult);
    }

    protected boolean verifyUsernameUnique(String username) {
        JFMongoCmdQuery query = new JFMongoCmdQuery.Builder(
                JFMongoUser.S_USER_COLLNAME)
                .projection(Constants.Props.objectId.toString())
                .whereEquals(Constants.Props.username.toString(), username).build();
        List<JFMongoObject> results = query.find();
        return results.size() == 0;
    }
}
