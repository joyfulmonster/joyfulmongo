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

import com.joyfulmongo.db.javadriver.client.JFMongoClient;
import com.joyfulmongo.db.javadriver.client.JFMongoClientFactory;
import com.joyfulmongo.monitor.MonitorManager;
import com.mongodb.*;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MongoQuery {
    private static Logger LOGGER = Logger.getLogger(MongoQuery.class.getName());

    private DBObject projections;
    private String collectionName;
    private DBObject constraints;

    private int limit;
    private int skip;
    private DBObject sortBy = null;

    private String distinctField;

    private MongoQuery(String colname, DBObject constraints, DBObject projection,
                       int limit, int skip, DBObject sort) {
        this.collectionName = colname;
        this.constraints = constraints;
        this.projections = projection;
        this.limit = limit;
        this.skip = skip;
        this.sortBy = sort;
    }

    public MongoQuery(String colname, DBObject constraints, String distinctField) {
        this.collectionName = colname;
        this.constraints = constraints;
        this.distinctField = distinctField;
    }

    public DBObject getConstraints() {
        return this.constraints;
    }

    private DBCollection getDBCollection(String collectionName) {
        JFMongoClient client = JFMongoClientFactory.getInstance();

        DB db = client.getMongoDB();

        DBCollection collection = db.getCollection(collectionName);

        if (collection == null) {
            throw new IllegalArgumentException("Collection " + collectionName
                    + " does not exist.");
        } else {
            return collection;
        }
    }

    public List<String> distinct() {
        DBCollection collection = getDBCollection(collectionName);
        List<String> result = collection.distinct(this.distinctField,
                this.constraints);
        return result;
    }

    public List<MongoObject> find() {
        long start = System.currentTimeMillis();
        DBCollection collection = getDBCollection(collectionName);

        List<MongoObject> result = new ArrayList<MongoObject>(limit);
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.log(Level.FINE, "Find collection=" + collection + "\n"
                    + " constraints " + constraints + "\n" + " projections="
                    + projections + "\n" + " limit=" + limit + "\n" + " skip=" + skip
                    + "\n" + " sortby=" + sortBy + "\n");
        }

        DBCursor cursor = collection.
                find(constraints, projections).
                sort(sortBy).
                skip(skip).
                limit(limit + skip);

        while (cursor.hasNext()) {
            result.add(new MongoObject(collectionName, cursor.next()));
        }

        MonitorManager.getInstance().logQuery(collectionName, MongoDBUtil.toJSONObject(constraints), MongoDBUtil.toJSONObject(sortBy),
                System.currentTimeMillis() - start);

        return result;
    }

    public static class Builder {
        private String colname;
        private DBObject constraint;
        private DBObject projection;
        private int limit;
        private int skip;
        private String distinctField;
        private DBObject sort;

        public Builder(String colname) {
            this.colname = colname;
            this.limit = 100;
            this.skip = 0;
            this.sort = null;
            this.projection = new BasicDBObject();
            this.constraint = new BasicDBObject();
            this.sort = new BasicDBObject();
            this.distinctField = null;
        }

        public void collection(String colname2) {
            this.colname = colname2;
        }

        public Builder projection(String... fields) {
            if (projection == null) {
                projection = new BasicDBObject();
            }
            for (String field : fields) {
                projection.put(field, 1);
            }
            return this;
        }

        public Builder distinct(String distinctField) {
            this.distinctField = distinctField;
            return this;
        }

        public Builder projectionExclude(String... fields) {
            if (projection == null) {
                projection = new BasicDBObject();
            }

            for (String field : fields) {
                projection.put(field, 0);
            }
            return this;
        }

        public Builder constraints(JSONObject constraint) {
            this.constraint = MongoDBUtil.toDBObject(constraint);
            return this;
        }

        public Builder limit(int limit) {
            this.limit = limit;
            return this;
        }

        public Builder skip(int skip) {
            this.skip = skip;
            return this;
        }

        public Builder sort(String sorts) {
            StringTokenizer st = new StringTokenizer(sorts, ",");
            while (st.hasMoreTokens()) {
                String sortCol = st.nextToken();
                if (sortCol.startsWith("-")) {
                    this.sort.put(sortCol.substring(1), -1);
                } else {
                    this.sort.put(sortCol, 1);
                }
            }
            return this;
        }

        public MongoQuery build() {
            MongoQuery result;
            if (this.distinctField != null) {
                result = new MongoQuery(colname, constraint, distinctField);
            } else {
                result = new MongoQuery(colname, constraint, projection, limit, skip, sort);
            }

            return result;
        }
    }
}
