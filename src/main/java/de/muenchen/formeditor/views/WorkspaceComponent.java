package de.muenchen.formeditor.views;

import javafx.event.Event;
import javafx.scene.Node;

import org.jacpfx.api.annotations.Resource;
import org.jacpfx.api.annotations.component.DeclarativeView;
import org.jacpfx.api.message.Message;
import org.jacpfx.rcp.component.FXComponent;
import org.jacpfx.rcp.context.Context;

@DeclarativeView(id = "workspacecomponent", name = "Workspace", active = true,
initialTargetLayoutId = FMWorkbench.TARGET_LAYOUT_LEFT, viewLocation = "/fxml/Workspace.fxml")
public class WorkspaceComponent implements FXComponent
{
  @Resource
  private Context context;

  @Override
  public Node postHandle(Node node, Message<Event, Object> message)
      throws Exception
  {
    System.out.println(context.getParentId());
    return null;
  }

  @Override
  public Node handle(Message<Event, Object> message) throws Exception
  {
    return null;
  }

}
