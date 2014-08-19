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

public class JFCConstants {
    public enum Props {
        // result
        result,
        results,

        // comman
        data,
        classname,
        session_token,
        iid,
        uuid,
        v,
        code,
        error,

        // login
        username,
        user_password,

        // mult
        commands,
        params,
        op,

        // find
        limit,
        skip,
        fields,
        order,
        include,
        redirectClassNameForKey, // this is special condition for relation query

        // elasticsearch
        query_must,
        query_should,
    }
}

