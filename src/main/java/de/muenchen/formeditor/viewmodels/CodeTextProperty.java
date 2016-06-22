package de.muenchen.formeditor.viewmodels;

import javafx.beans.property.StringPropertyBase;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;

public class CodeTextProperty extends StringPropertyBase 
{
  private JTextComponent editor;
  private boolean doUpdate = true;

  public CodeTextProperty(JTextComponent editor)
  {
    this.editor = editor;
    
    editor.getDocument().addDocumentListener(new DocumentListener()
    {
      @Override
      public void removeUpdate(DocumentEvent e)
      {
	if (doUpdate)
	{
	  fireValueChangedEvent();
	}
	doUpdate = true;
      }

      @Override
      public void insertUpdate(DocumentEvent e)
      {
	if (doUpdate)
	{
	  fireValueChangedEvent();
	}
	doUpdate = true;
      }

      @Override
      public void changedUpdate(DocumentEvent e)
      {
      }
    });
  }
  
  @Override
  public String get()
  {
    return editor.getText();
  }

  @Override
  public void set(String newValue)
  {
    super.set(newValue);
    if (newValue != null)
    {
      doUpdate = false;
      editor.setText(newValue);
    }
  }

  @Override
  public void setValue(String v)
  {
    super.setValue(v);
    if (v != null)
    {
      doUpdate = false;
      editor.setText(v);
    }
  }

  @Override
  public String getValue()
  {
    return editor.getText();
  }

  @Override
  public Object getBean()
  {
    return null;
  }

  @Override
  public String getName()
  {
    return "CodeTextProperty";
  }

}
