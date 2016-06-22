package de.muenchen.formeditor.beans;

import java.io.Serializable;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.BeforeBeanDiscovery;
import javax.enterprise.inject.spi.Extension;

public class FormScopeExtension implements Extension, Serializable
{
  private static final long serialVersionUID = -8871364646909018382L;
  
  public void addScope(@Observes final BeforeBeanDiscovery event)
  {
    event.addScope(FormScope.class, true, false);
  }

  public void registerContext(@Observes final AfterBeanDiscovery event)
  {
    event.addContext(new FormScopeContext());
  }
}
