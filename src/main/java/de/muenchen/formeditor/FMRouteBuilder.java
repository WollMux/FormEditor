package de.muenchen.formeditor;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;

@ApplicationScoped
public class FMRouteBuilder extends RouteBuilder
{
  @Inject
  public FMRouteBuilder(Logger log)
  {
    super();
    this.log = log;
  }
  
  @Override
  public void configure() throws Exception
  {
    
  }
}
