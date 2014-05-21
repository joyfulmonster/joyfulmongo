package com.joyfulmongo.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONObject;

public class JFMultiInput extends JFDataArrayInput
{
  private static Logger LOGGER = Logger.getLogger(JFMultiInput.class.getName());

  public JFMultiInput(String jsonStr)
  {
    super(jsonStr);
  }
  
  public List<JFCommandInput> getCommands()
  {
    List<JFCommandInput> commands = new ArrayList<JFCommandInput>();
    JSONArray array = getJSONArray(JFCConstants.Props.commands.toString());
    for (int i = 0; i < array.length(); i++)
    {
      JSONObject json = array.getJSONObject(i);
      if (LOGGER.isLoggable(Level.FINE))
      {
        LOGGER.fine("  Command multi-input " + json);
      }
      JFCommandInput command = new JFCommandInput(json);
      commands.add(command);
    }
    
    return commands;
  }
}
