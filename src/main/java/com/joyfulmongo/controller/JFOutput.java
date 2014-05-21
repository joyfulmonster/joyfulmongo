package com.joyfulmongo.controller;

public abstract class JFOutput
{
  private JFResult result;
  
  protected JFOutput()
  {
  }
  
  protected void setResult(JFResult result)
  {
    this.result = result;
  }
  
  public JFResult getResult()
  {
    return this.result;
  }
}
