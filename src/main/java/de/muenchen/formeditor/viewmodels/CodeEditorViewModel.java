package de.muenchen.formeditor.viewmodels;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import javax.inject.Singleton;

import de.muenchen.formeditor.controller.CodeEditorController;
import de.muenchen.formeditor.views.CodeEditorComponent;

/**
 * Die Klasse dient zum Datenaustausch zwischen {@link CodeEditorController}
 * und {@link CodeEditorComponent}. 
 * Andere Klassen, die auf die Formularbeschreibung zugreifen müssen, können sie
 * auch verwenden.
 * 
 */
@Singleton
public class CodeEditorViewModel
{
  private StringProperty code = new SimpleStringProperty();
  private BooleanProperty dirty = new SimpleBooleanProperty();
  
  public StringProperty codeProperty() {
    return code;
  }

  public BooleanProperty dirtyProperty() {
    return dirty;
  }
}
