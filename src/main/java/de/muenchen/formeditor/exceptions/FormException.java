package de.muenchen.formeditor.exceptions;

public class FormException extends Exception
{
  private static final long serialVersionUID = 1L;

  public FormException()
  {
    super();
  }

  public FormException(String message, Throwable cause)
  {
    super(message, cause);
  }

  public FormException(String message)
  {
    super(message);
  }

  public FormException(Throwable cause)
  {
    super(cause);
  }
}
