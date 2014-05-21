package com.joyfulmongo.controller;

import com.joyfulmongo.db.JFMongoCmdResult;

public class JFSignupOutput extends JFOutput
{
  public JFSignupOutput(String sessionToken, JFMongoCmdResult result)
  {
    setResult(new JFResult.JFResultBuilder().data(result)
        .sessionToken(sessionToken).build());
  }
  
  public JFSignupOutput(JFUserError error)
  {
    setResult(new JFResult.JFResultBuilder().errorCode(error.getCode())
        .errorMsg(error.getMessage()).build());
  }
}
