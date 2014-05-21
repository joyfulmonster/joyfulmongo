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
