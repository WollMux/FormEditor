package de.muenchen.formeditor.form.data;

import java.util.List;

public class Group implements Element
{
  private List<Element> children;

  public List<Element> getChildren()
  {
    return children;
  }

  public void setChildren(List<Element> children)
  {
    this.children = children;
  }

  public void addChild(Element element)
  {
    children.add(element);
  }
  
  public void removeChild(Element element)
  {
    children.remove(element);
  }
}
