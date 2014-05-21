package com.joyfulmongo.db.javadriver;

@SuppressWarnings("serial")
public class JFDBUserException extends RuntimeException
{
  private int code;
  private String error;
  
  public JFDBUserException(int code, String error)
  {
    super(error);
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
