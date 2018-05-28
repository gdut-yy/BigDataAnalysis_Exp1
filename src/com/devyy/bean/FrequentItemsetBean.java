package com.devyy.bean;

import java.util.List;

/** 
 * 频繁项集实体类 
 * 
 * @author ZYY
 * 
 */  
public class FrequentItemsetBean {
	private List<List<Integer>> frequentItemList;

	public List<List<Integer>> getFrequentItemList() {
		return frequentItemList;
	}

	public void setFrequentItemList(List<List<Integer>> frequentItemList) {
		this.frequentItemList = frequentItemList;
	}
}
