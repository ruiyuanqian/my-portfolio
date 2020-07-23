package com.google.sps.servlets;

import com.google.sps.servlets.DataServlet;
import com.google.sps.servlets.DataServlet.InputValidationStatus;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class DataServletTest {

  @Test
  public void testFuncCheckUnallowedInput() {
    DataServlet aDataServlet = new DataServlet();
    InputValidationStatus unallowedInputType;

    unallowedInputType = aDataServlet.checkUnallowedInput("","");
    assertEquals( InputValidationStatus.ILLEGAL_URL , unallowedInputType);

    unallowedInputType = aDataServlet.checkUnallowedInput("http://localhost:3000/a.jpg","");
    assertEquals( InputValidationStatus.ILLEGAL_URL , unallowedInputType);

    unallowedInputType = aDataServlet.checkUnallowedInput("http://wx4.sinaimg.cn/mw600/006z09Zkly1gafwzhq2d7j30yi16pu06.jpg","");
    assertEquals( InputValidationStatus.ILLEGAL_COMMENT , unallowedInputType);
    
    unallowedInputType = aDataServlet.checkUnallowedInput("http://wx4.sinaimg.cn/mw600/006z09Zkly1gafwzhq2d7j30yi16pu06.jpg","Let's upload!");
    assertEquals( InputValidationStatus.VALID , unallowedInputType);
    
    unallowedInputType = aDataServlet.checkUnallowedInput("http://wx4.sinaimg.cn/mw600/bmpShouldNotBeAllowed.bmp","Let's upload!");
    assertEquals( InputValidationStatus.ILLEGAL_URL , unallowedInputType);
  }

}