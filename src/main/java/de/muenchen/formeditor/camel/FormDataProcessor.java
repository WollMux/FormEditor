package de.muenchen.formeditor.camel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.Pointer;
import org.slf4j.Logger;

import de.muenchen.formeditor.form.data.Config;
import de.muenchen.formeditor.form.data.Group;
import de.muenchen.formeditor.form.data.Input;
import de.muenchen.formeditor.form.data.Key;
import de.muenchen.formeditor.form.data.Tab;
import de.muenchen.formeditor.form.data.Value;
import de.muenchen.formeditor.form.data.WollMuxForm;

public class FormDataProcessor implements Processor
{
  @Inject
  private Logger log;
  
  @Inject
  private BeanUtilsBean beanUtils;
  
  @Override
  public void process(Exchange exchange) throws Exception
  {
    Config config = exchange.getIn().getBody(Config.class);

    JXPathContext jxPathContext = JXPathContext.newContext(config);
    jxPathContext.setLenient(true);

    WollMuxForm wm = new WollMuxForm();

    @SuppressWarnings("unchecked")
    Iterator<Pointer> tabs = jxPathContext
	.iteratePointers("/file/children/content/children/content/children[id='Fenster']/content/children");

    while (tabs.hasNext())
    {
      Tab t = new Tab();
      Pointer tab = tabs.next();
      JXPathContext relativeContext = jxPathContext.getRelativeContext(tab);

      t.setName((String) relativeContext.getValue("./id"));

      List<?> keys = (List<?>) relativeContext.getValue("./content/children");

      keys.stream().map(k -> (Key) k).forEach(k -> {
	if (k.getContent() instanceof Value)
	{
	  String id = k.getId().toLowerCase();
	  Value v = (Value) k.getContent();
	  try
	  {
	    beanUtils.setProperty(t, id, v.getText());
	  } catch (Exception e)
	  {
	    log.error(e.getMessage(), e);
	  }
	} else if (k.getId().equals("Eingabefelder"))
	{
	  List<Input> input = mapInputfields(k);
	  t.addInputFields(input);
	}
      });

      wm.addTab(t);

      exchange.getOut().setBody(wm);
    }
  }

  private List<Input> mapInputfields(Key key)
  {
    List<Input> inputs = new ArrayList<>();

    Group fields = (Group) key.getContent();
    fields.getChildren().stream().map(e -> (Group) e).forEach(g -> {
      Input input = new Input();
      
      g.getChildren().stream().map(k -> (Key) k).forEach(k -> {
	if (k.getContent() instanceof Value)
	{
	  String id = k.getId().toLowerCase();
	  Value v = (Value) k.getContent();
	  try
	  {
	    beanUtils.setProperty(input, id, v.getText());
	  } catch (Exception e)
	  {
	    log.error(e.getMessage(), e);
	  }
	}
      });
      
      inputs.add(input);
    });

    return inputs;
  }
}
