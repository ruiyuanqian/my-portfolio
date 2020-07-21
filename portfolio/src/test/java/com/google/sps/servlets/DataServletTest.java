package com.google.sps.servlets;

import com.google.sps.servlets.DataServlet;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class DataServletTest {

  @Test
  public void testFuncCheckUnallowedInput() {
    DataServlet aDataServlet = new DataServlet();
    int unallowedInputType;

    unallowedInputType = aDataServlet.checkUnallowedInput("","");
    assertEquals(1, unallowedInputType);

    unallowedInputType = aDataServlet.checkUnallowedInput("http://localhost:3000/a.jpg","");
    assertEquals(1, unallowedInputType);

    unallowedInputType = aDataServlet.checkUnallowedInput("http://wx4.sinaimg.cn/mw600/006z09Zkly1gafwzhq2d7j30yi16pu06.jpg","");
    assertEquals(2, unallowedInputType);
    
    unallowedInputType = aDataServlet.checkUnallowedInput("http://wx4.sinaimg.cn/mw600/006z09Zkly1gafwzhq2d7j30yi16pu06.jpg","Let's upload!");
    assertEquals(0, unallowedInputType);
    
    unallowedInputType = aDataServlet.checkUnallowedInput("http://wx4.sinaimg.cn/mw600/bmpShouldNotBeAllowed.bmp","Let's upload!");
    assertEquals(1, unallowedInputType);
  }

}