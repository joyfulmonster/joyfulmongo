package com.joyfulmongo.controller;

import com.joyfulmongo.db.JFMongoCmdResult;

public class JFUpdateOutput extends JFOutput
{
  public JFUpdateOutput(JFMongoCmdResult result)
  {
    setResult(new JFResult.JFResultBuilder().data(result).build());
  }
}
