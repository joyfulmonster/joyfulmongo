package com.joyfulmongo.db;

import org.json.JSONObject;

public class OpCmdSearch extends OpCmd {
    public static final String CMD_INC = "$text";

    public OpCmdSearch(String key, JSONObject json) {
        super(key, json);
    }

    @Override
    public void onCreate(String colname, JSONObject joyObject) {
    }

    @Override
    public void onUpdate(String colname, JSONObject joyObject) {
    }

    @Override
    public void onQuery(String collectionName, JSONObject joyObject) {

    }
}
