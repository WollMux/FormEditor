package de.muenchen.formeditor.views;

import java.io.File;

import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.enterprise.event.Observes;
import javax.inject.Inject;

import net.jhorstmann.i18n.I18N;

import org.jacpfx.api.annotations.Resource;
import org.jacpfx.api.annotations.workbench.Workbench;
import org.jacpfx.api.componentLayout.WorkbenchLayout;
import org.jacpfx.api.message.Message;
import org.jacpfx.api.util.ToolbarPosition;
import org.jacpfx.controls.optionPane.JACPDialogButton;
import org.jacpfx.controls.optionPane.JACPDialogUtil;
import org.jacpfx.controls.optionPane.JACPOptionPane;
import org.jacpfx.rcp.componentLayout.FXComponentLayout;
import org.jacpfx.rcp.components.menuBar.JACPMenuBar;
import org.jacpfx.rcp.components.modalDialog.JACPModalDialog;
import org.jacpfx.rcp.components.toolBar.JACPToolBar;
import org.jacpfx.rcp.context.Context;
import org.jacpfx.rcp.workbench.FXWorkbench;
import org.jboss.weld.bootstrap.api.helpers.RegistrySingletonProvider;
import org.jboss.weld.environment.se.events.ContainerShutdown;

import de.muenchen.formeditor.beans.ErrorMessage;
import de.muenchen.formeditor.beans.Loaded;
import de.muenchen.formeditor.controller.FMWorkbenchController;

@Workbench(id = "fmworkbench", name = "FormularMax", perspectives = { "codeeditorperspective", "formbuilderperspective" })
public class FMWorkbench implements FXWorkbench
{ 
  private static final String TITLE = I18N.tr("Formulareditor");
  public final static String TARGET_LAYOUT_LEFT = "targetlayoutleft"; 
  public final static String TARGET_LAYOUT_CENTER = "targetlayoutcenter"; 
  public final static String TARGET_LAYOUT_RIGHT = "targetlayoutright";
  
  @Resource
  private Context context;

  @Inject
  private javax.enterprise.event.Event<ContainerShutdown> shutdownEvent;
  
  @Inject @Loaded
  private javax.enterprise.event.Event<File> loadedEvent;

  @Inject
  private FMWorkbenchController controller;
  
  private Stage stage;

  @Override
  public void handleInitialLayout(Message<Event, Object> action,
      WorkbenchLayout<Node> layout, Stage stage)
  {
    this.stage = stage;
    stage.setTitle(TITLE);
    layout.setWorkbenchXYSize(1024, 768);
    layout.registerToolBar(ToolbarPosition.NORTH);
    layout.setStyle(StageStyle.DECORATED);
    layout.setMenuEnabled(true);

    stage.setOnCloseRequest(event -> {
      closeWindow();
    });
  }

  @Override
  public void postHandle(FXComponentLayout layout)
  {
    JACPMenuBar menuBar = layout.getMenu();
    Menu menuFile = new Menu(I18N.tr("Datei"));

    MenuItem miOpen = new MenuItem(I18N.tr("Öffnen"));
    miOpen.setAccelerator(new KeyCodeCombination(KeyCode.O,
	KeyCombination.CONTROL_DOWN));
    miOpen.setOnAction(event -> {
      open();
    });

    MenuItem miExit = new MenuItem(I18N.tr("Beenden"));
    miExit.setAccelerator(new KeyCodeCombination(KeyCode.Q,
	KeyCombination.CONTROL_DOWN));
    miExit.setOnAction(event -> {
      closeWindow();
    });

    menuFile.getItems().addAll(miOpen, miExit);

    menuBar.getMenus().add(menuFile);
    
    JACPToolBar toolBar = layout.getRegisteredToolBar(ToolbarPosition.NORTH);
    
    ToggleGroup tg = new ToggleGroup();
    RadioButton tb1 = new RadioButton("Formular");
    tb1.getStyleClass().remove("radio-button");
    tb1.getStyleClass().add("toggle-button");
    tb1.setSelected(true);
    tb1.setToggleGroup(tg);
    tb1.setOnAction(event -> { context.send("formbuilderperspective", null); });

    RadioButton tb2 = new RadioButton("Code");
    tb2.getStyleClass().remove("radio-button");
    tb2.getStyleClass().add("toggle-button");
    tb2.setToggleGroup(tg);
    tb2.setOnAction(event -> { context.send("codeeditorperspective", null); });

    toolBar.addAllOnEnd(tb1, tb2);
  }
  
  public void showError(@Observes ErrorMessage msg)
  {
    JACPOptionPane pane = JACPDialogUtil.createOptionPane(TITLE, msg.getMessage());
    pane.setOnOkAction(action -> {});
    pane.setDefaultButton(JACPDialogButton.OK);
    JACPModalDialog.getInstance().showModalDialog(pane);
  }

  private void open()
  {
    FileChooser fc = new FileChooser();
    fc.getExtensionFilters().addAll(
	new ExtensionFilter(I18N.tr("LibreOffice Writer"), "*.ott", "*.odt"),
	new ExtensionFilter(I18N.tr("Alle Dateien"), "*.*"));
    File file = fc.showOpenDialog(stage);
    if (file != null)
    {
      loadedEvent.fire(file);
      stage.setTitle(String.format("%s - %s", TITLE, file.getName()));
    }
  }

  private void closeWindow()
  {
    shutdownEvent.fire(new ContainerShutdown(
        RegistrySingletonProvider.STATIC_INSTANCE));
  }
}
