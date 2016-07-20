package de.muenchen.formeditor.views;

import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.TextFieldTreeTableCell;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

import javax.inject.Inject;

import org.jacpfx.api.annotations.component.DeclarativeView;
import org.jacpfx.api.annotations.lifecycle.PostConstruct;
import org.jacpfx.api.message.Message;
import org.jacpfx.rcp.component.FXComponent;
import org.jacpfx.rcp.componentLayout.FXComponentLayout;

import de.muenchen.formeditor.form.data.Input;
import de.muenchen.formeditor.form.data.WollMuxForm;
import de.muenchen.formeditor.viewmodels.FormfieldEditorViewModel;

@DeclarativeView(id = "formfieldeditorcomponent", name = "FormfieldEditor", active = true, initialTargetLayoutId = FMWorkbench.TARGET_LAYOUT_CENTER, viewLocation = "/fxml/FormfieldEditor.fxml")
public class FormfieldEditorComponent implements FXComponent
{
  @FXML
  private TabPane tabPane;

  @Inject
  private FormfieldEditorViewModel model;

  @PostConstruct
  public void onStartComponent(final FXComponentLayout layout,
      final ResourceBundle resourceBundle)
  {
    model.formDataProperty().addListener(
	(ChangeListener<WollMuxForm>) (observable, oldValue, newValue) -> {
	  updateGUI(newValue);
	});
  }

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

  private void updateGUI(WollMuxForm formData)
  {
    tabPane.getTabs().clear();
    formData
	.getTabs()
	.stream()
	.forEach(
	    tab -> {
	      Tab t = new Tab(tab.getTitle());
	      HBox content = new HBox();
	      TreeTableView<TableViewModel> tableView = new TreeTableView<TableViewModel>();
	      tableView.setEditable(true);
	      HBox.setHgrow(tableView, Priority.ALWAYS);

	      TreeTableColumn<TableViewModel, String> colId = new TreeTableColumn<>(
		  "Id");
	      colId.setCellValueFactory(new TreeItemPropertyValueFactory<>("id"));
	      colId.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn());

	      TreeTableColumn<TableViewModel, String> colLabel = new TreeTableColumn<>(
		  "Label");
	      colLabel.setCellValueFactory(new TreeItemPropertyValueFactory<>("label"));
	      colLabel.setCellFactory(TextAreaTableCell.forTreeTableColumn());

	      TreeTableColumn<TableViewModel, String> colFeldtyp = new TreeTableColumn<>(
		  "Feldtyp");
	      colFeldtyp.setCellValueFactory(new TreeItemPropertyValueFactory<>(
		  "feldtyp"));
	      colFeldtyp.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn());

	      tableView.getColumns().setAll(colId, colLabel, colFeldtyp);
	      tableView
		  .setColumnResizePolicy(TreeTableView.CONSTRAINED_RESIZE_POLICY);

	      content.getChildren().add(tableView);

	      List<TreeItem<TableViewModel>> model = tab.getInputFields().stream()
		  .map(x -> {
		    TreeItem<TableViewModel> ti = new TreeItem<>();
		    TableViewModel tvm = new TableViewModel();
		    tvm.setId(x.getId());
		    tvm.setLabel(x.getLabel());
		    tvm.setFeldtyp(x.getType());
		    tvm.setInputField(x);
		    ti.setValue(tvm);
		    ti.getChildren().add(new TreeItem<FormfieldEditorComponent.TableViewModel>());
		    return ti;
		  }).collect(Collectors.toList());
	      TreeItem<TableViewModel> root = new TreeItem<>();
	      root.getChildren().addAll(model);
	      tableView.setShowRoot(false);
	      tableView.setRoot(root);

//	      tableView.setItems(FXCollections.observableArrayList(model));

	      t.setContent(content);

	      tabPane.getTabs().add(t);
	    });
  }

  public class TableViewModel
  {
    private StringProperty id = new SimpleStringProperty(this, "id");
    private StringProperty label = new SimpleStringProperty(this, "label");
    private StringProperty feldtyp = new SimpleStringProperty(this, "feldtyp");
    private Input inputField;

    public String getId()
    {
      return id.get();
    }

    public void setId(String value)
    {
      id.set(value);
    }

    public String getLabel()
    {
      return label.get();
    }

    public void setLabel(String value)
    {
      label.set(value);
    }

    public String getFeldtyp()
    {
      return feldtyp.get();
    }

    public void setFeldtyp(String value)
    {
      feldtyp.set(value);
    }

    public Input getInputField()
    {
      return inputField;
    }

    public void setInputField(Input inputField)
    {
      this.inputField = inputField;
    }

    public StringProperty IdProperty()
    {
      return id;
    }

    public StringProperty LabelProperty()
    {
      return label;
    }

    public StringProperty FeldtypProperty()
    {
      return feldtyp;
    }
  }
}
