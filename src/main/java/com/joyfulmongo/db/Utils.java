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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.SimpleTimeZone;

public class Utils
{
  public static DateFormat getParseDateFormat()
  {
    DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
        Locale.US);
    format.setTimeZone(new SimpleTimeZone(0, "GMT"));
    return format;
  }
  
  public static Date getCurrentTime()
  {
    Date now = new Date();
    return now;
  }
  
  public static String getCurrentTimeStr()
  {
    Date now = new Date();
    DateFormat format = getParseDateFormat();
    String result = format.format(now);
    return result;
  }
}
