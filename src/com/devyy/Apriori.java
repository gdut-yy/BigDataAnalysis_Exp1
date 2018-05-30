package com.devyy;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import com.devyy.bean.CandidateItemsetBean;
import com.devyy.bean.DataBean;
import com.devyy.bean.FrequentItemsetBean;
import com.devyy.bean.InFrequentItemsetBean;
import com.devyy.util.ExcelUtil;
import com.devyy.util.PrintUtil;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

/** 
 * 首先通过单遍扫描数据集，确定每个项的支持度，得到频繁1-项集。 
 * 接着
 * (1)连接 
 * (2)剪枝 
 * 采用迭代的方法利用频繁k-1-项集生成k候选项集，扫描Excel表后从候选k-项集中找出频繁k-项集。直到生成的候选项集为空时算法结束。 
 * 其中，用到 Apriori 性质： 
 * 一个频繁项集的任一子集也应是频繁项集。 
 * 逆否命题：如果一个项集是非频繁的，则它的超集也是非频繁的。 
 * 
 * @author ZYY
 * 
 */  
public class Apriori {
	// 定义创建存放最大项集时数组的长度为100
	public static final int biggestLength = 100;
	
	private ExcelUtil excelUtil = new ExcelUtil();
	private PrintUtil printUtil = new PrintUtil();

	/**
	 * 计算Excel表中的列长度
	 * @param list——存放Excel表中单元格内容的Cell数组
	 * @return 列长度
	 */
	public int getColumnLength(List<Cell[]> list) {
		int maxLength = 0;
		Cell[] cells = null;
		for (int i = 0; i < list.size(); i++) {
			cells = list.get(i);
			if (cells.length > maxLength) {
				maxLength = cells.length;
			}
		}
		return maxLength;
	}

	/**
	 * 首先确定每个项的支持度，得到频繁1-项集及候选1-项集
	 * @param list——存放了Excel每行记录的一个list
	 * @param columnInitLoca——要进行频繁项集筛选的首列位置
	 * @param threshold——阈值
	 * @return 返回DataBean实体
	 */
	public DataBean getFrequentOneItemset(List<Cell[]> list, int columnInitLoca, int threshold) {
		int i, j, maxLength = getColumnLength(list);
		DataBean dataBean = new DataBean();
		FrequentItemsetBean frequentItemSet = new FrequentItemsetBean();
		CandidateItemsetBean candidateItemSet = new CandidateItemsetBean();
		InFrequentItemsetBean inFrequentItemSet = new InFrequentItemsetBean();
		Cell[] cells = null;
		Integer[] countArray = new Integer[maxLength];
		List<List<Integer>> frequentItemList = new ArrayList<List<Integer>>();
		List<List<Integer>> inFrequentItemList = new ArrayList<List<Integer>>();
		for (i = 0; i < countArray.length; i++) {
			countArray[i] = 0;
		}
		for (i = 0; i < list.size(); i++) {
			cells = list.get(i);
			for (j = columnInitLoca - 1; j < cells.length; j++) {
				if (cells[j].getContents().equals("T")) {
					countArray[j]++;
				}
			}
		}
		for (i = 0; i < countArray.length; i++) {
			if (countArray[i] >= threshold) {
				List<Integer> integerList = new ArrayList<Integer>();
				integerList.add(i);
				frequentItemList.add(integerList);
			} else {
				List<Integer> integerList2 = new ArrayList<Integer>();
				integerList2.add(i);
				inFrequentItemList.add(integerList2);
			}
		}
		frequentItemSet.setFrequentItemList(frequentItemList);
		candidateItemSet.setCandidateItemList(frequentItemList);
		inFrequentItemSet.setInFrequentItemList(inFrequentItemList);
		dataBean.setFrequentItemSet(frequentItemSet);
		dataBean.setCandidateItemSet(candidateItemSet);
		dataBean.setInFrequentItemSet(inFrequentItemSet);
		return dataBean;
	}

	/**
	 * 对候选子集进行剪枝
	 * @param dataBean——DataBean实体
	 * @return 返回修改后的DataBean实体
	 */
	public DataBean pruning(DataBean dataBean) {
		InFrequentItemsetBean inFrequentItemSet = dataBean.getInFrequentItemSet();
		CandidateItemsetBean candidateItemSet = dataBean.getCandidateItemSet();
		List<List<Integer>> inFrequentItemList = inFrequentItemSet.getInFrequentItemList();
		List<List<Integer>> candidateItemList = candidateItemSet.getCandidateItemList();
		List<List<Integer>> candidateItemWillBeMovedList = new ArrayList<List<Integer>>();
		for (Iterator<List<Integer>> iter = candidateItemList.iterator(); iter.hasNext();) {
			List<Integer> candidateItem = iter.next();
			for (Iterator<List<Integer>> iter2 = inFrequentItemList.iterator(); iter2.hasNext();) {
				List<Integer> inFrequentItem = iter2.next();
				if (candidateItem.containsAll(inFrequentItem)) {
					candidateItemWillBeMovedList.add(candidateItem);
				}
			}
		}
		inFrequentItemList.addAll(candidateItemWillBeMovedList);
		candidateItemList.removeAll(candidateItemWillBeMovedList);
		candidateItemSet.setCandidateItemList(candidateItemList);
		inFrequentItemSet.setInFrequentItemList(inFrequentItemList);
		dataBean.setCandidateItemSet(candidateItemSet);
		dataBean.setInFrequentItemSet(inFrequentItemSet);
		return dataBean;
	}

	/**
	 * 组成新的候选子集
	 * @param dataBean——DataBean实体
	 * @return 修改后的DataBean实体
	 */
	public DataBean createCandidateItemSet(DataBean dataBean) {
		CandidateItemsetBean candidateItemSet = dataBean.getCandidateItemSet();
		List<List<Integer>> candidateItemList = candidateItemSet.getCandidateItemList();
		List<List<Integer>> newCandidateItemList = new ArrayList<List<Integer>>();
		Set<List<Integer>> newCandidateItemListSet = new HashSet<List<Integer>>();
		for (int i = 0; i < candidateItemList.size(); i++) {
			List<Integer> candidateItem1 = candidateItemList.get(i);
			for (int j = i + 1; j < candidateItemList.size(); j++) {
				Set<Integer> set = new HashSet<Integer>();
				List<Integer> candidateItem2 = candidateItemList.get(j);
				for (int m = 0; m < candidateItem1.size(); m++) {
					set.add(candidateItem1.get(m));
					set.add(candidateItem2.get(m));
				}
				if (set.size() == (candidateItem1.size() + 1)) {
					List<Integer> newCandidateList = new ArrayList<Integer>();
					newCandidateList.addAll(set);
					newCandidateItemList.add(newCandidateList);
				}
			}
		}
		newCandidateItemListSet.addAll(newCandidateItemList);
		List<List<Integer>> resultList = new ArrayList<List<Integer>>();
		resultList.addAll(newCandidateItemListSet);
		candidateItemSet.setCandidateItemList(resultList);
		dataBean.setCandidateItemSet(candidateItemSet);
		return dataBean;
	}

	/**
	 * 得到频繁-k项集
	 * @param dataBean——DataBean实体
	 * @param sheet——工作表
	 * @param cellList——单元格List
	 * @param threshold——阈值
	 * @param columnInitLoca——要进行频繁项集筛选的首列位置
	 * @return 修改后的DataBean实体
	 */
	public DataBean getFrequentKItemSet(DataBean dataBean, Sheet sheet, List<Cell[]> cellList, int threshold,
			int columnInitLoca) {
		int i, j, n;
		boolean flag = true;
		FrequentItemsetBean frequentItemSet = dataBean.getFrequentItemSet();
		List<List<Integer>> frequentItemList = frequentItemSet.getFrequentItemList();
		CandidateItemsetBean candidateItemSet = dataBean.getCandidateItemSet();
		List<List<Integer>> candidateItemList = candidateItemSet.getCandidateItemList();
		if (!candidateItemList.isEmpty()) {
			List<Integer> candidateItem = candidateItemList.get(0);
			int candidateItemlength = candidateItem.size();
			Integer[] candidateArray = new Integer[candidateItemlength];
			InFrequentItemsetBean inFrequentItemSet = new InFrequentItemsetBean();
			List<List<Integer>> inFrequentItemList = new ArrayList<List<Integer>>();
			// 用于存放每次取到的cell的内容的值
			String[] cellArray = new String[candidateItemlength];
			List<Cell[]> cellColumnList = new ArrayList<Cell[]>();
			// 将候选集的Excel的那几列存为一个个数组，并将数组封装在一个List里
			for (i = 0; i < candidateItemlength; i++) {
				Cell[] cell = new Cell[sheet.getRows()];
				cellColumnList.add(cell);
			}
			// 计数器
			int[] countArray = new int[candidateItemList.size()];
			// 计数器的初始化
			for (i = 0; i < countArray.length; i++) {
				countArray[i] = 0;
			}
			// 遍历候选集
			for (i = 0; i < candidateItemList.size(); i++) {
				flag = true;
				// 候选集中的候选项下标
				candidateItem = candidateItemList.get(i);
				// 取出该候选项中所要比较的列，并保存到cellColumnList中
				for (j = 0; j < candidateItemlength; j++) {
					cellColumnList.set(j, sheet.getColumn(candidateItem.get(j)));
				}
				// 将该候选项里面存放的Integer数组的内容拿出来转换为Integer数组
				for (j = 0; j < candidateItemlength; j++) {
					candidateArray[j] = candidateItem.get(j);
				}
				// 对比将要比较的列中的每一行是否都为true，如果都为true，则计数器进行计数
				for (j = 0; j < sheet.getRows(); j++) {
					flag = true;
					// 将候选项中的内容的值取出
					for (n = 0; n < candidateItemlength; n++) {
						Cell[] cell = cellColumnList.get(n);
						cellArray[n] = cell[j].getContents();
					}
					for (n = 0; n < candidateItemlength; n++) {
						if ((!cellArray[0].equals(cellArray[n])) || cellArray[0].equals("F")) {
							flag = false;
							break;
						}
					}
					if (flag) {
						countArray[i]++;
					}
				}
			}
			// 从候选集中筛选出频繁项集和非频繁项集
			for (i = 0; i < countArray.length; i++) {
				if (countArray[i] >= threshold) {
					frequentItemList.add(candidateItemList.get(i));
				} else {
					inFrequentItemList.add(candidateItemList.get(i));
				}
			}
			inFrequentItemSet.setInFrequentItemList(inFrequentItemList);
			frequentItemSet.setFrequentItemList(frequentItemList);
			dataBean.setInFrequentItemSet(inFrequentItemSet);
			dataBean.setFrequentItemSet(frequentItemSet);
		}
		return dataBean;
	}

	/**
	 * 得到最大频繁项集
	 * @param dataBean
	 * 
	 * @return 返回存放了最大频繁项集的list
	 */
	public List<List<Integer>> getFrequentMaxItemset(DataBean dataBean) {
		FrequentItemsetBean frequentItemSet = dataBean.getFrequentItemSet();
		List<List<Integer>> frequentItemList = frequentItemSet.getFrequentItemList();
		// 当存在多个最大频繁项集时，用list封装
		List<List<Integer>> biggestItemList = new ArrayList<List<Integer>>();
		int max = 0;
		for (int i = 0; i < frequentItemList.size(); i++) {
			List<Integer> frequentItem = frequentItemList.get(i);
			if (frequentItem.size() > max) {
				max = frequentItem.size();
			}
		}
		for (Iterator<List<Integer>> iter = frequentItemList.iterator(); iter.hasNext();) {
			List<Integer> frequentItem = iter.next();
			if (frequentItem.size() == max) {
				biggestItemList.add(frequentItem);
			}
		}
		return biggestItemList;
	}

	/**
	 * 运行Apriori算法
	 */
	public void startApriori() {		
		// 文件名
		String filePath = "basketdata.xls";
		Workbook workbook = excelUtil.readExcel(filePath);
		// Excel文件要读取的工作表位置
		int sheetLoca = 0;		
		Sheet sheet = workbook.getSheet(sheetLoca);
		int wantLoca = 2;		// 请输入想获得的表头行位置(表头行不存在则输入-1)
		int initRowLoca = 3;	// 请输入要进行Apriori算法记录的开始的行数
		int columnInitLoca = 2;	// 请输入要进行Apriori算法记录的开始的列数(即流水号ID的下一列)
		//
		System.out.println("请输入阈值：");
		Scanner scan = new Scanner(System.in);
		int threshold = scan.nextInt();// 阈值
		
		Cell[] headArray = excelUtil.getHeadInfo(workbook, sheetLoca, wantLoca);
		List<Cell[]> cellList = excelUtil.sheetEncapsulation(workbook, sheetLoca, initRowLoca);
		DataBean dataBean = getFrequentOneItemset(cellList, columnInitLoca, threshold);
		for (; !dataBean.getCandidateItemSet().getCandidateItemList().isEmpty();) {
			// 构造新的候选子集
			dataBean = createCandidateItemSet(dataBean);
			// 对候选子集进行剪枝
			dataBean = pruning(dataBean);
			// 得到频繁-k项集
			dataBean = getFrequentKItemSet(dataBean, sheet, cellList, threshold, columnInitLoca);
		}
		// 打印频繁项集的内容
		printUtil.printFrequentItemSetContent(dataBean.getFrequentItemSet(), headArray);
		// 得到最大频繁项集
		List<List<Integer>> biggestFrequentItemSetList = getFrequentMaxItemset(dataBean);
		// 打印最大频繁项集的内容
		printUtil.printBiggestFrequentItemSetContent(biggestFrequentItemSetList, headArray);
		scan.close();
	}
}
