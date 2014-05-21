package com.joyfulmongo.controller;

import com.joyfulmongo.db.JFMongoCmdResult;

public class JFCreateOutput extends JFOutput
{
  public JFCreateOutput(JFMongoCmdResult result)
  {
    setResult(new JFResult.JFResultBuilder().data(result).build());
  }
  
  public JFCreateOutput(JFUserError error)
  {
    setResult(new JFResult.JFResultBuilder().errorCode(error.getCode())
        .errorMsg(error.getMessage()).build());
  }
}
