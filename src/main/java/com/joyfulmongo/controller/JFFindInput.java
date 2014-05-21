package com.joyfulmongo.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

public class JFFindInput extends JFSingleDataInput
{
  public JFFindInput(String jsonStr)
  {
    super(jsonStr);
  }
  
  public Integer getLimit()
  {
    Integer limit = optInt(JFCConstants.Props.limit.toString());
    return limit;
  }
  
  public Integer getSkip()
  {
    Integer skip = optInt(JFCConstants.Props.skip.toString());
    return skip;
  }
  
  public String[] getFields()
  {
    String str = optString(JFCConstants.Props.fields.toString());
    Set<String> keys = new HashSet<String>();
    StringTokenizer st = new StringTokenizer(str, ",");
    while (st.hasMoreTokens())
    {
      keys.add(st.nextToken());
    }
    
    String[] result = new String[keys.size()];
    result = keys.toArray(result);
    
    return result;
  }
  
  public String getOrder()
  {
    String order = optString(JFCConstants.Props.order.toString());
    return order;
  }
  
  public String getRedirectClassNameForKey()
  {
    String redirect = optString(JFCConstants.Props.redirectClassNameForKey
        .toString());
    return redirect;
  }
  
  public String[] getIncludes()
  {
    String[] result = new String[0];
    String str = optString(JFCConstants.Props.include.toString());
    if (str != null && str.length() != 0)
    {
      StringTokenizer st = new StringTokenizer(str, ",");
      List<String> stlist = new ArrayList<String>(0);
      while (st.hasMoreTokens())
      {
        stlist.add(st.nextToken());
      }
      result = stlist.toArray(result);
    }
    return result;
  }
}
