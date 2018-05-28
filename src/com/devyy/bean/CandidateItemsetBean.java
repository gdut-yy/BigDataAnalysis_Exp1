package com.devyy.bean;

import java.util.List;

/** 
 * 候选项集实体类 
 * 
 * @author ZYY
 * 
 */  
public class CandidateItemsetBean {
	private List<List<Integer>> candidateItemList;

	public List<List<Integer>> getCandidateItemList() {
		return candidateItemList;
	}

	public void setCandidateItemList(List<List<Integer>> candidateItemList) {
		this.candidateItemList = candidateItemList;
	}
}
