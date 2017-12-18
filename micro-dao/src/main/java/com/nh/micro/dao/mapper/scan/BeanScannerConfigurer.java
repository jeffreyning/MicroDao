package com.nh.micro.dao.mapper.scan;





import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class BeanScannerConfigurer implements  BeanFactoryPostProcessor, ApplicationContextAware {
	private ApplicationContext applicationContext;
	private String scanPath;
	
	public String getScanPath() {
		return scanPath;
	}
	public void setScanPath(String scanPath) {
		this.scanPath = scanPath;
	}
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
	    this.applicationContext = applicationContext;
	}
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
	    Scanner scanner = new Scanner((BeanDefinitionRegistry) beanFactory);
	    scanner.setResourceLoader(this.applicationContext);
	    scanner.scan(scanPath);
	}
}