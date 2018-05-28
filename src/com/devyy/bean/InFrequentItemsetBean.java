package com.devyy.bean;

import java.util.List;

/** 
 * 非频繁项集实体类 
 * 
 * @author ZYY
 * 
 */  
public class InFrequentItemsetBean {
	private List<List<Integer>> inFrequentItemList;

	public List<List<Integer>> getInFrequentItemList() {
		return inFrequentItemList;
	}

	public void setInFrequentItemList(List<List<Integer>> inFrequentItemList) {
		this.inFrequentItemList = inFrequentItemList;
	}
}
