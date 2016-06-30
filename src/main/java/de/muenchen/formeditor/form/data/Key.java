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
  public void setContent(Element content)
  {
    this.content = content;
  }
}
