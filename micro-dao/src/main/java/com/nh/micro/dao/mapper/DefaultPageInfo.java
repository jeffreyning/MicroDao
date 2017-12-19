package com.nh.micro.dao.mapper;

public class DefaultPageInfo implements MicroPageInfo {

	public Integer pageNo=1;
	public Integer pageRows=10;
	public Long total=0l;
	public String orderStr="";

	public void setPageNo(Integer pageNo) {
		this.pageNo=pageNo;
	}
	@Override
	public Integer getPageNo() {
		return this.pageNo;
	}

	public void setPageRows(Integer pageRows) {
		this.pageRows=pageRows;
	}
	@Override
	public Integer getPageRows() {
		return this.pageRows;
	}

	@Override
	public void setTotal(Long total) {
		this.total=total;
	}
	
	public Long getTotal() {
		return this.total;
	}	

	@Override
	public String getOrderStr() {
		return this.orderStr;
	}
	
	public void setOrderStr(String orderStr){
		this.orderStr=orderStr;
	}

}
