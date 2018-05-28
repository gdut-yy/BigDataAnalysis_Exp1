package com.devyy.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

/** 
 * ExcelUtil
 * 
 * 需导入 jxl.jar 包，其常用方法总结如下： 
 * (1)Workbook为Excel文件，Cell为单元格，Sheet为工作表对象 
 * (2)sheet.getCell(x,y)：获得第x行第y列的单元格 
 * (3)workbook.getWorkbook(File):获得文件 
 * (4)workbook.getSheet(0):获得0号(第一个)工作表对象 
 * (5)cell.getContents():获得单元格的内容 
 * (6)Cell[] cells = sheet.getColumn(column):获得某一列的值 
 * (7)Cell[] cells = sheet.getRow(row):获得某一行的值 
 * 
 * @author ZYY
 * 
 */  
public class ExcelUtil {
	/**
	 * 读取Excel文件
	 * 
	 * @param filePath——Excel文件路径
	 * @return Workbook
	 */
	public Workbook readExcel(String filePath) {
		File file = new File(filePath);
		Workbook workbook = null;
		try {
			workbook = Workbook.getWorkbook(file);
			System.out.println(filePath + "文件读取成功！");
		} catch (BiffException e) {
			System.out.println("输入流读入为空，java读取Excel异常");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("IO异常");
			e.printStackTrace();
		}
		return workbook;
	}

	/**
	 * 对Excel文件工作表的内容进行封装
	 * 
	 * @param workbook——Excel文件
	 * @param sheetLoca——工作表位置
	 * @param initRowLoca——初始行，即非表头行的记录开始的行数
	 * @return 返回一个封装了一行行数据的List
	 */
	public List<Cell[]> sheetEncapsulation(Workbook workbook, int sheetLoca, int initRowLoca) {
		Sheet sheet = workbook.getSheet(sheetLoca);
		List<Cell[]> list = new ArrayList<Cell[]>();
		Cell[] cells = null;
		int i = initRowLoca - 1, length = sheet.getRows() - initRowLoca + 1;
		while (length-- != 0) {
			cells = sheet.getRow(i);
			list.add(cells);
			i++;
		}
		return list;
	}

	/**
	 * 当表头存在多行时，获得某一特定所需表头行，将该表头行信息保存为一个Cell数组
	 * 
	 * @param workbook——Excel文件
	 * @param sheetLoca——工作表位置
	 * @param wantLoca——想获得的特定表头行位置
	 * @return 该表头行信息Cell[]数组
	 */
	public Cell[] getHeadInfo(Workbook workbook, int sheetLoca, int wantLoca) {
		if (wantLoca == -1) {
			return null;
		} else {
			Sheet sheet = workbook.getSheet(sheetLoca);
			Cell[] cells = sheet.getRow(wantLoca - 1);
			return cells;
		}
	}
}
