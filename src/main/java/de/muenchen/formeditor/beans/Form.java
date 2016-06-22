package de.muenchen.formeditor.beans;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.inject.Qualifier;

import de.muenchen.formeditor.form.IForm;

/**
 * Annotation für Formularklassen, die {@link IForm} implementieren. Als
 * Parameter wird der Mimetype des Dokuments erwartet, das die Basis des
 * Formulars ist.
 * Z.B. 'application/vnd.oasis.opendocument.text-template' für ein Writer-
 * Template.   
 */
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER, ElementType.TYPE})
public @interface Form
{
  /**
   * Mimetype des Dokuments auf dem das Formular aufbaut.
   * 
   * @return
   */
  String value() default ""; 
}
