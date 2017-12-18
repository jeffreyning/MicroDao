package com.nh.micro.dao.mapper;



import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

import org.springframework.beans.factory.FactoryBean;

import com.nh.micro.rule.engine.core.GroovyExecUtil;

public class NDaoMapperFactory implements FactoryBean {
	public Class mapperInterface=null;

	public Class getMapperInterface() {
		return mapperInterface;
	}

	public void setMapperInterface(Class mapperInterface) {
		this.mapperInterface = mapperInterface;
	}

	@Override
	public Object getObject() throws Exception {
		NhsDaoProxy daoProxy=new NhsDaoProxy();
		daoProxy.setMapperInterface(mapperInterface);
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
