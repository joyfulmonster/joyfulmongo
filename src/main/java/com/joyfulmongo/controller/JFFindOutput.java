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

import com.joyfulmongo.db.JFMongoObject;

import java.util.List;

public class JFFindOutput extends JFOutput {
    public JFFindOutput(List<JFMongoObject> objs) {
        JFResults.JFResultsBuilder resultsBuilder = new JFResults.JFResultsBuilder();
        for (JFMongoObject obj : objs) {
            resultsBuilder.addData(obj.toJson());
        }

        JFResults results = resultsBuilder.build();
        JFResult result = new JFResult.JFResultBuilder().results(results).build();
        setResult(result);
    }

    public JFFindOutput(JFUserError error) {
        setResult(new JFResult.JFResultBuilder().errorCode(error.getCode())
                .errorMsg(error.getMessage()).build());
    }
}
