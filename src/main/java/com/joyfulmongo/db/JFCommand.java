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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class JFCommand {
    /**
     * prepare
     */
    protected abstract void beforeExecute();

    /**
     * execute
     *
     * @return
     */
    protected abstract JFMongoCmdResult execute();

    /**
     * post execute
     *
     * @return
     */
    protected abstract JFMongoCmdResult afterExecute(
            JFMongoCmdResult executeResult);

    /**
     * @return
     */
    public JFMongoCmdResult invoke() {
        beforeExecute();
        JFMongoCmdResult executeResult = execute();
        JFMongoCmdResult result = afterExecute(executeResult);
        return result;
    }

    protected List<ContainerObject> findChildObject(JSONObject parentObj) {
        List<ContainerObject> childs = new ArrayList<ContainerObject>(0);
        Iterator<String> keys = parentObj.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            Object obj = parentObj.get(key);
            if (obj instanceof JSONObject) {
                ContainerObject typedObj = ContainerObjectFactory.getChildObject(key,
                        (JSONObject) obj);
                if (typedObj != null) {
                    childs.add(typedObj);
                }
            }
        }

        return childs;
    }
}
