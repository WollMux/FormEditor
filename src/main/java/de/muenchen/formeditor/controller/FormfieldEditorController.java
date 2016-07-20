package de.muenchen.formeditor.controller;

import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.EventMetadata;
import javax.inject.Inject;

import ma.glasnost.orika.MapperFactory;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.slf4j.Logger;

import de.muenchen.formeditor.beans.ErrorMessage;
import de.muenchen.formeditor.beans.FormScope;
import de.muenchen.formeditor.beans.Update;
import de.muenchen.formeditor.form.IForm;
import de.muenchen.formeditor.form.data.WollMuxForm;
import de.muenchen.formeditor.viewmodels.FormfieldEditorViewModel;

@FormScope
public class FormfieldEditorController
{
  @Inject
  private Logger log;

  @Inject
  private Event<ErrorMessage> showError;

  @Inject
  private CamelContext context;

  @Inject
  private MapperFactory mapper;
  
  @Inject
  private FormfieldEditorViewModel model;

  protected void onFormUpdated(@Observes @Update IForm form, EventMetadata meta)
  {
    try
    {
      ProducerTemplate producerTemplate = context.createProducerTemplate();
      WollMuxForm formData = producerTemplate.requestBody("direct:read-config", form,
	  WollMuxForm.class);
      
      model.formDataProperty().set(formData);

      // Property titleProp = Property.Builder.propertyFor(Key.class, "title")
      // .type(String.class).getter("getValue(\"TITLE\")")
      // .setter("setValue(\"TITLE\", %s)").build();
      //
      // mapper.classMap(Key.class, WollMuxForm.class).field(titleProp, "title")
      // .field("content", "tabs").register();
      // mapper.classMap(Group.class, List.class)
      // .customize(new CustomMapper<Group, List>()
      // {
      // @Override
      // public void mapAtoB(Group a, List b, MappingContext context)
      // {
      // Key fenster = a.getChildren().stream().filter(e -> e instanceof Key)
      // .map(e -> (Key) e).filter(k -> k.getId().equals("Fenster"))
      // .findFirst().get();
      // mapper.getMapperFacade().map(fenster.getContent(), List.class);
      // }
      // }).register();
      //
      // MapperFacade facade = mapper.getMapperFacade();
      // Object sourceObject = o.getFile().getChildren().stream().findFirst()
      // .map(e -> (Key) e).get().getContent(Group.class).getChildren()
      // .stream().findFirst().get();
      // WollMuxForm wm = facade.map(sourceObject, WollMuxForm.class);
      // System.out.println(wm);

//      String xml = producerTemplate.requestBody("direct:write-config", o,
//	  String.class);
//      System.out.println(xml);
    } catch (Exception e)
    {
      showError.fire(new ErrorMessage(e.getLocalizedMessage(), e));
    }
  }
}
