package com.joyfulmongo.controller;

import java.util.List;

public class JFMultiOutput extends JFOutput
{
  public JFMultiOutput(List<JFResult> results)
  {
    JFResult.JFResultArrayBuilder resultBuilder = new JFResult.JFResultArrayBuilder();
    for (JFResult result : results)
    {
      resultBuilder.data(result.getData());
    }
    setResult(resultBuilder.build());
  }
  
  public JFMultiOutput(JFUserError error)
  {
    setResult(new JFResult.JFResultBuilder().errorCode(error.getCode())
        .errorMsg(error.getMessage()).build());
  }
  
}
