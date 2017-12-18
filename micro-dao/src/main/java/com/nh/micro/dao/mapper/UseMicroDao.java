package com.nh.micro.dao.mapper;



import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target({ ElementType.METHOD}) 
@Retention(RetentionPolicy.RUNTIME) 
@Documented

/**
 * 
 * @author ninghao
 *
 */
public @interface UseMicroDao {

}
