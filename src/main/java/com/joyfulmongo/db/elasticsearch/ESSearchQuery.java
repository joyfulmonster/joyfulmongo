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

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by wbao on 6/22/2014.
 */
public class ESSearchQuery {
    private static Logger LOGGER = Logger.getLogger(ESSearchQuery.class.getName());

    private JSONObject projections;
    private String collectionName;
    private JSONObject constraints;

    private int limit;
    private int skip;
    private JSONObject sortBy = null;

    private String distinctField;

    private ESSearchQuery(String indexName, 
                          JSONObject constraints, 
                          JSONObject projection,
                          int limit, 
                          int skip, 
                          JSONObject sort)
    {
        this.collectionName = indexName;
        this.constraints = constraints;
        this.projections = projection;
        this.limit = limit;
        this.skip = skip;
        this.sortBy = sort;
    }

    public JSONObject getConstraints()
    {
        return this.constraints;
    }

    public List<JSONObject> find()
    {
        Settings settings = ImmutableSettings.settingsBuilder().build(); //put("cluster.name", "kcpes").build();
        Client client =  new TransportClient(settings).addTransportAddress(new InetSocketTransportAddress("192.168.127.139", 9300));

        MatchQueryBuilder query1 = QueryBuilders.matchQuery("FO", "北京到 AND (泰安 OR 巴楚)");
        //MatchQueryBuilder query2 = QueryBuilders.matchQuery("FO", "泰安");
        BoolQueryBuilder query = QueryBuilders.boolQuery().must(query1); //.should(query2);

        SearchResponse response = client.prepareSearch("kcpdb")
                .setTypes("Freight")
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(query1)             // Query
                //.setPostFilter(FilterBuilders.rangeFilter("age").from(12).to(18))   // Filter
                .setFrom(0).setSize(60).setExplain(true)
                .execute()
                .actionGet();

        SearchHits hits = response.getHits();
        for (SearchHit hit : hits.getHits()) {
            String str = hit.getSourceAsString();
            System.out.println ( "hit " + str);
        }

        return null;
    }

    public static class Builder
    {
        private String colname;
        private JSONObject constraint;
        private JSONObject projection;
        private int limit;
        private int skip;
        private String distinctField;
        private JSONObject sort;

        public Builder()
        {
            this.colname = colname;
            this.limit = 100;
            this.skip = 0;
            this.sort = null;
            this.projection = new JSONObject();
            this.constraint = new JSONObject();
            this.sort = new JSONObject();
            this.distinctField = null;
        }

        public void collection(String colname2)
        {
            this.colname = colname2;
        }

        public Builder projection(String... fields)
        {
            return this;
        }

        public Builder distinct(String distinctField)
        {
            this.distinctField = distinctField;
            return this;
        }

        public Builder projectionExclude(String... fields)
        {
            return this;
        }

        public Builder constraints(JSONObject constraint)
        {
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
            ESSearchQuery result;
            result = new ESSearchQuery(colname, constraint, projection, limit, skip, sort);

            return result;
        }
    }
}
