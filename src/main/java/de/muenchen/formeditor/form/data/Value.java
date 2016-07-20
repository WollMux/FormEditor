package de.muenchen.formeditor.form.data;

public class Value implements Element
{
  private String text;

  public String getText()
  {
    return text;
  }

  public void setText(String text)
  {
    this.text = text;
  }

  @Override
  public void setValue(String name, String value)
  {
  }

  @Override
  public String getValue(String name)
  {
    return null;
  }
}
