package de.muenchen.formeditor.form.data;

import java.util.ArrayList;
import java.util.List;

public class WollMuxForm
{
  private String title;
  private List<Tab> tabs = new ArrayList<Tab>();

  public String getTitle()
  {
    return title;
  }

  public void setTitle(String title)
  {
    this.title = title;
  }

  public List<Tab> getTabs()
  {
    return tabs;
  }

  public void setTabs(List<Tab> tabs)
  {
    this.tabs = tabs;
  }
  
  public void addTab(Tab tab)
  {
    tabs.add(tab);
  }
  
  public void removeTab(Tab tab)
  {
    tabs.remove(tab);
  }
}
