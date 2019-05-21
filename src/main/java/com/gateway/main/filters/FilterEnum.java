package com.gateway.main.filters;

public enum FilterEnum {

	DATAFILTER("DataFilter", DataFilter.class), DSL("AuthFilter", AuthFilter.class);

	Class  clazz;
	String name;

	FilterEnum(String name, Class clazz) {
		this.clazz = clazz;
		this.name  = name;
	}

	public static FilterEnum findbyName(String name) {
		for (FilterEnum denum : FilterEnum.values()) {
			 if(denum.name.equals(name))
			 return denum;
		}
		return null;
	}
}
