package de.muenchen.formeditor.views;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Cell;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Text;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javafx.util.converter.DefaultStringConverter;

public class TextAreaTableCell<S, T> extends TreeTableCell<S, T>
{
  public static <S> Callback<TreeTableColumn<S, String>, TreeTableCell<S, String>> forTreeTableColumn()
  {
    return forTreeTableColumn(new DefaultStringConverter());
  }

  public static <S, T> Callback<TreeTableColumn<S, T>, TreeTableCell<S, T>> forTreeTableColumn(
      final StringConverter<T> converter)
  {
    return list -> new TextAreaTableCell<>(converter);
  }

  private static <T> String getItemText(Cell<T> cell,
      StringConverter<T> converter)
  {
    return converter == null ? cell.getItem() == null ? "" : cell.getItem()
	.toString() : converter.toString(cell.getItem());
  }

  private static <T> TextArea createTextArea(final Cell<T> cell,
      final StringConverter<T> converter)
  {
    TextArea textArea = new TextArea(getItemText(cell, converter));
    textArea
	.setOnKeyReleased(t -> {
	  if (t.getCode() == KeyCode.ESCAPE)
	  {
	    cell.cancelEdit();
	    t.consume();
	  } else if (t.getCode() == KeyCode.ENTER && t.isShiftDown())
	  {
	    t.consume();
	    textArea.appendText("\n");
	  } else if (t.getCode() == KeyCode.ENTER)
	  {
	    if (converter == null)
	    {
	      throw new IllegalStateException(
		  "Attempting to convert text input into Object, but provided "
		      + "StringConverter is null. Be sure to set a StringConverter "
		      + "in your cell factory.");
	    }
	    cell.commitEdit(converter.fromString(textArea.getText()));
	    t.consume();
	  }
	});
    textArea.prefRowCountProperty().bind(
	Bindings.size(textArea.getParagraphs()));
    return textArea;
  }

  private void startEdit(final Cell<T> cell, final StringConverter<T> converter)
  {
    textArea.setText(getItemText(cell, converter));

    cell.setText(null);
    cell.setGraphic(textArea);

    textArea.selectAll();
    textArea.requestFocus();
  }

  private static <T> void cancelEdit(Cell<T> cell,
      final StringConverter<T> converter)
  {
    cell.setText(getItemText(cell, converter));
    cell.setGraphic(null);
  }

  private void updateItem(final Cell<T> cell, final StringConverter<T> converter)
  {
    cell.setPrefHeight(USE_COMPUTED_SIZE);

    if (cell.isEmpty())
    {
      cell.setText(null);
      cell.setGraphic(null);

    } else
    {
      if (cell.isEditing())
      {
	if (textArea != null)
	{
	  textArea.setText(getItemText(cell, converter));
	  textArea.setPrefHeight(USE_COMPUTED_SIZE);
	}
	cell.setText(null);
	cell.setGraphic(textArea);
      } else
      {
	Text text = new Text(getItemText(cell, converter));
        text.wrappingWidthProperty().bind(cell.widthProperty());
        cell.setGraphic(text);
        
//        cell.setText(getItemText(cell, converter));
//	cell.setGraphic(null);
      }
    }
  }

  private TextArea textArea;
  private ObjectProperty<StringConverter<T>> converter = new SimpleObjectProperty<>(
      this, "converter");

  public TextAreaTableCell()
  {
    this(null);
  }

  public TextAreaTableCell(StringConverter<T> converter)
  {
    this.getStyleClass().add("text-area-table-cell");
    setConverter(converter);
  }

  public final ObjectProperty<StringConverter<T>> converterProperty()
  {
    return converter;
  }

  public final void setConverter(StringConverter<T> value)
  {
    converterProperty().set(value);
  }

  public final StringConverter<T> getConverter()
  {
    return converterProperty().get();
  }

  @Override
  public void startEdit()
  {
    if (!isEditable() || !getTreeTableView().isEditable()
	|| !getTableColumn().isEditable())
    {
      return;
    }

    super.startEdit();

    if (isEditing())
    {
      if (textArea == null)
      {
	textArea = createTextArea(this, getConverter());
      }

      startEdit(this, getConverter());
    }
  }

  @Override
  public void cancelEdit()
  {
    super.cancelEdit();
    cancelEdit(this, getConverter());
  }

  @Override
  public void updateItem(T item, boolean empty)
  {
    super.updateItem(item, empty);
    updateItem(this, getConverter());
  }

}