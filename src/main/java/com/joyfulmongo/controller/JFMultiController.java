/*
 * Copyright 2014 Weifeng Bao
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
*/
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
