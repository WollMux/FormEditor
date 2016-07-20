package de.muenchen.formeditor.beans;

import java.io.File;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;

import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;

import org.apache.camel.CamelContext;
import org.apache.camel.main.Main;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.FileBasedBuilderParameters;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.configuration2.io.ClasspathLocationStrategy;
import org.apache.commons.configuration2.io.FileHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.muenchen.formeditor.camel.FMRouteBuilder;
import de.muenchen.formeditor.form.data.Function;
import de.muenchen.formeditor.utils.FunctionBeanConverter;

@ApplicationScoped
public class Producers
{
  @Inject
  private Logger log;

  @Produces
  public Logger produceLogger(InjectionPoint injectionPoint)
  {
    return LoggerFactory.getLogger(injectionPoint.getMember()
	.getDeclaringClass());
  }

  @Produces
  @ApplicationScoped
  public Configuration getConfig() throws ConfigurationException
  {
    File propertiesFile = new File("config.properties");
    FileBasedBuilderParameters params = new Parameters().fileBased()
	.setFile(propertiesFile)
	.setLocationStrategy(new ClasspathLocationStrategy());

    FileBasedConfigurationBuilder<FileBasedConfiguration> builder = new FileBasedConfigurationBuilder<FileBasedConfiguration>(
	PropertiesConfiguration.class).configure(params);
    Configuration config = builder.getConfiguration();

    return config;
  }

  @Produces
  public FileHandler getFileHandler(Configuration config)
  {
    if (config instanceof FileBasedConfiguration)
      return new FileHandler((FileBasedConfiguration) config);
    return null;
  }

  @Produces
  @Config
  public String getStringConfigValue(InjectionPoint ip, Configuration config)
  {
    String key = ip.getAnnotated().getAnnotation(Config.class).value();

    if (!key.isEmpty())
    {
      return config.getString(key);
    }

    return null;
  }

  @Produces
  @ApplicationScoped
  public Main getCamel(FMRouteBuilder routeBuilder) throws Exception
  {
    try
    {
      Main main = new Main();

      main.addRouteBuilder(routeBuilder);
      main.start();
      return main;
    } catch (Exception e)
    {
      log.error(e.getMessage(), e);
      throw e;
    }
  }
  
  @Produces
  public CamelContext getCamelContext(Main main)
  {
    return main.getCamelContexts().stream().findFirst().get();
  }
  
  @Produces
  public MapperFactory getMapperFactory()
  {
    return new DefaultMapperFactory.Builder().build();
  }
  
  @Produces
  public BeanUtilsBean getBeanUtils()
  {
    ConvertUtilsBean cub = new ConvertUtilsBean();
    cub.register(new FunctionBeanConverter(), Function.class);
    
    return new BeanUtilsBean(cub, new PropertyUtilsBean());
  }
}
