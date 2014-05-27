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

import java.util.List;

import com.joyfulmongo.db.JFMongoCmdQuery;
import org.json.JSONObject;

import com.joyfulmongo.db.JFMongoObject;

public class JFFindController extends JFController<JFFindInput, JFFindOutput>
{
  @Override
  public JFFindOutput process(JFFindInput input)
  {
    String colName = input.getClassname();
    
    JSONObject condition = input.getData();
    String sort = input.getOrder();
    String[] includes = input.getIncludes();
    
    int limit = input.getLimit();
    int skip = input.getSkip();
    
    String redirectClassNameForKey = input.getRedirectClassNameForKey();
    
    JFMongoCmdQuery query = new JFMongoCmdQuery.Builder(colName)
        .constraints(condition).limit(limit).skip(skip).include(includes)
        .sort(sort).redirectClassNameForKey(redirectClassNameForKey).build();
    
    List<JFMongoObject> results = query.find();
    
    if (results == null)
    {
      throw new JFUserError(101, "object not found for get");
    } else
    {
      JFFindOutput output = new JFFindOutput(results);
      return output;
    }
  }
}
