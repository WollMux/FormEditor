package de.muenchen.formeditor.camel;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.castor.CastorDataFormat;
import org.slf4j.Logger;

@ApplicationScoped
public class FMRouteBuilder extends RouteBuilder
{
  @Inject
  private ConfigXmlProcessor configXmlProcessor;
  
  @Inject
  public FMRouteBuilder(Logger log)
  {
    super();
    this.log = log;
  }
  
  @Override
  public void configure() throws Exception
  {
    CastorDataFormat castor = new CastorDataFormat();
    castor.setMappingFile("mapping.xml");
    
    from("direct:read-config").process(configXmlProcessor).unmarshal(castor);
    from("direct:write-config").marshal(castor);
  }
}
