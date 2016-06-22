package de.muenchen.formeditor;

import javafx.stage.Stage;

import org.jacpfx.api.annotations.workbench.Workbench;
import org.jacpfx.api.exceptions.AnnotationNotFoundException;
import org.jacpfx.api.exceptions.AttributeNotFoundException;
import org.jacpfx.api.exceptions.ComponentNotFoundException;
import org.jacpfx.api.fragment.Scope;
import org.jacpfx.api.launcher.Launcher;
import org.jacpfx.minimal.launcher.AMinimalLauncher;
import org.jacpfx.minimal.launcher.ApplicationContext;
import org.jacpfx.rcp.workbench.EmbeddedFXWorkbench;
import org.jacpfx.rcp.workbench.FXWorkbench;

import de.muenchen.formeditor.views.FMWorkbench;

public class Application extends AMinimalLauncher
{
  @Override
  protected Class<? extends FXWorkbench> getWorkbenchClass()
  {
    return FMWorkbench.class;
  }

  @Override
  protected String[] getBasePackages()
  {
    return new String[] { "de.muenchen.formeditor.beans",
	"de.muenchen.formeditor.views" };
  }

  @Override
  protected void postInit(Stage stage)
  {
  }

  @Override
  public void start(Stage stage) throws Exception
  {
    initExceptionHandler();
    scanPackegesAndInitRegestry();

    final Launcher<ApplicationContext> launcher = new WeldLauncher();
    final Class<? extends FXWorkbench> workbenchHandler = getWorkbenchClass();
    if (workbenchHandler == null)
      throw new ComponentNotFoundException("no FXWorkbench class defined");
    initWorkbench(stage, launcher, workbenchHandler);
  }

  private void initWorkbench(final Stage stage,
      final Launcher<ApplicationContext> launcher,
      final Class<? extends FXWorkbench> workbenchHandler)
  {
    if (workbenchHandler.isAnnotationPresent(Workbench.class))
    {
      this.workbench = createWorkbench(launcher, workbenchHandler);
      workbench.init(launcher, stage);
      postInit(stage);
    } else
    {
      throw new AnnotationNotFoundException(
	  "no @Workbench annotation found on class");
    }
  }

  private EmbeddedFXWorkbench createWorkbench(
      final Launcher<ApplicationContext> launcher,
      final Class<? extends FXWorkbench> workbenchHandler)
  {
    final Workbench annotation = workbenchHandler
	.getAnnotation(Workbench.class);
    final String id = annotation.id();
    if (id.isEmpty())
      throw new AttributeNotFoundException("no workbench id found for: "
	  + workbenchHandler);
    final FXWorkbench handler = launcher.registerAndGetBean(workbenchHandler,
	id, Scope.SINGLETON);
    return new EmbeddedFXWorkbench(handler, getWorkbenchDecorator());
  }
}
