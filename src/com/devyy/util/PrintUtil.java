package com.devyy.util;

import java.util.Iterator;
import java.util.List;

import com.devyy.bean.CandidateItemsetBean;
import com.devyy.bean.FrequentItemsetBean;

import jxl.Cell;


/** 
 * PrintUtil
 * 
 * 处理程序打印语句的工具类 
 * 
 * @author ZYY
 * 
 */  
public class PrintUtil {
	/**
	 * 打印当前候选集内容
	 * 
	 * @param set——候选项集
	 * @param cell——表头信息数组
	 */
	public void printCandidateItemSetContent(CandidateItemsetBean set, Cell[] cell) {
		System.out.println("------------------当前候选集中的内容为------------------");
		int i = 1, j = 0;
		List<List<Integer>> list = set.getCandidateItemList();
		for (Iterator<List<Integer>> iter = list.iterator(); iter.hasNext();) {
			List<Integer> integerList = iter.next();
			System.out.print("第" + i + "项的内容为：{");
			for (j = 0; j < integerList.size() - 1; j++) {
				System.out.print(cell[integerList.get(j)].getContents() + ", ");
			}
			System.out.print(cell[integerList.get(integerList.size() - 1)].getContents());
			System.out.println("}");
			i++;
		}
	}

	/**
	 * 打印频繁项集的内容
	 * 
	 * @param set——频繁项集
	 * @param cell——表头信息数组
	 */
	public void printFrequentItemSetContent(FrequentItemsetBean set, Cell[] cell) {
		System.out.println("------------------频繁项集的内容为------------------");
		int i = 1, j = 0;
		List<List<Integer>> list = set.getFrequentItemList();
		for (Iterator<List<Integer>> iter = list.iterator(); iter.hasNext();) {
			List<Integer> integerList = iter.next();
			System.out.print("第" + i + "项的内容为：{");
			for (j = 0; j < integerList.size() - 1; j++) {
				System.out.print(cell[integerList.get(j)].getContents() + ", ");
			}
			System.out.print(cell[integerList.get(integerList.size() - 1)].getContents());
			System.out.println("}");
			i++;
		}
	}

	/**
	 * 打印最大频繁项集的内容
	 * 
	 * @param list——封装最大频繁项集的list
	 * @param cell——表头信息数组
	 */
	public void printBiggestFrequentItemSetContent(List<List<Integer>> list, Cell[] cell) {
		int j;
		System.out.println("------------------最大频繁项集为------------------");
		for (Iterator<List<Integer>> iter = list.iterator(); iter.hasNext();) {
			List<Integer> integerList = iter.next();
			System.out.print("{");
			for (j = 0; j < integerList.size() - 1; j++) {
				System.out.print(cell[integerList.get(j)].getContents() + ", ");
			}
			System.out.print(cell[integerList.get(integerList.size() - 1)].getContents());
			System.out.println("}");
		}
	}
}
