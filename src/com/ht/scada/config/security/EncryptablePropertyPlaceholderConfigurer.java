package com.ht.scada.config.security;

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
            props.setProperty("jdbc.password", password.concat("ltx_212"));
        }
		
		super.processProperties(beanFactoryToProcess, props);
	}

	
}
