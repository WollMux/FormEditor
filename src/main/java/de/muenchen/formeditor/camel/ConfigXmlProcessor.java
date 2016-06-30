package de.muenchen.formeditor.camel;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.w3c.dom.Document;

import de.muenchen.formeditor.form.IForm;

public class ConfigXmlProcessor implements Processor
{

  @Override
  public void process(Exchange exchange) throws Exception
  {
    IForm body = exchange.getIn().getBody(IForm.class);
    Document doc = body.getPersistentDataAsXML();
    exchange.getOut().setBody(doc);
  }

}
