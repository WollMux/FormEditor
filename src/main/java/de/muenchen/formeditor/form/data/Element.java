package de.muenchen.formeditor.form.data;

public interface Element
{
  
  /**
   * Hilfsfunktion, die in der Group nach einem Key-Value-Paar mit einer
   * bestimmten Id sucht, Der Textinhalt des Value-Elements wird auf den 
   * übergebenen Wert gesetzt.
   * 
   * @param name
   * 	Id des {@link Key}-Elements
   * @param value
   * 	Wert, auf den das {@link Value}-Element gesetzt wird.
   */
  public abstract void setValue(String name, String value);

  /**
   * Hilfsfunktion, die in der Group nach einem Key-Value-Paar mit einer
   * bestimmten Id sucht, Der Textinhalt des Value-Elements wird zurückgegeben.
   * 
   * @param name
   * 	Id des {@link Key}-Elements
   */
  public abstract String getValue(String name);

}
