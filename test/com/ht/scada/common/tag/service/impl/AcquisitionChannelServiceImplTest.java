package com.ht.scada.common.tag.service.impl;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.ht.scada.common.tag.entity.AcquisitionChannel;
import com.ht.scada.common.tag.service.AcquisitionChannelService;
import com.ht.scada.common.tag.util.CommunicationProtocal;

public class AcquisitionChannelServiceImplTest {
	
	private AcquisitionChannelService acquisitionChannelService;
  @BeforeTest
  public void beforeTest() {
	  ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");  
	  acquisitionChannelService = (AcquisitionChannelServiceImpl) context.getBean("acquisitionChannelService");  
	  
  }


  @Test
  public void create() {
	  AcquisitionChannel a = new AcquisitionChannel();
		a.setIdx(22);
		a.setName("344");
		a.setFrames("eeeee");
		a.setPortInfo("dddd");
		a.setProtocal(CommunicationProtocal.IEC104);
		
	  acquisitionChannelService.create(a);
  }
}
