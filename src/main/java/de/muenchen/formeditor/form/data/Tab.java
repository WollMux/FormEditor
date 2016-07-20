package de.muenchen.formeditor.form.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Tab
{
  private String name;
  private String title;
  private String closeaction;
  private String tip;
  private String hotkey;
  
  private List<Input> inputFields = new ArrayList<>();

  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public String getTitle()
  {
    return title;
  }

  public void setTitle(String title)
  {
    this.title = title;
  }

  public String getCloseaction()
  {
    return closeaction;
  }

  public void setCloseaction(String closeaction)
  {
    this.closeaction = closeaction;
  }

  public String getTip()
  {
    return tip;
  }

  public void setTip(String tip)
  {
    this.tip = tip;
  }

  public String getHotkey()
  {
    return hotkey;
  }

  public void setHotkey(String hotkey)
  {
    this.hotkey = hotkey;
  }
  
  public void addInputField(Input input)
  {
    inputFields.add(input);
  }
  
  public void addInputFields(List<Input> input)
  {
    inputFields.addAll(input);
  }

  public void removeInputField(Input input)
  {
    inputFields.remove(input);
  }
  
  public List<Input> getInputFields()
  {
    return Collections.unmodifiableList(inputFields);
  }
}
