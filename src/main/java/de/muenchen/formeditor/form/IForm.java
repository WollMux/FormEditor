package de.muenchen.formeditor.form;

import java.io.File;

import javax.enterprise.context.Dependent;

import de.muenchen.allg.itd51.wollmux.core.document.PersistentDataContainer;
import de.muenchen.allg.itd51.wollmux.core.document.TextDocumentModel;

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
  public void close();
}
