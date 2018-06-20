package com.nh.micro.dao.mapper;



import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

import org.springframework.beans.factory.FactoryBean;

import com.nh.micro.rule.engine.core.GroovyExecUtil;

/**
 * 
 * @author ninghao
 *
 */
public class NDaoMapperFactory implements FactoryBean {
	public Class mapperInterface=null;

	public Class getMapperInterface() {
		return mapperInterface;
	}

	public void setMapperInterface(Class mapperInterface) {
		this.mapperInterface = mapperInterface;
	}
	
	public String groovyName=null;
	
	public String getGroovyName() {
		return groovyName;
	}

	public void setGroovyName(String groovyName) {
		this.groovyName = groovyName;
	}
	@Override
	public Object getObject() throws Exception {
		NhsDaoProxy daoProxy=new NhsDaoProxy();
		daoProxy.setMapperInterface(mapperInterface);
		
		if(groovyName==null || "".equals(groovyName)){
			InjectDao anno=(InjectDao) mapperInterface.getAnnotation(InjectDao.class);
			if(anno!=null){
				String tempGroovyName=anno.name();
				daoProxy.setGroovyName(tempGroovyName);
			}
		}		
		if(groovyName!=null && !"".equals(groovyName)){
			daoProxy.setGroovyName(groovyName);
		}		
	    Object proxy = Proxy.newProxyInstance(mapperInterface.getClassLoader(), 
	    	     new Class[]{mapperInterface}, 
	    	     (InvocationHandler) daoProxy);	  

		return proxy;
	}

	@Override
	public Class getObjectType() {
		// TODO Auto-generated method stub
		return mapperInterface;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

}
