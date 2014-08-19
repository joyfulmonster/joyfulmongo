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
package com.joyfulmongo.db;

public class JFMongoException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private int code;
    public static final int OTHER_CAUSE = -1;

    public static final int INTERNAL_SERVER_ERROR = 1;
    public static final int CONNECTION_FAILED = 100;
    public static final int OBJECT_NOT_FOUND = 101;
    public static final int INVALID_QUERY = 102;
    public static final int INVALID_CLASS_NAME = 103;
    public static final int MISSING_OBJECT_ID = 104;
    public static final int INVALID_KEY_NAME = 105;
    public static final int INVALID_POINTER = 106;
    public static final int INVALID_JSON = 107;
    public static final int COMMAND_UNAVAILABLE = 108;
    public static final int NOT_INITIALIZED = 109;
    public static final int INCORRECT_TYPE = 111;
    public static final int INVALID_CHANNEL_NAME = 112;
    public static final int PUSH_MISCONFIGURED = 115;
    public static final int OBJECT_TOO_LARGE = 116;
    public static final int OPERATION_FORBIDDEN = 119;
    public static final int CACHE_MISS = 120;
    public static final int INVALID_NESTED_KEY = 121;
    public static final int INVALID_FILE_NAME = 122;
    public static final int INVALID_ACL = 123;
    public static final int TIMEOUT = 124;
    public static final int INVALID_EMAIL_ADDRESS = 125;
    public static final int DUPLICATE_VALUE = 137;
    public static final int INVALID_ROLE_NAME = 139;
    public static final int EXCEEDED_QUOTA = 140;
    public static final int SCRIPT_ERROR = 141;
    public static final int VALIDATION_ERROR = 142;
    public static final int FILE_DELETE_ERROR = 153;
    public static final int USERNAME_MISSING = 200;
    public static final int PASSWORD_MISSING = 201;
    public static final int USERNAME_TAKEN = 202;
    public static final int EMAIL_TAKEN = 203;
    public static final int EMAIL_MISSING = 204;
    public static final int EMAIL_NOT_FOUND = 205;
    public static final int SESSION_MISSING = 206;
    public static final int MUST_CREATE_USER_THROUGH_SIGNUP = 207;
    public static final int ACCOUNT_ALREADY_LINKED = 208;
    public static final int LINKED_ID_MISSING = 250;
    public static final int INVALID_LINKED_SESSION = 251;
    public static final int UNSUPPORTED_SERVICE = 252;

    public JFMongoException(int theCode, String theMessage) {
        super(theMessage);
        this.code = theCode;
    }

    public JFMongoException(String message, Throwable cause) {
        super(message, cause);
        this.code = -1;
    }

    public JFMongoException(Throwable cause) {
        super(cause);
        this.code = -1;
    }

    public int getCode() {
        return this.code;
    }
}
