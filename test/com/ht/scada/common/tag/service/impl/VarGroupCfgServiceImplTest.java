package com.ht.scada.common.tag.service.impl;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.ht.scada.common.tag.entity.VarGroupCfg;
import com.ht.scada.common.tag.service.VarGroupCfgService;
import com.ht.scada.common.tag.util.VarGroup;

public class VarGroupCfgServiceImplTest {
	
	private VarGroupCfgService varGroupCfgService;
	
	@BeforeTest
	  public void beforeTest() {
		  @SuppressWarnings("resource")
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");  
		  varGroupCfgService = (VarGroupCfgServiceImpl) context.getBean("varGroupCfgService");  
		  
	  }
	
  @Test
  public void getAllVarGroupCfg() {
	  
	  List<VarGroupCfg> varGroupCfgs = varGroupCfgService.getAllVarGroupCfg();
	  for(VarGroupCfg varGroupCfg : varGroupCfgs){
		  System.out.println(varGroupCfg.getId() + " "+ varGroupCfg.getIntvl()+" "+ varGroupCfg.getName());
	  }
//	  
	  VarGroup value = VarGroup.values()[1];
	  
	  System.out.println(value);
	  System.out.println( value.ordinal());
	  System.out.println( value.getValue());
	  
  }
}
