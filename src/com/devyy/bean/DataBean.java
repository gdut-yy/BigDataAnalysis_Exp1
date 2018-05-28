package com.devyy.bean;

/** 
 * DataVO实体类，封装了频繁项集，候选项集，非频繁项集。
 *  
 * @author ZYY
 * 
 */  
public class DataBean {
	private FrequentItemsetBean frequentItemSet;
	private CandidateItemsetBean candidateItemSet;
	private InFrequentItemsetBean inFrequentItemSet;

	public FrequentItemsetBean getFrequentItemSet() {
		return frequentItemSet;
	}

	public void setFrequentItemSet(FrequentItemsetBean frequentItemSet) {
		this.frequentItemSet = frequentItemSet;
	}

	public CandidateItemsetBean getCandidateItemSet() {
		return candidateItemSet;
	}

	public void setCandidateItemSet(CandidateItemsetBean candidateItemSet) {
		this.candidateItemSet = candidateItemSet;
	}

	public InFrequentItemsetBean getInFrequentItemSet() {
		return inFrequentItemSet;
	}

	public void setInFrequentItemSet(InFrequentItemsetBean inFrequentItemSet) {
		this.inFrequentItemSet = inFrequentItemSet;
	}
}
