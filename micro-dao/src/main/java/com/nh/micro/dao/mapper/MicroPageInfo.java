package com.nh.micro.dao.mapper;



/**
 * 
 * @author ninghao
 *
 */
public interface MicroPageInfo {
	public Integer getPageNo();
	public Integer getPageRows();
	public void setTotal(Long total);
	public String getOrderStr();
}
