package com.joyfulmongo.controller;

public class JFDeleteOutput extends JFOutput
{
  public JFDeleteOutput(Boolean result)
  {
    setResult(new JFResult.JFResultBuilder().booleanResult(result).build());
  }
  
  public JFDeleteOutput(JFUserError error)
  {
    setResult(new JFResult.JFResultBuilder().errorCode(error.getCode())
        .errorMsg(error.getMessage()).build());
  }
  
}
