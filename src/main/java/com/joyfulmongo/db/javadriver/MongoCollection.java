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

package com.joyfulmongo.db.javadriver;

import com.joyfulmongo.db.Constants;
import com.joyfulmongo.db.ContainerObjectGeoPoint;
import com.mongodb.*;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MongoCollection {
    protected static Logger LOGGER = Logger.getLogger(MongoCollection.class
            .getName());

    private String colName;
    private DBCollection dbCollection;

    MongoCollection(String name, DBCollection collection) {
        this.colName = name;
        this.dbCollection = collection;
    }

    public void ensureIndex(String key, boolean ascend) {
    }

    public void create(JSONObject creates) {
        MongoObject newDBObj = new MongoObject(colName, creates);

        DBObject dbObject = newDBObj.getDBObject();

        dbCollection.ensureIndex(Constants.Props.objectId.toString());
        ensure2DIndexIfExist(creates);

        WriteResult result = dbCollection.insert(dbObject, WriteConcern.SAFE);

        recordWriteResult("create", result);
    }

    public void update(JSONObject query, JSONObject updates) {
        JSONObject extraInstruction = null;

        List<String> modifiers = findModifiers(updates);
        for (String modifier : modifiers) {
            if (extraInstruction == null) {
                extraInstruction = new JSONObject();
            }
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine("UPDATE modifier=" + modifier + " " + extraInstruction);
            }
            Object o = updates.get(modifier);
            extraInstruction.put(modifier, o);
        }

        for (String modifier : modifiers) {
            updates.remove(modifier);
        }

        this.ensure2DIndexIfExist(updates);

        MongoObject queryObj = new MongoObject(colName, query);
        DBObject queryDBObject = queryObj.getDBObject();

        WriteResult result = null;
        {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine("UPDATE =" + " " + updates);
            }
            JSONObject updateInstruction = new JSONObject();
            updateInstruction.put("$set", updates);
            MongoObject jfObj = new MongoObject(colName, updateInstruction);
            DBObject updateDBObject = jfObj.getDBObject();

            result = dbCollection.update(queryDBObject, updateDBObject, false, false, WriteConcern.SAFE);
        }

        if (extraInstruction != null) {
            MongoObject jfObj = new MongoObject(colName, extraInstruction);
            DBObject updateInstructionObject = jfObj.getDBObject();
            result = dbCollection.update(queryDBObject, updateInstructionObject,
                    false, false, WriteConcern.SAFE);
        }

        recordWriteResult("update", result);
    }

    public void upsert(JSONObject query, JSONObject updates) {
        MongoObject queryObj = new MongoObject(colName, query);
        DBObject queryDBObject = queryObj.getDBObject();

        MongoObject jfObj = new MongoObject(colName, updates);
        DBObject updateDBObject = jfObj.getDBObject();

        DBObject oldValue = dbCollection.findAndModify(queryDBObject, null, null,
                false, updateDBObject, false, true);
    }

    private List<String> findModifiers(JSONObject updateDelta) {
        List<String> modifiers = new ArrayList<String>(0);
        Iterator<String> keys = updateDelta.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            if (key.startsWith("$")) {
                modifiers.add(key);
            }
        }

        return modifiers;
    }

    public void delete(JSONObject obj) {
        String objectId = (String) obj.get(Constants.Props.objectId.toString());
        if (objectId == null || objectId.length() == 0) {
            throw new IllegalArgumentException("ObjectId can not be null or empty.");
        } else {
            DBObject queryCondition = new BasicDBObject();

            queryCondition.put(Constants.Props.objectId.toString(), objectId);

            WriteResult result = dbCollection.remove(queryCondition,
                    WriteConcern.SAFE);

            recordWriteResult("delete", result);
        }
    }

    private void recordWriteResult(String msg, WriteResult result) {
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.log(Level.FINE, msg + " Write result is " + result);
        }
    }

    private void ensure2DIndexIfExist(JSONObject payload) {
        Object geoPoint = payload.opt(ContainerObjectGeoPoint.S_GEO_POINT);
        if (geoPoint != null) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine("create EnsureIndex " + ContainerObjectGeoPoint.S_GEO_POINT);
            }
            DBObject indexObj = new BasicDBObject();
            indexObj.put(ContainerObjectGeoPoint.S_GEO_POINT, "2d");
            dbCollection.ensureIndex(indexObj);
        }
    }
}
