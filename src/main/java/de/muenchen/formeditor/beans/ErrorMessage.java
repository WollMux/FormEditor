package de.muenchen.formeditor.beans;

public class ErrorMessage
{
  private String msg;
  private Throwable exception;
  
  public ErrorMessage(String msg)
  {
    super();
    this.msg = msg;
  }
  public ErrorMessage(String msg, Throwable exception)
  {
    this(msg);
    this.exception = exception;
  }
  public String getMessage()
  {
    return msg;
  }
  public void setMessage(String msg)
  {
    this.msg = msg;
  }
  public Throwable getException()
  {
    return exception;
  }
  public void setException(Throwable exception)
  {
    this.exception = exception;
  }
}
