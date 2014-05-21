package com.joyfulmongo.controller;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class JFController<INPUT extends JFInput, OUTPUT extends JFOutput>
{
  protected static Logger LOGGER = Logger.getLogger(JFController.class
      .getName());
  
  abstract protected OUTPUT process(INPUT input);
  
  public JFResult processWrapper(INPUT input)
  {
    JFResult result;
    try
    {
      if (LOGGER.isLoggable(Level.FINE))
      {
        LOGGER.log(Level.FINE, "Input=" + input);
      }
      OUTPUT output = process(input);
      result = output.getResult();
    } 
    catch (JFUserError e)
    {
      LOGGER.log(Level.WARNING, "JFUserError " + e.getError());
      result = new JFResult.JFResultBuilder().errorCode(e.getCode())
          .errorMsg(e.getError()).build();
    } catch (Throwable e)
    {
      LOGGER.log(Level.SEVERE, "Failed process " + this.getClass(), e);
      result = new JFResult.JFResultBuilder().errorCode(500)
          .errorMsg(e.getMessage()).build();
    }
    
    return result;
  }
  
  private static Map<Class<? extends JFInput>, Class<? extends JFController>> inputToController;
  
  static
  {
    inputToController = new HashMap<Class<? extends JFInput>, Class<? extends JFController>>(
        8);
    inputToController.put(JFCreateInput.class, JFCreateController.class);
    inputToController.put(JFDeleteInput.class, JFDeleteController.class);
    inputToController.put(JFUpdateInput.class, JFUpdateController.class);
    inputToController.put(JFGetInput.class, JFGetController.class);
    inputToController.put(JFFindInput.class, JFFindController.class);
    inputToController.put(JFLoginInput.class, JFLoginController.class);
    inputToController.put(JFSignupInput.class, JFSignupController.class);
    inputToController.put(JFMultiInput.class, JFMultiController.class);
  }
  
  public static <INPUT extends JFInput, OUTPUT extends JFOutput> JFController<INPUT, OUTPUT> getInstance(
      Class<INPUT> inputClass, Class<OUTPUT> resultClass)
  {
    Class<? extends JFController> controllerClass = inputToController
        .get(inputClass);
    JFController<INPUT, OUTPUT> result = null;
    try
    {
      Constructor<? extends JFController> constructor = controllerClass
          .getConstructor();
      result = constructor.newInstance();
    } catch (Exception e)
    {
      e.printStackTrace();
      throw new IllegalArgumentException(e);
    }
    return result;
  }
}
