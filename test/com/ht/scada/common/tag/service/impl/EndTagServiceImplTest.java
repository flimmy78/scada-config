package com.ht.scada.common.tag.service.impl;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.ht.scada.common.tag.util.EndTagTypeEnum;

public class EndTagServiceImplTest {
	private EndTagServiceImpl endTagService;
	  @BeforeTest
	  public void beforeTest() {
		  ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");  
		  endTagService = (EndTagServiceImpl) context.getBean("endTagService");  
		  
	  }
  @Test
  public void getByType() {
    System.out.println(endTagService.getByType(EndTagTypeEnum.TIAN_RAN_QI_JING.toString()).size());
  }
}
