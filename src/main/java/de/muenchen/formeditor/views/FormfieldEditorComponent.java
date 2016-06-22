package de.muenchen.formeditor.views;

import javafx.event.Event;
import javafx.scene.Node;

import org.jacpfx.api.annotations.component.DeclarativeView;
import org.jacpfx.api.message.Message;
import org.jacpfx.rcp.component.FXComponent;

@DeclarativeView(id = "formfieldeditorcomponent", name = "FormfieldEditor", 
	active = true, initialTargetLayoutId = FMWorkbench.TARGET_LAYOUT_CENTER, 
	viewLocation = "/fxml/FormfieldEditor.fxml")
public class FormfieldEditorComponent implements FXComponent
{
  @Override
  public Node postHandle(Node node, Message<Event, Object> message)
      throws Exception
  {
    return null;
  }

  @Override
  public Node handle(Message<Event, Object> message) throws Exception
  {
    return null;
  }
}
