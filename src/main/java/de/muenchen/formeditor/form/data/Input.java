package de.muenchen.formeditor.form.data;

import java.util.List;

public class Input
{
  private String id;
  private String label;
  private String type;
  private String tip;
  private boolean readonly;
  private boolean edit;
  private List<String> values;
  private Function autofill;
  private Function plausi;

  public String getId()
  {
    return id;
  }

  public void setId(String id)
  {
    this.id = id;
  }

  public String getLabel()
  {
    return label;
  }

  public void setLabel(String label)
  {
    this.label = label;
  }

  public String getType()
  {
    return type;
  }

  public void setType(String type)
  {
    this.type = type;
  }

  public String getTip()
  {
    return tip;
  }

  public void setTip(String tip)
  {
    this.tip = tip;
  }

  public boolean isReadonly()
  {
    return readonly;
  }

  public void setReadonly(boolean readonly)
  {
    this.readonly = readonly;
  }

  public boolean isEdit()
  {
    return edit;
  }

  public void setEdit(boolean edit)
  {
    this.edit = edit;
  }

  public List<String> getValues()
  {
    return values;
  }

  public void setValues(List<String> values)
  {
    this.values = values;
  }

  public Function getAutofill()
  {
    return autofill;
  }

  public void setAutofill(Function autofill)
  {
    this.autofill = autofill;
  }

  public Function getPlausi()
  {
    return plausi;
  }

  public void setPlausi(Function plausi)
  {
    this.plausi = plausi;
  }
}
