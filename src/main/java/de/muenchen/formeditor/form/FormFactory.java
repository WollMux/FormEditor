package de.muenchen.formeditor.form;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import de.muenchen.formeditor.beans.FormQualifier;

@Dependent
public class FormFactory
{
  @Inject
  @Any
  private Instance<IForm> formInst;

  public IForm getFormFromFile(File file) throws IOException
  {
    if (!formInst.isUnsatisfied())
    {
      String mimeType = Files.probeContentType(file.toPath());
      IForm form = formInst.select(new FormQualifier(mimeType)).get();
      form.load(file);
      return form;
    }
    return null;
  }
}
