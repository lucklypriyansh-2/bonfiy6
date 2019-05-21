package com.gateway.main.transformers;


import javax.servlet.http.HttpServletRequest;

import org.apache.http.client.methods.RequestBuilder;

import com.gateway.main.filters.FilterFactory;

public abstract class ProxyRequestTransformer {

  protected ProxyRequestTransformer predecessor;
  protected FilterFactory filterfactory;

  public abstract RequestBuilder transform(HttpServletRequest request);

  public void setPredecessor(ProxyRequestTransformer transformer) {
    this.predecessor = transformer;
  }
}
