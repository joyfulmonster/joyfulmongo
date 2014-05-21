package com.joyfulmongo.controller;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

public class JFMultiController extends
    JFController<JFMultiInput, JFMultiOutput>
{
  @Override
  public JFMultiOutput process(JFMultiInput input)
  {
    List<JFCommandInput> commands = input.getCommands();
    List<JFResult> results = new ArrayList<JFResult>(commands.size());
    for (JFCommandInput command : commands)
    {
      JFCommandInput.Operation op = command.getOp();
      switch (op)
      {
        case create:
        {
          JSONObject data = command.getData();
          String subColname = command.getClassname();
          JFCreateInput createInput = new JFCreateInput(subColname, data);
          JFController<JFCreateInput, JFCreateOutput> controller = JFController
              .getInstance(JFCreateInput.class, JFCreateOutput.class);
          JFCreateOutput output = controller.process(createInput);
          results.add(output.getResult());
          break;
        }
        case update:
        {
          String subColname = command.getClassname();
          JSONObject data = command.getData();
          JFUpdateInput createInput = new JFUpdateInput(subColname, data);
          JFController<JFUpdateInput, JFUpdateOutput> controller = JFController
              .getInstance(JFUpdateInput.class, JFUpdateOutput.class);
          JFUpdateOutput output = controller.process(createInput);
          results.add(output.getResult());
          break;
        }
        default:
          throw new IllegalArgumentException("The input command operation is "
              + op + " does not know how to handle it.");
      }
    }
    JFMultiOutput multiOutput = new JFMultiOutput(results);
    return multiOutput;
  }
}
