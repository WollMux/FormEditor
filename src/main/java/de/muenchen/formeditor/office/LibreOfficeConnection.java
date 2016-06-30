package de.muenchen.formeditor.office;

import java.io.File;
import java.net.MalformedURLException;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import net.jhorstmann.i18n.I18N;

import org.slf4j.Logger;

import com.sun.star.frame.TerminationVetoException;
import com.sun.star.frame.XTerminateListener;
import com.sun.star.io.IOException;
import com.sun.star.lang.EventObject;
import com.sun.star.lang.IllegalArgumentException;
import com.sun.star.text.XTextDocument;

import de.muenchen.allg.afid.UNO;
import de.muenchen.allg.itd51.wollmux.core.document.RDFMetadataNotSupportedException;
import de.muenchen.allg.itd51.wollmux.core.document.TextDocumentModel;
import de.muenchen.allg.itd51.wollmux.core.document.TransitionModeDataContainer;
import de.muenchen.formeditor.beans.ErrorMessage;
import de.muenchen.formeditor.exceptions.LoadDocumentException;

public class LibreOfficeConnection implements IOfficeConnection
{
  @Inject
  private Logger log;
  
  @Inject
  private Event<ErrorMessage> showError; 
  
  private boolean connected = false;
  
  @PostConstruct
  @Override
  public void init()
  {
    connect();
  }

  @Override
  public boolean isConnected()
  {
    return connected;
  }

  @Override
  public void connect()
  {
    try
    {
      UNO.init();
      connected = true;
      
      UNO.desktop.addTerminateListener(new XTerminateListener()
      {
        @Override
        public void disposing(EventObject event)
        {
        }
        
        @Override
        public void queryTermination(EventObject event)
            throws TerminationVetoException
        {
        }
        
        @Override
        public void notifyTermination(EventObject event)
        {
          connected = false;
        }
      });
    } catch (Exception e)
    {
      String msg = I18N.tr("Fehler bei der Verbindung mit Office.");
      log.error(msg, e);
      showError.fire(new ErrorMessage(msg, e));
      connected = false;
    }
  }

  /**
   * Öffnet eine Datei in LibreOffice und erzeugt ein {@link TextDocumentModel}
   * für diese Datei. 
   */
  @Override
  public TextDocumentModel load(File file, boolean asTemplate)
  {
    try
    {
      String url = UNO.getParsedUNOUrl(file.toURI().toURL()
	  .toExternalForm()).Complete;
      XTextDocument doc = UNO.XTextDocument(UNO.loadComponentFromURL(url, asTemplate, false));
      TransitionModeDataContainer persistentDataContainer = new TransitionModeDataContainer(doc);
      return new TextDocumentModel(doc, persistentDataContainer);
    } catch (IllegalArgumentException | IOException
	| RDFMetadataNotSupportedException | MalformedURLException ex)
    {
      throw new LoadDocumentException(
	  I18N.tr("Fehler beim Laden des Formulars."), ex);
    }
  }
}
