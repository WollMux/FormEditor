package de.muenchen.formeditor;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.apache.camel.main.Main;
import org.jboss.weld.environment.se.events.ContainerInitialized;
import org.jboss.weld.environment.se.events.ContainerShutdown;
import org.slf4j.Logger;

import de.muenchen.formeditor.beans.Config;

@ApplicationScoped
public class FormularMaxMain
{
  @Inject
  private Logger log;
  
  @Inject
  private Main camelMain;
  
  @Inject @Config("locale")
  private String locale;
  
  public void start(@Observes ContainerInitialized event,
      @org.jboss.weld.environment.se.bindings.Parameters List<String> args)
  {
    try
    {
      camelMain.start();
      Application.launch(Application.class, args.toArray(new String[]{}));
    } catch (Exception e)
    {
      log.error(e.getMessage(), e);
    }
  }

  public void stop(@Observes ContainerShutdown event)
  {
    close();
  }

  protected void close()
  {
    try
    {
      camelMain.shutdown();
    } catch (Exception e)
    {
      log.error(e.getMessage(), e);
    }
    System.exit(0);
  }
}
