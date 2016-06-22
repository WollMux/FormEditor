package de.muenchen.formeditor.controller;

import java.io.File;
import java.io.IOException;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;

import org.slf4j.Logger;

import de.muenchen.formeditor.beans.Closed;
import de.muenchen.formeditor.beans.ErrorMessage;
import de.muenchen.formeditor.beans.FormScope;
import de.muenchen.formeditor.beans.FormScopeContext;
import de.muenchen.formeditor.beans.Loaded;
import de.muenchen.formeditor.beans.Update;
import de.muenchen.formeditor.exceptions.LoadDocumentException;
import de.muenchen.formeditor.form.FormFactory;
import de.muenchen.formeditor.form.IForm;

@ApplicationScoped
public class FMWorkbenchController
{
  @Inject
  private Logger log;
  
  @Inject
  private BeanManager beanManager;
  
  @Inject
  private Event<ErrorMessage> showError;
  
  @Inject @Update
  private Event<IForm> formUpdated;

  @Inject
  private FormFactory formFactory;

  private IForm currentForm;
  
  public void onFileLoaded(@Observes @Loaded File file)
  {
    try
    {
      if (currentForm != null)
      {
	currentForm.close();
      }
      currentForm = formFactory.getFormFromFile(file);
      formUpdated.fire(currentForm);
    } catch (IOException | LoadDocumentException e)
    {
      showError.fire(new ErrorMessage(e.getMessage(), e));
      log.error(e.getMessage(), e);
    }
  }
  
  public void onFileClosed(@Observes @Closed IForm form)
  {
    if (form == currentForm)
    {
      currentForm = null;
    }
    ((FormScopeContext) beanManager.getContext(FormScope.class)).clear();
  }
}
