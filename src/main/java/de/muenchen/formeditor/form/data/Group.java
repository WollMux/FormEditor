package de.muenchen.formeditor.form.data;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class Group implements Element
{
  private List<Element> children = new ArrayList<>();

  public List<Element> getChildren()
  {
    return children;
  }

  public void setChildren(List<Element> children)
  {
    this.children = children;
  }

  public void addChildren(List<Element> children)
  {
    this.children.addAll(children);
  }

  public void addChild(Element element)
  {
    children.add(element);
  }

  public void removeChild(Element element)
  {
    children.remove(element);
  }

  @Override
  public String getValue(String name)
  {
    try
    {
      Value v = (Value) children
	  .stream()
	  .filter(e -> e instanceof Key)
	  .map(e -> (Key) e)
	  .filter(
	      k -> k.getId().equals(name) && k.getContent() instanceof Value)
	  .findFirst().get().getContent();
      return v.getText();
    } catch (NoSuchElementException ex)
    {
      return null;
    }
  }

  @Override
  public void setValue(String name, String value)
  {
    Value v = (Value) children.stream().filter(e -> e instanceof Key)
	.map(e -> (Key) e)
	.filter(k -> k.getId().equals(name) && k.getContent() instanceof Value)
	.findFirst().get().getContent();
    v.setText(value);
  }
}
