package de.muenchen.formeditor.exceptions;

public class LoadDocumentException extends RuntimeException
{
  private static final long serialVersionUID = 1L;

  public LoadDocumentException()
  {
    super();
  }

  public LoadDocumentException(String message, Throwable cause,
      boolean enableSuppression, boolean writableStackTrace)
  {
    super(message, cause, enableSuppression, writableStackTrace);
  }

  public LoadDocumentException(String message, Throwable cause)
  {
    super(message, cause);
  }

  public LoadDocumentException(String message)
  {
    super(message);
  }

  public LoadDocumentException(Throwable cause)
  {
    super(cause);
  }
}
