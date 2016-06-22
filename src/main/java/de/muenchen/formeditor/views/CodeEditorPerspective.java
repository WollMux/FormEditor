package de.muenchen.formeditor.views;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import javax.inject.Inject;

import org.jacpfx.api.annotations.Resource;
import org.jacpfx.api.annotations.perspective.Perspective;
import org.jacpfx.api.message.Message;
import org.jacpfx.rcp.componentLayout.PerspectiveLayout;
import org.jacpfx.rcp.context.Context;
import org.jacpfx.rcp.perspective.FXPerspective;
import org.jacpfx.rcp.util.FXUtil;

import de.muenchen.formeditor.beans.Show;

@Perspective(id = "codeeditorperspective", name = "CodeEditor", viewLocation = "/fxml/codeeditor.fxml", 
	components = {"workspacecomponent", "codeeditorcomponent" })
public class CodeEditorPerspective implements FXPerspective
{
  @Resource
  private Context context;
  
  @FXML
  private HBox root;

  @FXML
  private Pane targetlayoutleft;

  @FXML
  private Pane targetlayoutcenter;
  
  @Inject @Show
  public javax.enterprise.event.Event<FXPerspective> show;

  @Override
  public void handlePerspective(Message<Event, Object> message,
      PerspectiveLayout perspectiveLayout)
  {
    if (message.messageBodyEquals(FXUtil.MessageUtil.INIT))
    {
      perspectiveLayout.registerTargetLayoutComponent(
	  FMWorkbench.TARGET_LAYOUT_LEFT, targetlayoutleft);
      perspectiveLayout.registerTargetLayoutComponent(
	  FMWorkbench.TARGET_LAYOUT_CENTER, targetlayoutcenter);
    }
  }
}
