package com.joyfulmongo.controller;

import com.joyfulmongo.db.JFMongoObject;

public class JFGetOutput extends JFOutput
{
  public JFGetOutput(JFMongoObject obj)
  {
    setResult(new JFResult.JFResultBuilder().data(obj.toJson()).build());
  }
  
  public JFGetOutput(JFUserError error)
  {
    setResult(new JFResult.JFResultBuilder().errorCode(error.getCode())
        .errorMsg(error.getMessage()).build());
  }
  
}
