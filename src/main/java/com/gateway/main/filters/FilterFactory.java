package com.gateway.main.filters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class FilterFactory {

	@Autowired
	ApplicationContext context;
	
	public Filter getInstance(FilterEnum f)
	{
		return (Filter) context.getBean(f.clazz);
		
	}
	
	
	
}
