package de.muenchen.formeditor.form;

import java.io.File;

import javax.enterprise.event.Event;
import javax.inject.Inject;

import net.jhorstmann.i18n.I18N;

import com.sun.star.lang.EventObject;
import com.sun.star.util.CloseVetoException;
import com.sun.star.util.XCloseListener;
import com.sun.star.util.XCloseable;

import de.muenchen.allg.afid.UNO;
import de.muenchen.allg.itd51.wollmux.core.document.PersistentDataContainer;
import de.muenchen.allg.itd51.wollmux.core.document.TextDocumentModel;
import de.muenchen.formeditor.beans.Closed;
import de.muenchen.formeditor.beans.ErrorMessage;
import de.muenchen.formeditor.beans.Form;
import de.muenchen.formeditor.exceptions.LoadDocumentException;
import de.muenchen.formeditor.office.IOfficeConnection;

/**
 * WollMux-Formular, das auf einer Writer-Vorlage (*.ott) basiert.
 *
 */
@Form("application/vnd.oasis.opendocument.text-template")
public class OTTForm implements IForm
{
  @Inject @Closed
  private Event<IForm> closing;
  
  @Inject
  private IOfficeConnection officeConnection;
  
  @Inject
  private Event<ErrorMessage> showError; 
  
  private PersistentDataContainer persistentDataContainer;
  private TextDocumentModel textDocumentModel;
  private File file;

  public OTTForm()
  {

  }

  /**
   * 
   * @throws LoadDocumentException
   */
  @Override
  public void load(File file)
  {
    this.file = file;
    this.textDocumentModel = officeConnection.load(file);
    this.persistentDataContainer = textDocumentModel.getPersistentData();
    XCloseable closeable = UNO.XCloseable(textDocumentModel.doc);
    closeable.addCloseListener(new XCloseListener()
    {
      @Override
      public void disposing(EventObject event)
      {
      }
      
      @Override
      public void queryClosing(EventObject evnt, boolean getsOwnership)
          throws CloseVetoException
      {
      }
      
      @Override
      public void notifyClosing(EventObject event)
      {
	closing.fire(OTTForm.this);
      }
    });
  }
  
  @Override
  public void close()
  {
    try
    {
      UNO.XCloseable(textDocumentModel.doc).close(false);
    } catch (CloseVetoException e)
    {
      showError.fire(new ErrorMessage(I18N.tr("Die Datei kann zur Zeit nicht geschlossen werden."), e));
    }
  }

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
