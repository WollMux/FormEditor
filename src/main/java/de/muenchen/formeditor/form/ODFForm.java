package de.muenchen.formeditor.form;

import java.io.File;
import java.io.IOException;

import javax.enterprise.event.Event;
import javax.inject.Inject;

import net.jhorstmann.i18n.I18N;

import org.apache.commons.io.IOUtils;
import org.w3c.dom.Document;

import com.sun.star.lang.EventObject;
import com.sun.star.util.CloseVetoException;
import com.sun.star.util.XCloseListener;
import com.sun.star.util.XCloseable;

import de.muenchen.allg.afid.UNO;
import de.muenchen.allg.itd51.wollmux.core.document.PersistentDataContainer.DataID;
import de.muenchen.allg.itd51.wollmux.core.parser.generator.xml.ConfGenerator;
import de.muenchen.allg.itd51.wollmux.core.parser.generator.xml.XMLGenerator;
import de.muenchen.allg.itd51.wollmux.core.parser.generator.xml.XMLGeneratorException;
import de.muenchen.formeditor.beans.Closed;
import de.muenchen.formeditor.beans.ErrorMessage;
import de.muenchen.formeditor.exceptions.FormException;
import de.muenchen.formeditor.exceptions.LoadDocumentException;
import de.muenchen.formeditor.office.IOfficeConnection;

public abstract class ODFForm extends BaseForm
{
  @Inject @Closed
  private Event<IForm> closing;
  
  @Inject
  private IOfficeConnection officeConnection;
  
  @Inject
  private Event<ErrorMessage> showError; 
  
  public ODFForm()
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
    this.textDocumentModel = officeConnection.load(file, false);
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
	closing.fire(ODFForm.this);
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
  public Document getPersistentDataAsXML() throws FormException
  {
    try
    {
      XMLGenerator gen = new XMLGenerator(IOUtils.toInputStream(
	  persistentDataContainer.getData(DataID.FORMULARBESCHREIBUNG), "UTF-8"));
      return gen.generateXML();
    } catch (XMLGeneratorException | IOException e)
    {
      throw new FormException(e);
    }
  }

  @Override
  public void setPersistentDataFromXML(Document doc) throws FormException
  {
    try
    {
      ConfGenerator gen = new ConfGenerator(doc);
      gen.generateConf();
      persistentDataContainer.setData(DataID.FORMULARBESCHREIBUNG, "");
    } catch (XMLGeneratorException e)
    {
      throw new FormException(e);
    }
  }
}
