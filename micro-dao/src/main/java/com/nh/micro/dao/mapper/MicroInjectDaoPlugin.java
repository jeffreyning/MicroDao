package com.nh.micro.dao.mapper;


import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

import com.nh.micro.rule.engine.core.IGroovyLoadPlugin;
import groovy.lang.GroovyObject;


/**
 * 
 * @author ninghao
 *
 */
public class MicroInjectDaoPlugin implements IGroovyLoadPlugin {

	@Override
	public GroovyObject execPlugIn(String name, GroovyObject groovyObject, GroovyObject proxyObject) throws Exception {
		Field[] fields=groovyObject.getClass().getDeclaredFields();
		int size=fields.length;
		for(int i=0;i<size;i++){
			Field field=fields[i];
			InjectDao anno=field.getAnnotation(InjectDao.class);
			if(anno==null){
				continue;
			}
			String groovyName=null;

				groovyName=anno.name();
				if(groovyName==null || "".equals(groovyName)){
					groovyName=field.getName();
				}

			Class cls=field.getType();
			NhsDaoProxy daoProxy=new NhsDaoProxy();
			daoProxy.setMapperInterface(cls);
			daoProxy.setGroovyName(groovyName);
		    Object proxy = Proxy.newProxyInstance(cls.getClassLoader(), 
		    	     new Class[]{cls}, 
		    	     (InvocationHandler) daoProxy);	  

			field.set(groovyObject, proxy);
			
		}
		return proxyObject;
	}

}
