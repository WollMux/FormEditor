package de.muenchen.formeditor.beans;

import java.io.Serializable;
import java.lang.annotation.Annotation;

import javax.enterprise.context.spi.Context;
import javax.enterprise.context.spi.Contextual;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;

import de.muenchen.formeditor.beans.FormScopeContextHolder.FormScopeInstance;

public class FormScopeContext implements Context, Serializable
{
  private static final long serialVersionUID = -6668745429870460124L;

  private FormScopeContextHolder contextHolder = new FormScopeContextHolder();

  @Override
  public Class<? extends Annotation> getScope()
  {
    return FormScope.class;
  }

  @Override
  public <T> T get(Contextual<T> contextual,
      CreationalContext<T> creationalContext)
  {
    Bean<T> bean = (Bean<T>) contextual;
    if (contextHolder.getBeans().containsKey(bean.getBeanClass()))
    {
      return (T) contextHolder.getBean(bean.getBeanClass()).instance;
    } else
    {
      T t = bean.create(creationalContext);
      FormScopeInstance<T> customInstance = new FormScopeInstance<T>();
      customInstance.bean = bean;
      customInstance.ctx = creationalContext;
      customInstance.instance = t;
      contextHolder.putBean(customInstance);
      return t;
    }
  }

  @Override
  public <T> T get(Contextual<T> contextual)
  {
    Bean<T> bean = (Bean<T>) contextual;
    if (contextHolder.getBeans().containsKey(bean.getBeanClass()))
    {
      return (T) contextHolder.getBean(bean.getBeanClass()).instance;
    } else
    {
      return null;
    }
  }

  @Override
  public boolean isActive()
  {
    return true;
  }
  
  public void clear()
  {
    contextHolder.clear();
  }
}
