package de.muenchen.formeditor.beans;

import java.io.File;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

import org.apache.camel.main.Main;
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

import de.muenchen.formeditor.FMRouteBuilder;

@ApplicationScoped
public class Producers
{
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
    FileBasedBuilderParameters params = new Parameters().fileBased().setFile(
	propertiesFile).setLocationStrategy(new ClasspathLocationStrategy());

    FileBasedConfigurationBuilder<FileBasedConfiguration> builder = new FileBasedConfigurationBuilder<FileBasedConfiguration>(
	PropertiesConfiguration.class).configure(params);
    Configuration config = builder.getConfiguration();

    return config;
  }
  
  @Produces
  public FileHandler getFileHandler(Configuration config)
  {
    if (config instanceof FileBasedConfiguration)
      return new FileHandler((FileBasedConfiguration)config);
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
  public Main getCamel(FMRouteBuilder routeBuilder)
  {
    Main main = new Main();

    main.addRouteBuilder(routeBuilder);

    return main;
  }
}
