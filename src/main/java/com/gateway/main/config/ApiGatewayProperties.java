package com.gateway.main.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.RequestMethod;

import com.gateway.main.filters.FilterEnum;

@Configuration
@ConfigurationProperties(prefix = "api.gateway")
@PropertySource("classpath:application.properties")
public class ApiGatewayProperties {

  private List<Endpoint> endpoints;

  public static class Filter
  {
	  String filterName;
	  String filterkey;
	  String filtervalue;
	  String filterType;
	  
	FilterEnum associatedFilter;
	  
	
	  public String getFilterType() {
		return filterType;
	}
	public void setFilterType(String filterType) {
		this.filterType = filterType;
	}
	public FilterEnum getAssociatedFilter() {
		return associatedFilter;
	}
	public void setAssociatedFilter(FilterEnum associatedFilter) {
		this.associatedFilter = associatedFilter;
	}
	public String getFilterName() {
		return filterName;
	}
	public void setFilterName(String filterName) {
		this.filterName = filterName;
	}
	public String getFilterkey() {
		return filterkey;
	}
	public void setFilterkey(String filterkey) {
		setAssociatedFilter(FilterEnum.findbyName(filterkey));
		this.filterkey = filterkey;
	}
	public String getFiltervalue() {
		return filtervalue;
	}
	public void setFiltervalue(String filtervalue) {
		this.filtervalue = filtervalue;
	}
	  
	 
  }
  
  public static class Endpoint {
    private String path;
    private RequestMethod method;
    private List<String> location;
    private List<Filter> filters;

    



	public List<Filter> getFilters() {
		return filters;
	}

	public void setFilters(List<Filter> filters) {
		this.filters = filters;
	}

	public Endpoint() {
    }

    public Endpoint(List<String> location) {
      this.location = location;
    }

    public String getPath() {
      return path;
    }

    public void setPath(String path) {
      this.path = path;
    }

    public RequestMethod getMethod() {
      return method;
    }

    public void setMethod(RequestMethod method) {
      this.method = method;
    }

    public List<String> getLocation() {
      return location;
    }

    public void setLocation(List<String> location) {
      this.location = location;
    }
  }

  public List<Endpoint> getEndpoints() {
    return endpoints;
  }

  public void setEndpoints(List<Endpoint> endpoints) {
    this.endpoints = endpoints;
  }
}
