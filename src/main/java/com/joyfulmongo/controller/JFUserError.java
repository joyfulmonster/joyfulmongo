package com.joyfulmongo.controller;

@SuppressWarnings("serial")
public class JFUserError extends RuntimeException
{
  private int code;
  private String error;
  
  public JFUserError(int code, String error)
  {
    this.code = code;
    this.error = error;
  }
  
  public int getCode()
  {
    return this.code;
  }
  
  public String getError()
  {
    return this.error;
  }
}
