package de.muenchen.formeditor.viewmodels;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import javax.inject.Singleton;

import de.muenchen.formeditor.form.data.WollMuxForm;

@Singleton
public class FormfieldEditorViewModel
{
  private ObjectProperty<WollMuxForm> formData = new SimpleObjectProperty<>();
  
  public ObjectProperty<WollMuxForm> formDataProperty()
  {
    return formData;
  }
}
