package de.muenchen.formeditor.beans;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;

public class FormScopeContextHolder implements Serializable
{
  private static final long serialVersionUID = 8030397296138116598L;

  private static final FormScopeContextHolder instance = new FormScopeContextHolder();

  private Map<Class<?>, FormScopeInstance<?>> beans;

  public synchronized static FormScopeContextHolder getInstance()
  {
    return instance;
  }

  public FormScopeContextHolder()
  {
    beans = Collections
	.synchronizedMap(new HashMap<Class<?>, FormScopeInstance<?>>());
  }

  public Map<Class<?>, FormScopeInstance<?>> getBeans()
  {
    return beans;
  }

  public <T> FormScopeInstance<T> getBean(T type)
  {
    return (FormScopeInstance<T>) getBeans().get(type);
  }

  public void putBean(FormScopeInstance<?> customInstance)
  {
    getBeans().put(customInstance.bean.getBeanClass(), customInstance);
  }

  public <T> void destroyBean(FormScopeInstance<T> formScopeInstance)
  {
    getBeans().remove(formScopeInstance.bean.getBeanClass());
    formScopeInstance.bean.destroy(formScopeInstance.instance,
	formScopeInstance.ctx);
  }
  
  public void clear()
  {
    for (Entry<Class<?>, FormScopeInstance<?>> b : beans.entrySet())
    {
      destroyBean(b.getValue());
    }
    beans.clear();
  }

  public static class FormScopeInstance<T>
  {
    Bean<T> bean;
    CreationalContext<T> ctx;
    T instance;
  }
}
