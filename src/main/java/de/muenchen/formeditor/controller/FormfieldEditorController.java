package de.muenchen.formeditor.controller;

import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.EventMetadata;
import javax.inject.Inject;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;

import de.muenchen.formeditor.beans.ErrorMessage;
import de.muenchen.formeditor.beans.FormScope;
import de.muenchen.formeditor.beans.Update;
import de.muenchen.formeditor.form.IForm;

@FormScope
public class FormfieldEditorController
{
  @Inject
  private Event<ErrorMessage> showError;
  
  @Inject
  private CamelContext context;
  
  protected void onFormUpdated(@Observes @Update IForm form, EventMetadata meta)
  {
    try
    {
      ProducerTemplate producerTemplate = context.createProducerTemplate();
      Object o = producerTemplate.requestBody("direct:read-config", form);
      System.out.println(o);
      
      o = producerTemplate.requestBody("direct:write-config", o, String.class);
      System.out.println(o);
            
    } catch (Exception e)
    {
      showError.fire(new ErrorMessage(e.getLocalizedMessage(), e));
    }
  }
}
