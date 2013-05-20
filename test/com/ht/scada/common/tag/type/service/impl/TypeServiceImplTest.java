package com.ht.scada.common.tag.type.service.impl;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.ht.scada.common.tag.entity.VarGroupCfg;
import com.ht.scada.common.tag.type.service.TypeService;

public class TypeServiceImplTest {
	
	private TypeService typeService;
	
  @BeforeTest
  public void beforeTest() {
	  ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");  
	  typeService = (TypeServiceImpl) context.getBean("typeService");  
  }


  @Test
  public void insertVarGroupType() {
    VarGroupCfg varGroupType = new VarGroupCfg();
    varGroupType.setName("name");
    varGroupType.setValue("value");
    
    typeService.insertVarGroupCgf(varGroupType);
  }
}
