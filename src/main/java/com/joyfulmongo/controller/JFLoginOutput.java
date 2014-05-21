package com.joyfulmongo.controller;

import com.joyfulmongo.db.JFMongoObject;

public class JFLoginOutput extends JFOutput
{
  public JFLoginOutput()
  {
    setResult(new JFResult.JFResultBuilder().nullResult().build());
  }
  
  public JFLoginOutput(String sessionToken, JFMongoObject user)
  {
    setResult(new JFResult.JFResultBuilder().data(user.toJson())
        .sessionToken(sessionToken).build());
  }
  
  public JFLoginOutput(JFUserError error)
  {
    setResult(new JFResult.JFResultBuilder().errorCode(error.getCode())
        .errorMsg(error.getMessage()).build());
  }
  
}
