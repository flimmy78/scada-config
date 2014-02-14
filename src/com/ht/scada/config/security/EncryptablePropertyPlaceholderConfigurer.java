package com.ht.scada.config.security;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

public class EncryptablePropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {

	@Override
	protected void processProperties(
			ConfigurableListableBeanFactory beanFactoryToProcess,
			Properties props) throws BeansException {
		
		
		String password = props.getProperty("jdbc.password");
		
		if (password != null) {
			String jiemi = null;
			try {
				jiemi = new String(AESUtil.decrypt2(AESUtil.parseHexStr2Byte(password), "dltx"), "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			String result = "d" + jiemi; 
            props.setProperty("jdbc.password", result.trim());
        }
		
		super.processProperties(beanFactoryToProcess, props);
	}
}
