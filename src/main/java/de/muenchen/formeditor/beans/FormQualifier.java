package de.muenchen.formeditor.beans;

import javax.enterprise.util.AnnotationLiteral;

/**
 * Der Qualifier wird zum programmatischen Injizieren von Objekten benötigt,
 * die die {@link Form}-Annotation benutzen. 
 */
public class FormQualifier extends AnnotationLiteral<Form> implements Form
{
  private static final long serialVersionUID = 1L;
  private String value;

  public FormQualifier(String value)
  {
    this.value = value;
  }

  @Override
  public String value()
  {
    return value;
  }

}
