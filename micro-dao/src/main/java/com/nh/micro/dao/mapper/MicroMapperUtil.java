package com.nh.micro.dao.mapper;



import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.nh.micro.dao.mapper.MicroMapperTemplate;

public class MicroMapperUtil {
	public static Class getEntityClass(Class commonClass,Class mapperClass){
        Type[] types = mapperClass.getGenericInterfaces();
        for (Type type : types) {
            if (type instanceof ParameterizedType) {
                ParameterizedType t = (ParameterizedType) type;
                if (t.getRawType().equals(commonClass) || commonClass.isAssignableFrom((Class<?>) t.getRawType())) {
                    Class<?> returnType = (Class<?>) t.getActualTypeArguments()[0];
                    return returnType;
                }
            }
        }
        return null;
	}
	
	public static Object executeMethod(Class commonClass,Class mapperInterface,Method method, MicroMapperTemplate template, Object[] args)
			throws Exception {

		String methodName=method.getName();

		Method[] methods = MicroMapperTemplate.class.getMethods();
		for (Method rowMethod : methods) {
			if (rowMethod.getName().equals(methodName)) {
				method = rowMethod;
				break;
			}
		}
		if (method == null) {
			throw new RuntimeException("no such method =" + methodName);
		}

		Object retObj = method.invoke(template, args);
		return retObj;
	}	
	
	public static Class getInputClass(Object example,Class inClass){
		if(inClass!=null){
			return inClass;
		}
		return example.getClass();
	}
	public static Class getOutputClass(Object example,Class inClass,Class outClass){
		if(outClass!=null){
			return outClass;
		}
		if(inClass!=null){
			return inClass;
		}
		return example.getClass();
	}
}
