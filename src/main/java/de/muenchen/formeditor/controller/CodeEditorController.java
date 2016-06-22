package de.muenchen.formeditor.controller;

import javafx.beans.InvalidationListener;
import javafx.beans.property.StringProperty;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.EventMetadata;
import javax.inject.Inject;

import de.muenchen.allg.itd51.wollmux.core.document.PersistentDataContainer.DataID;
import de.muenchen.formeditor.beans.Closed;
import de.muenchen.formeditor.beans.FormScope;
import de.muenchen.formeditor.beans.Update;
import de.muenchen.formeditor.form.IForm;
import de.muenchen.formeditor.viewmodels.CodeEditorViewModel;
import de.muenchen.formeditor.views.CodeEditorComponent;

/**
 * Controller für {@link CodeEditorComponent}.
 * 
 * Die Klasse ist hauptsächlich dafür verantwortlich die Formularbeschreibung
 * zum Bearbeiten auszulesen und anschließend wieder in das Formular 
 * zurückzuschreiben. 
 * 
 */
@FormScope
public class CodeEditorController
{
  @Inject
  private CodeEditorViewModel viewModel;

  @Inject @Update
  private Event<IForm> formUpdated;

  private IForm form;

  @PostConstruct
  private void init()
  {
    viewModel.codeProperty().addListener((InvalidationListener) observable -> {
      StringProperty prop = (StringProperty)observable;
      // Die Formularbeschreibung wird nur geschrieben, wenn sich etwas geändert
      // hat.
      if (form != null
	&& !prop.getValue().equals(form.getPersistentDataContainer().getData(
		  DataID.FORMULARBESCHREIBUNG)))
	  {
	    form.getPersistentDataContainer().setData(
		DataID.FORMULARBESCHREIBUNG, prop.getValue());
	    formUpdated.fire(form);
	  }
    });
  }

  protected void onFormUpdated(@Observes @Update IForm form, EventMetadata meta)
  {
    // Ignoriere Events, die der Controller selbst erzeugt hat.
    if (meta.getInjectionPoint().getBean().getBeanClass() != getClass())
    {
      this.form = form;
      if (viewModel != null)
      {
        String formDesc = form.getPersistentDataContainer().getData(
  	  DataID.FORMULARBESCHREIBUNG);
        viewModel.codeProperty().set(formDesc);
      }
    }
  }

  protected void onClose(@Observes @Closed IForm form)
  {
    viewModel.codeProperty().set("");
    this.form = null;
  }
}
