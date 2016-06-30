package de.muenchen.formeditor.form;

import java.io.File;

import javax.enterprise.context.Dependent;

import org.w3c.dom.Document;

import de.muenchen.allg.itd51.wollmux.core.document.PersistentDataContainer;
import de.muenchen.allg.itd51.wollmux.core.document.TextDocumentModel;
import de.muenchen.formeditor.exceptions.FormException;

/**
 * Gemeinsames Interface für alle Formularklassen. Ein Formular ist in diesem
 * Zusammenhang eine Datei, die eine Formularbeschreibung für den WollMux
 * enthält. 
 */
@Dependent
public interface IForm
{
  /**
   * Lädt eine Datei in der dazugehörigen Office-Software.
   * 
   * @param file Datei, die geöffnet werden soll.
   */
  public void load(File file);
  public File getFile();
  public TextDocumentModel getDocumentModel();
  public PersistentDataContainer getPersistentDataContainer();
  public Document getPersistentDataAsXML() throws FormException;
  public void setPersistentDataFromXML(Document doc) throws FormException;
  public void close();
}
