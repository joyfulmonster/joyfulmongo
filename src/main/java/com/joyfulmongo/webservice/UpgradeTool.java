package com.joyfulmongo.webservice;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.json.JSONObject;

import com.joyfulmongo.controller.JFResult;
import com.joyfulmongo.db.Utils;
import com.joyfulmongo.db.javadriver.JFDBCollection;
import com.joyfulmongo.db.javadriver.JFDBCollectionFactory;
import com.joyfulmongo.db.javadriver.JFDBObject;
import com.joyfulmongo.db.javadriver.JFDBQuery;

@Path("/upgrade")
public class UpgradeTool extends BaseResource
{
  public static final String S_CLASS_USER     = "_User";
  public static final String S_CLASS_FREIGHT  = "Freight";
  public static final String S_CLASS_LOCATION = "Loc";
  public static final String S_CLASS_LGROUP   = "LGroup";
  public static final String S_CLASS_LTEAM    = "LTeam";
  public static final String S_CLASS_FEEDBACK = "Feedback"; 
  public static final String S_CLASS_INSTALL  = "Install";

  public static final String S_CLASS_CASHOUT  = "CashOut";
  public static final String S_CLASS_GUARANTY = "Guaranty";
  public static final String S_CLASS_INSUR    = "Insur";
  public static final String S_CLASS_PAY      = "Pay";
  public static final String S_CLASS_WAYBILL  = "Waybill";
  public static final String S_CLASS_TEAMUSER = "TeamUser";
  public static final String S_CLASS_MONEYTREE_DIVIDENT = "MtDivident";
  public static final String S_CLASS_RECOMMEND= "Recommend";
  public static final String S_CLASS_REFERRAL = "Referral";
  public static final String S_CLASS_COIN     = "Coin";
  public static final String S_CLASS_REVIEW   = "Review";
  
  public static final String S_CLASS_RELATIONSHIP_POINTER = "_RelMeta";
  public static final String S_CLASS_RELATIONSHIP_METADATA = "_RelationshipMeta";

  public static final String[] collections = new String[]{
    S_CLASS_USER,
    S_CLASS_FREIGHT,
    S_CLASS_LOCATION,
    S_CLASS_LGROUP,
    S_CLASS_LTEAM,
    S_CLASS_FEEDBACK, 
    S_CLASS_INSTALL,

    S_CLASS_CASHOUT,
    S_CLASS_GUARANTY,
    S_CLASS_INSUR,
    S_CLASS_PAY,
    S_CLASS_WAYBILL,
    S_CLASS_TEAMUSER,
    S_CLASS_MONEYTREE_DIVIDENT,
    S_CLASS_RECOMMEND,
    S_CLASS_REFERRAL,
    S_CLASS_COIN,
    S_CLASS_REVIEW,
    
    S_CLASS_RELATIONSHIP_POINTER,
    S_CLASS_RELATIONSHIP_METADATA,    
  };
  
  public static String S_CREATED_AT = "createdAt";
  public static String S_UPDATED_AT = "updatedAt";
  public static String S_OBJECT_ID = "objectId";
  
  @Path("date")
  @GET
  @Consumes(Constants.JF_JSON)
  @Produces({ Constants.JF_JSON })
  public Response changeDate(String input) throws ParseException
  {
    for (String coll : collections)
    {
      JFDBCollection collection = JFDBCollectionFactory.getInstance().getCollection(coll);
      JFDBQuery query = new JFDBQuery.Builder(coll).build();
      List<JFDBObject> objs = query.find();
      for (JFDBObject obj : objs)
      {
        String objId = (String) obj.getDBObject().get(S_OBJECT_ID);
        DateFormat format = Utils.getParseDateFormat();
        JSONObject payload = new JSONObject();
        String createdAtStr = null;

        Object createdAtObj = obj.getDBObject().get(S_CREATED_AT);        
        if (createdAtObj != null)
        {
          if (createdAtObj instanceof String)
          {
            createdAtStr = (String) createdAtObj;
            Date createdDate = format.parse(createdAtStr);
            payload.put(S_CREATED_AT, createdDate);
          }
        }
        else
        {
          payload.put(S_CREATED_AT, new Date());          
        }
        
        Object updateObj = obj.getDBObject().get(S_UPDATED_AT);
        String updatedStr = null;
        if (updateObj != null)
        {
          if (updateObj instanceof String)
          {
            updatedStr = (String) updateObj;
            if (updatedStr != null)
            {
              Date date = format.parse(updatedStr);
              payload.put(S_UPDATED_AT, date);
            }          
          }
        }
        else
        {
          payload.put(S_UPDATED_AT, new Date());
        }
                        
        JSONObject jquery = new JSONObject();
        jquery.put(S_OBJECT_ID, objId );
        collection.update(jquery, payload);
      }        
    }
    
    JFResult result = new JFResult.JFResultBuilder().booleanResult(true).build();
    return getResponse(result);
  }  
}