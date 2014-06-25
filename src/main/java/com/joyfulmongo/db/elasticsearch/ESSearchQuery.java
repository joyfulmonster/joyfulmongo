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

package com.joyfulmongo.db.elasticsearch;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.json.JSONObject;

import java.util.*;
import java.util.logging.Logger;

/**
 * Created by wbao on 6/22/2014.
 */
public class ESSearchQuery {
    private static Logger LOGGER = Logger.getLogger(ESSearchQuery.class.getName());

    private SearchRequestBuilder search;

    private ESSearchQuery( )
    {
    }

    public List<JSONObject> find()
    {
        SearchResponse response = search.execute().actionGet();

        SearchHits hits = response.getHits();
        for (SearchHit hit : hits.getHits()) {
            String str = hit.getSourceAsString();
            System.out.println ( "hit " + str);
        }

        return null;
    }

    public static class Builder
    {
        private String applicationName;
        private String collectionName;
        private String[] fields = new String[0];
        private JSONObject projection;
        private int limit;
        private int skip;
        private JSONObject sort;
        private Map<String, List<String>> musts;
        private Map<String, List<String>> shoulds;

        private static Client client;
        static {
            String esClusterName = System.getProperty("es.cluster.name");
            String esNodeHost = System.getProperty("es.node.hostname", "192.168.127.139");
            String esNodePort = System.getProperty("es.node.port", "9300");

            Settings settings = ImmutableSettings.settingsBuilder().build(); //put("cluster.name", "kcpes").build();
            client =  new TransportClient(settings).addTransportAddress(new InetSocketTransportAddress(esNodeHost, Integer.parseInt(esNodePort)));
        }

        public Builder()
        {
            this.limit = 100;
            this.skip = 0;
            this.sort = null;
            this.musts = new HashMap<String, List<String>>(); // ArrayList<String>();
            this.shoulds = new HashMap<String, List<String>>(); //ArrayList<String>();
            this.projection = new JSONObject();
            this.sort = new JSONObject();
        }

        public Builder applicationName(String applicationName)
        {
            this.applicationName = applicationName;
            return this;
        }

        public Builder collectionName(String collectionName)
        {
            this.collectionName = collectionName;
            return this;
        }

        public Builder fields(String... fields)
        {
            this.fields = fields;
            return this;
        }

        public Builder must(String field, String match)
        {
            List<String> matches = this.musts.get(field);
            if (matches == null) {
                matches = new ArrayList<String>();
                this.musts.put(field, matches);
            }
            matches.add(match);
            return this;
        }

        public Builder should (String field, String match)
        {
            List<String> matches = this.shoulds.get(field);
            if (matches == null) {
                matches = new ArrayList<String>();
                this.shoulds.put(field, matches);
            }
            matches.add(match);
            return this;
        }

        public Builder limit(int limit)
        {
            this.limit = limit;
            return this;
        }

        public Builder skip(int skip)
        {
            this.skip = skip;
            return this;
        }

        public Builder sort(String sorts)
        {
            return this;
        }

        public ESSearchQuery build()
        {
            QueryBuilder query = null;
            Iterator<String> mustkeys = musts.keySet().iterator();
            while(mustkeys.hasNext()) {
                String mustKey = mustkeys.next();
                List<String> mustVal = musts.get(mustKey);
                String pattern = "";
                if (mustVal.size() > 0) {
                    pattern = mustVal.get(0);
                }
                for (int i=1; i<mustVal.size(); i++) {
                    pattern += " AND " + mustVal.get(0);
                }

                List<String> shouldVals = musts.get(mustKey);
                if (shouldVals.size() > 0) {
                    pattern = "AND (" + shouldVals.get(0);
                }
                for (int i=1; i<shouldVals.size(); i++) {
                    pattern += " OR " + shouldVals.get(0);
                }

                pattern += ")";

                System.out.println ("Query pattern: " + mustKey + ":" + pattern);
                query = QueryBuilders.matchQuery(mustKey, pattern);
            }

            //QueryBuilder query1 = QueryBuilders.matchQuery("FO", "北京到 AND (泰安 OR 巴楚)");
            //MatchQueryBuilder query2 = QueryBuilders.matchQuery("FO", "泰安");
            //QueryBuilder query = QueryBuilders.boolQuery().must(query1); //.should(query2);

            SearchRequestBuilder search = client.prepareSearch(applicationName)
                    .setTypes(collectionName)
                    .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                    .setQuery(query)
                    .setFrom(skip)
                    .setSize(limit)
                    .setExplain(true)
                    .addFields(fields);
            //.setPostFilter(FilterBuilders.rangeFilter("age").from(12).to(18))   // Filter

            ESSearchQuery result = new ESSearchQuery();
            result.search = search;
            return result;
        }
    }
}
