package de.muenchen.formeditor.views;

import java.util.ResourceBundle;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import org.jacpfx.api.annotations.lifecycle.PostConstruct;
import org.jacpfx.api.annotations.perspective.Perspective;
import org.jacpfx.api.message.Message;
import org.jacpfx.rcp.componentLayout.FXComponentLayout;
import org.jacpfx.rcp.componentLayout.PerspectiveLayout;
import org.jacpfx.rcp.perspective.FXPerspective;
import org.jacpfx.rcp.util.FXUtil;

@Perspective(id="formbuilderperspective", name = "Form Builder", 
viewLocation = "/fxml/Formbuilder.fxml", components = { "workspacecomponent", "formfieldeditorcomponent" })
public class FormBuilderPerspective implements FXPerspective
{
  @FXML
  private HBox root;
  
  @FXML
  private Pane targetlayoutleft;
  
  @FXML
  private Pane targetlayoutcenter;
  
  @FXML
  private Pane targetlayoutright;

  @Override
  public void handlePerspective(Message<Event, Object> message,
      PerspectiveLayout perspectiveLayout)
  {
    if (message.messageBodyEquals(FXUtil.MessageUtil.INIT))
    {
      perspectiveLayout.registerTargetLayoutComponent(FMWorkbench.TARGET_LAYOUT_LEFT, targetlayoutleft);
      perspectiveLayout.registerTargetLayoutComponent(FMWorkbench.TARGET_LAYOUT_CENTER, targetlayoutcenter);
      perspectiveLayout.registerTargetLayoutComponent(FMWorkbench.TARGET_LAYOUT_RIGHT, targetlayoutright);
    }
  }
  
  @PostConstruct
  public void onStartPerspective(final PerspectiveLayout perspectiveLayout, final FXComponentLayout layout,
      final ResourceBundle resourceBundle) {
  }
}
