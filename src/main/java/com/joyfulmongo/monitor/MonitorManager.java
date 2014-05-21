package com.joyfulmongo.monitor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.json.JSONObject;

public class MonitorManager
{
  private static MonitorManager sInstance;
  private static Object syncObj = new Object();
  public static MonitorManager getInstance()
  {
    if (sInstance == null)
    {
      synchronized (syncObj)
      {
        if (sInstance == null)
        {
          sInstance = new MonitorManager();
        }
      }
    }
    
    return sInstance;
  }
  
  private class Record
  {
    private AtomicLong count;
    private AtomicLong maxTime;
    private AtomicLong totalTime;
    private JSONObject payload;
    
    public Record(long duration)
    {
      count = new AtomicLong(1);
      maxTime = new AtomicLong(duration);
      totalTime = new AtomicLong(duration);
    }
    
    public AtomicLong getCount()
    {
      return count;
    }
    public AtomicLong getMaxTime()
    {
      return maxTime;
    }
    public AtomicLong getTotalTime()
    {
      return totalTime;
    }    
    public void setPayload(JSONObject payload)
    {
      this.payload = payload;
    }    
    public JSONObject getPayload()
    {
      return this.payload;
    }
    
    public String toString()
    {
      return "c=" + this.getCount().get() + " mt=" + this.getMaxTime().get() + " tt=" + this.getTotalTime().get() + " payload=" + getPayload();
    }
  }
  
  private ConcurrentHashMap<String, Record> queryProfile;
  private ConcurrentHashMap<String, Record> updateProfile;
  private MonitorManager()
  {
    queryProfile = new ConcurrentHashMap<String, Record>();
    updateProfile = new ConcurrentHashMap<String, Record>();
  }
  
  public void logQuery(String collection, JSONObject query, JSONObject sort, long duration)
  {
    List<String> queryKeys = new ArrayList<String>();
    queryKeys.addAll(query.keySet());
    Collections.sort(queryKeys);
    String keysStr = getString(queryKeys);
    
    String theKey = "db.getCollection(\"" + collection + "\").ensureIndex({" + keysStr + "})";
    Record record = new Record(duration);
    record.setPayload(query);
    queryProfile.putIfAbsent(theKey, record);
    queryProfile.get(theKey).getCount().incrementAndGet();      
    queryProfile.get(theKey).getTotalTime().addAndGet(duration);    
    long lastDuration = queryProfile.get(theKey).getMaxTime().get();
    if (lastDuration < duration)
    {
      queryProfile.get(theKey).getMaxTime().set(duration);
    }
  }  
  
  public void logUpdate(String collection, JSONObject update, JSONObject sort, long duration)
  {
    List<String> queryKeys = new ArrayList<String>();
    queryKeys.addAll(update.keySet());
    Collections.sort(queryKeys);
    String keysStr = getString(queryKeys);
    
    String theKey = "db.getCollection(\"" + collection + "\").ensureIndex({" + keysStr + "})";
    
    Record record = new Record(duration);
    record.setPayload(update);
    updateProfile.putIfAbsent(theKey, record);
    updateProfile.get(theKey).getCount().incrementAndGet();      
    updateProfile.get(theKey).getTotalTime().addAndGet(duration);    
    long lastDuration = updateProfile.get(theKey).getMaxTime().get();
    if (lastDuration < duration)
    {
      updateProfile.get(theKey).getMaxTime().set(duration);
    }
  }
  
  public String diagnosticQueries()
  {
    return this.getString(queryProfile);
  }
  
  private String getString(List<String> strs)
  { 
    String result = "";
    if (strs.size() > 0)
    {
      result = strs.get(0) + ":1";
    }
    for (int i=1; i<strs.size(); i++)
    {
      result += "," + strs.get(i) + ":1";
    }
    
    return result;
  }
  
  private String getString (ConcurrentHashMap<String, Record> map)
  {    
    String result = "";    
    List<String> keys = new ArrayList<String>();
    Set<Entry<String, Record>> entries = map.entrySet();
    Iterator<Entry<String, Record>> iter = entries.iterator();
    while (iter.hasNext())
    {
      keys.add(iter.next().getKey());
    }
    Collections.sort(keys);
            
    for (String key : keys)
    {
      Record record = map.get(key);
      result += key + "," + record + "\n";
    }    
    
    if (result.length() == 0)
    {
      result = "No result";
    }
    return result;    
  }
}
