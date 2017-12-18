package com.nh.micro.dao.mapper;



import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



import com.nh.micro.dao.mapper.MicroMapperTemplate;
import com.nh.micro.dao.mapper.MicroCommonMapper;
import com.nh.micro.dao.mapper.MicroMapperUtil;
import com.nh.micro.dao.mapper.UseMicroDao;
import com.nh.micro.orm.MicroDbName;
import com.nh.micro.rule.engine.core.GroovyExecUtil;

public class NhsDaoProxy implements InvocationHandler {
	private MicroMapperTemplate microMapperTemplate=null;
	private Class mapperInterface=null;
	public MicroMapperTemplate getMicroMapperTemplate() {
		return microMapperTemplate;
	}
	public void setMicroMapperTemplate(MicroMapperTemplate microMapperTemplate) {
		this.microMapperTemplate = microMapperTemplate;
	}
	public Class getMapperInterface() {
		return mapperInterface;
	}
	public void setMapperInterface(Class mapperInterface) {
		this.mapperInterface = mapperInterface;
	}
	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		  if(this.microMapperTemplate==null){
			  MicroDbName dbNameAnno=(MicroDbName) mapperInterface.getAnnotation(MicroDbName.class);
			  String dbName="default";
			  if(dbNameAnno!=null){
				  dbName=dbNameAnno.name();
			  }			  
			  MicroMapperTemplate microMapperTemplate=new MicroMapperTemplate(dbName);
			  Class entityClass=MicroMapperUtil.getEntityClass(MicroCommonMapper.class, mapperInterface);
			  microMapperTemplate.setDefaultClass(entityClass);
			  this.microMapperTemplate=microMapperTemplate;
		  }		
		  
		  UseMicroDao useMicro=method.getAnnotation(UseMicroDao.class);
		  if(useMicro!=null){
			 // String methodName=method.getName();
			  
/*			  MicroDbName dbNameAnno=(MicroDbName) mapperInterface.getAnnotation(MicroDbName.class);
			  String dbName="default";
			  if(dbNameAnno!=null){
				  dbName=dbNameAnno.name();
			  }
			  if(this.microMapperTemplate==null){
				  MicroMapperTemplate microMapperTemplate=new MicroMapperTemplate(dbName);
				  Class entityClass=MicroMapperUtil.getEntityClass(MicroCommonMapper.class, mapperInterface);
				  microMapperTemplate.setDefaultClass(entityClass);
				  this.microMapperTemplate=microMapperTemplate;
			  }*/

			  return MicroMapperUtil.executeMethod(MicroCommonMapper.class, mapperInterface, method, this.microMapperTemplate, args);

		  }

		  //add end		

		  List placeList=new ArrayList();
		  String groovyName=mapperInterface.getSimpleName();
		  String methodName=method.getName();

		 // String sql=(String) GroovyExecUtil.execGroovyRetObj(groovyName, methodName, args, placeList);	
		  Class oc=method.getReturnType();
		  boolean listFlag=false;
		  if(oc.isAssignableFrom(List.class)){
			  listFlag=true;
		  }
		  Class outClass=microMapperTemplate.getDefaultClass();
		  ListInnerClass innerClassAnno=method.getAnnotation(ListInnerClass.class);
		  if(innerClassAnno!=null){
			  outClass=innerClassAnno.name();
		  }
		  return microMapperTemplate.execServiceByGroovy(groovyName, methodName, outClass, listFlag, args);

	}

}
