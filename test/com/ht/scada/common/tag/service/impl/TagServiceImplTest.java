package com.ht.scada.common.tag.service.impl;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class TagServiceImplTest {

	

	private TagServiceImpl tagService;
	  @BeforeTest
	  public void beforeTest() {
		  ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");  
		  tagService = (TagServiceImpl) context.getBean("tagService");  
		  
	  }
  @Test
  public void getTagCfgTplByCodeAndVarName() {
	  System.out.println(tagService.getTagCfgTplByCodeAndVarName("1", "jing_kou_w_du").getTagName());
  }
  @Test
  public void getSensorDeviceByCode() {
	  System.out.println("个数：" + tagService.getSensorDeviceByCode("油井#自喷").size());
  }
  @Test
  public void getSensorDeviceByCodeAndNickName() {
	  System.out.println("个数：" + tagService.getSensorDeviceByCodeAndNickName("油井#自喷", "number1").getName());
  }
}
