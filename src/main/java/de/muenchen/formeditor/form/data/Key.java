package de.muenchen.formeditor.form.data;

public class Key implements Element
{
  private String id;
  private Element content;

  public String getId()
  {
    return id;
  }

  public void setId(String id)
  {
    this.id = id;
  }

  public Element getContent()
  {
    return content;
  }
  
  public <T> T getContent(Class<T> type)
  {
    return (T)content;
  }

  public void setContent(Element content)
  {
    this.content = content;
  }

  @Override
  public void setValue(String name, String elem)
  {
    content.setValue(name, elem);
  }

  @Override
  public String getValue(String name)
  {
    return content.getValue(name);
  }
}
