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

public class JFResult extends JSONObject {
    /**
     * Result for operation
     *
     * @param json
     */
    private JFResult(JSONObject json) {
        super();
        if (json == null) {
            put(JFCConstants.Props.result.toString(), JSONObject.NULL);
        } else {
            put(JFCConstants.Props.result.toString(), json);
        }
    }

    /**
     * Result for query
     *
     * @param array
     */
    private JFResult(JSONArray array) {
        super();
        put(JFCConstants.Props.result.toString(), array);
    }

    /**
     * result for delete
     *
     * @param json
     */
    private JFResult(Boolean json) {
        super();
        put(JFCConstants.Props.result.toString(), json);
    }

    public JFResult(Integer errorCode, String errorMsg) {
        super();
        put(JFCConstants.Props.code.toString(), errorCode);
        put(JFCConstants.Props.error.toString(), errorMsg);
    }

    public JSONObject getData() {
        JSONObject data = null;
        JSONObject result = getJSONObject(JFCConstants.Props.result.toString());
        if (result != null) {
            data = result.getJSONObject(JFCConstants.Props.data.toString());
        }

        return data;
    }

    public static class JFResultArrayBuilder {
        private JSONArray marray;

        public JFResultArrayBuilder() {
            marray = new JSONArray();
        }

        public JFResultArrayBuilder data(JSONObject data) {
            JSONObject json = new JSONObject();
            json.put(JFCConstants.Props.data.toString(), data);
            marray.put(json);
            return this;
        }

        public JFResult build() {
            return new JFResult(marray);
        }
    }

    public static class JFResultBuilder {
        private JSONObject mobj;
        private Boolean success;
        private Integer errorCode = null;
        private String errorMsg = null;
        private Boolean isNull = false;

        public JFResultBuilder() {
            mobj = new JSONObject();
        }

        public JFResultBuilder nullResult() {
            isNull = true;
            return this;
        }

        public JFResultBuilder data(JSONObject data) {
            mobj.put(JFCConstants.Props.data.toString(), data);
            return this;
        }

        public JFResultBuilder sessionToken(String str) {
            mobj.put(JFCConstants.Props.session_token.toString(), str);
            return this;
        }

        public JFResultBuilder results(JFResults res) {
            mobj.put(JFCConstants.Props.results.toString(), res);
            return this;
        }

        public JFResultBuilder booleanResult(Boolean result) {
            this.success = true;
            return this;
        }

        public JFResultBuilder errorCode(int code) {
            this.errorCode = code;
            return this;
        }

        public JFResultBuilder errorMsg(String message) {
            this.errorMsg = message;
            return this;
        }

        public JFResult build() {
            if (isNull) {
                return new JFResult((JSONObject) null);
            } else if (success != null) {
                return new JFResult(success);
            } else if (errorCode != null) {
                return new JFResult(errorCode, errorMsg);
            } else {
                return new JFResult(mobj);
            }
        }
    }
}
