package de.muenchen.formeditor;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.CDI;

import org.jacpfx.api.fragment.Scope;
import org.jacpfx.api.launcher.Launcher;
import org.jacpfx.minimal.launcher.ApplicationContext;

@SuppressWarnings("unchecked")
public class WeldLauncher implements Launcher<ApplicationContext>
{
  private BeanManager beanManager;

  private Map<String, Object> singletons = new ConcurrentHashMap<>();
  private Map<String, Class<?>> prototypes = new ConcurrentHashMap<>();

  public WeldLauncher()
  {
    beanManager = CDI.current().getBeanManager();
  }

  @Override
  public ApplicationContext getContext()
  {
    return null;
  }

  @Override
  public <P> P getBean(Class<P> clazz)
  {
    Set<Bean<?>> beans = beanManager.getBeans(clazz);
    Bean<?> bean = beans.iterator().next();
    CreationalContext<?> ctx = beanManager.createCreationalContext(bean);
    return (P) beanManager.getReference(bean, clazz, ctx);
  }

  @Override
  public <P> P getBean(String qualifier)
  {
    if (singletons.containsKey(qualifier))
    {
      return (P) singletons.get(qualifier);
    } else if (prototypes.containsKey(qualifier))
    {
      final Class<?> type = prototypes.get(qualifier);
      final Object instance = getBean(type);
      return (P) instance;
    }
    return null;
  }

  @Override
  public <P> P registerAndGetBean(Class<? extends P> type, String qualifier,
      Scope scope)
  {
    if (singletons.containsKey(qualifier))
      return getBean(qualifier);

    final Object instance = getBean(type);
    if (scope.equals(Scope.SINGLETON))
    {
      singletons.put(qualifier, instance);
    } else
    {
      if (!prototypes.containsKey(qualifier))
	prototypes.put(qualifier, type);
    }
    return (P) instance;
  }

}
