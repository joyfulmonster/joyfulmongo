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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JFMultiInput extends JFDataArrayInput {
    private static Logger LOGGER = Logger.getLogger(JFMultiInput.class.getName());

    public JFMultiInput(String jsonStr) {
        super(jsonStr);
    }

    public List<JFCommandInput> getCommands() {
        List<JFCommandInput> commands = new ArrayList<JFCommandInput>();
        JSONArray array = getJSONArray(JFCConstants.Props.commands.toString());
        for (int i = 0; i < array.length(); i++) {
            JSONObject json = array.getJSONObject(i);
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine("  Command multi-input " + json);
            }
            JFCommandInput command = new JFCommandInput(json);
            commands.add(command);
        }

        return commands;
    }
}
