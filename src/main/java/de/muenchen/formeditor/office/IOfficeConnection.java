package de.muenchen.formeditor.office;

import java.io.File;

import javax.enterprise.context.ApplicationScoped;

import org.jacpfx.api.annotations.lifecycle.PostConstruct;

import de.muenchen.allg.itd51.wollmux.core.document.TextDocumentModel;

@ApplicationScoped
public interface IOfficeConnection
{
  @PostConstruct
  public void init();
  public boolean isConnected();
  public void connect();
  public TextDocumentModel load(File file);
}
