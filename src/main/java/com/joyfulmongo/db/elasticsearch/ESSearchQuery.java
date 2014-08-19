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
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
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

    private ESSearchQuery() {
    }

    public List<JSONObject> find() {
        SearchResponse response = search.execute().actionGet();

        List<JSONObject> result = new ArrayList<JSONObject>();
        SearchHits hits = response.getHits();
        System.out.println("Got hits = " + hits.getTotalHits());
        for (SearchHit hit : hits.getHits()) {
            String str = hit.sourceAsString();
            System.out.println("Got result = " + str);
            result.add(new JSONObject(str));
        }

        return result;
    }

    public static class Builder {
        private String applicationName;
        private String collectionName = "kcpdb2";
        private String[] fields = new String[0];
        private JSONObject projection;
        private int limit;
        private int skip;
        private JSONObject sort;
        private Map<String, List<String>> musts;
        private Map<String, List<String>> shoulds;

        private static Client client;

        static {
            String esClusterName = System.getProperty("es.cluster.name", "kcpes");
            String esNodeHost = System.getProperty("es.node.hostname", "localhost");
            String esNodePort = System.getProperty("es.node.port", "9300");

            Settings settings = ImmutableSettings.settingsBuilder().build(); //put("cluster.name", esClusterName).build();
            client = new TransportClient(settings).addTransportAddress(new InetSocketTransportAddress(esNodeHost, Integer.parseInt(esNodePort)));
        }

        public Builder() {
            this.limit = 20;
            this.skip = 0;
            this.sort = null;
            this.musts = new HashMap<String, List<String>>(); // ArrayList<String>();
            this.shoulds = new HashMap<String, List<String>>(); //ArrayList<String>();
            this.projection = new JSONObject();
            this.sort = new JSONObject();
        }

        public Builder applicationName(String applicationName) {
            this.applicationName = applicationName;
            return this;
        }

        public Builder collectionName(String collectionName) {
            this.collectionName = collectionName;
            return this;
        }

        public Builder fields(String... fields) {
            this.fields = fields;
            return this;
        }

        public Builder must(String field, String... match) {
            List<String> matches = this.musts.get(field);
            if (matches == null) {
                matches = new ArrayList<String>();
                this.musts.put(field, matches);
            }
            matches.addAll(Arrays.asList(match));
            return this;
        }

        public Builder should(String field, String... match) {
            List<String> matches = this.shoulds.get(field);
            if (matches == null) {
                matches = new ArrayList<String>();
                this.shoulds.put(field, matches);
            }
            matches.addAll(Arrays.asList(match));
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
            return this;
        }

        public ESSearchQuery build() {
            BoolQueryBuilder query = QueryBuilders.boolQuery();
            Iterator<String> mustkeys = musts.keySet().iterator();
            while (mustkeys.hasNext()) {
                String mustKey = mustkeys.next();
                List<String> mustVal = musts.get(mustKey);
                QueryBuilder subQuery = QueryBuilders.matchPhraseQuery(mustKey, mustVal);
                query.must(subQuery);
            }

            Iterator<String> shouldkeys = shoulds.keySet().iterator();
            while (shouldkeys.hasNext()) {
                String shouldKey = shouldkeys.next();
                List<String> shouldVal = shoulds.get(shouldKey);
                QueryBuilder subQuery = QueryBuilders.matchPhraseQuery(shouldKey, shouldVal);
                query.should(subQuery);
            }

            System.out.println("app " + applicationName + " col " + collectionName + " size=" + limit + " skip=" + skip + " Query " + query);
            SearchRequestBuilder search = client.prepareSearch(applicationName)
                    .setTypes(collectionName)
                    .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                    .setQuery(query)
                    .setFrom(skip)
                    .setSize(limit)
                    .setExplain(true);

            if (fields.length > 0) {
                search.addFields(fields);
            }

            ESSearchQuery result = new ESSearchQuery();
            result.search = search;
            return result;
        }
    }
}
