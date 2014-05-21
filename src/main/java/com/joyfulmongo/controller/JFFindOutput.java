package com.joyfulmongo.controller;

import java.util.List;

import com.joyfulmongo.db.JFMongoObject;

public class JFFindOutput extends JFOutput
{
  public JFFindOutput(List<JFMongoObject> objs)
  {
    JFResults.JFResultsBuilder resultsBuilder = new JFResults.JFResultsBuilder();
    for (JFMongoObject obj : objs)
    {
      resultsBuilder.addData(obj.toJson());
    }
    
    JFResults results = resultsBuilder.build();
    JFResult result = new JFResult.JFResultBuilder().results(results).build();
    setResult(result);
  }
  
  public JFFindOutput(JFUserError error)
  {
    setResult(new JFResult.JFResultBuilder().errorCode(error.getCode())
        .errorMsg(error.getMessage()).build());
  }
}
