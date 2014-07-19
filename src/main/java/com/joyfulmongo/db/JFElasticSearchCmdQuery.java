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
 */

package com.joyfulmongo.db;

import com.joyfulmongo.controller.JFCConstants;
import com.joyfulmongo.db.elasticsearch.ESSearchQuery;
import com.joyfulmongo.db.javadriver.MongoObject;
import com.joyfulmongo.db.javadriver.MongoQuery;
import com.joyfulmongo.util.JsonUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;
import java.util.logging.Logger;

/**
 * Created by wbao on 6/25/2014.
 */
public class JFElasticSearchCmdQuery extends JFCommand{
    private static Logger LOGGER = Logger.getLogger(JFMongoCmdQuery.class.getName());

    public static Integer S_DEFAULT_SKIP = 0;
    public static Integer S_DEFAULT_LIMIT = 100;

    private ESSearchQuery query;
    private String collectionName;
    private String[] includeFields;
    private String redirectClassname;

    private JFElasticSearchCmdQuery(String colname, ESSearchQuery dbquery,
                                    String redirectClassname)
    {
        this.collectionName = colname;
        this.query = dbquery;
        this.redirectClassname = redirectClassname;
    }

    @Override
    protected void beforeExecute()
    {
    }

    @Override
    protected JFMongoCmdResult execute()
    {
        List<JSONObject> parseObjs = query.find();
        JFMongoCmdResult result = new JFMongoCmdResult();
        result.put(JFCConstants.Props.results.toString(), parseObjs);
        if (this.redirectClassname != null)
        {
            result.put(JFCConstants.Props.classname.toString(), redirectClassname);
        }
        return result;
    }

    public List<JFMongoObject> find()
    {
        List<JFMongoObject> objs = new ArrayList<JFMongoObject>();
        List<JSONObject> jobjs = query.find();
        for (JSONObject jobj : jobjs){
            objs.add(new JFMongoObject(collectionName, jobj));
        }
        return objs;
    }

    @Override
    protected JFMongoCmdResult afterExecute(JFMongoCmdResult executeResult)
    {
        return executeResult;
    }

    private void setIncludeFields(String... includeFields)
    {
        this.includeFields = includeFields;
    }

    public static class Builder {
        private String collectionName;
        private ESSearchQuery.Builder esQuery;

        private JSONObject constraints;
        private List<String> includes;
        private List<String> sorts;
        private int limit;
        private int skip;

        public Builder(String collectionName) {
            if (collectionName == null || collectionName.length() == 0) {
                throw new IllegalArgumentException(
                        "Collection name can not be null or zero length");
            }

            esQuery = new ESSearchQuery.Builder();
            esQuery.applicationName("kcpdb2");
            esQuery.collectionName(collectionName);

            this.collectionName = collectionName;
            this.sorts = new ArrayList<String>(0);
            this.includes = new ArrayList<String>(0);
            this.limit = S_DEFAULT_LIMIT;
            this.skip = S_DEFAULT_SKIP;
        }

        public void collection(String colname) {
            this.esQuery.collectionName(colname);
            this.collectionName = colname;
        }

        public Builder must(String field, String... vals){
            esQuery.must(field, vals);
            return this;
        }

        public Builder should(String field, String... vals){
            esQuery.should(field, vals);
            return this;
        }

        public String getCollection() {
            return this.collectionName;
        }

        public Builder include(String... fields) {
            for (String field : fields) {
                includes.add(field);
            }
            return this;
        }

        String[] getIncludes() {
            String[] results = new String[includes.size()];
            results = includes.toArray(results);
            return results;
        }

        public Builder limit(int limit) {
            this.limit = limit;
            return this;
        }

        public Builder skip(int skip) {
            this.skip = skip;
            return this;
        }

        int getSkip() {
            return this.skip;
        }

        public Builder sort(String sorts) {
            StringTokenizer st = new StringTokenizer(sorts, ",");
            while (st.hasMoreTokens()) {
                this.sorts.add(st.nextToken());
            }
            return this;
        }

        String getSort() {
            String result = "";
            if (sorts.size() > 0) {
                result = sorts.get(0);
            }

            for (int i = 1; i < sorts.size(); i++) {
                result += "," + sorts.get(i);
            }
            return result;
        }

        public Builder constraints(JSONObject constraints) {
            this.constraints = constraints;
            if (constraints.has(JFCConstants.Props.query_must.toString())) {
              JSONObject json = constraints.getJSONObject(JFCConstants.Props.query_must.toString());
              Iterator<String> keys = json.keys();
              while (keys.hasNext()) {
                String key = keys.next();
                JSONArray array = json.getJSONArray(key);
                esQuery.must(key, JsonUtil.toArray(array));                
              }
            }
            
            if (constraints.has(JFCConstants.Props.query_should.toString())) {
              JSONObject json = constraints.getJSONObject(JFCConstants.Props.query_should.toString());
              Iterator<String> keys = json.keys();
              while (keys.hasNext()) {
                String key = keys.next();
                JSONArray array = json.getJSONArray(key);
                esQuery.should(key, JsonUtil.toArray(array));                
              }
            }            
            return this;
        }

        public Builder whereEquals(String key, String value) {
            this.constraints.put(key, value);
            return this;
        }

        JSONObject getConstraints() {
            return this.constraints;
        }

        public JFElasticSearchCmdQuery build() {
            ESSearchQuery dbquery = esQuery.build();
            System.out.println ("The ESQuery " + dbquery);
            JFElasticSearchCmdQuery query = new JFElasticSearchCmdQuery(collectionName, dbquery, null);
            return query;
        }
    }
}
