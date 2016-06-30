package de.muenchen.formeditor.form;

import java.io.File;

import de.muenchen.allg.itd51.wollmux.core.document.PersistentDataContainer;
import de.muenchen.allg.itd51.wollmux.core.document.TextDocumentModel;

public abstract class BaseForm implements IForm
{
  protected PersistentDataContainer persistentDataContainer;
  protected TextDocumentModel textDocumentModel;
  protected File file;

  @Override
  public TextDocumentModel getDocumentModel()
  {
    return textDocumentModel;
  }

  @Override
  public PersistentDataContainer getPersistentDataContainer()
  {
    return persistentDataContainer;
  }

  @Override
  public File getFile()
  {
    return file;
  } 
}
