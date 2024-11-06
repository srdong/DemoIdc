package com.teenet.handler;

import com.alibaba.excel.write.handler.SheetWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;

import java.util.List;
import java.util.Map;

/**
 * @Description
 * @Author threedong
 * @Date: 2022/11/3 17:44
 */
@Slf4j
public class TitleHandler implements SheetWriteHandler {

    /**
     * 下拉框值
     */
    private Map<Integer, String[]> dropDownMap;


    private Map<String, List<String>> dropDownMap2;

    /**
     * 多少行有下拉
     */
    private Integer rowSize = 10;


    public TitleHandler(Map<Integer, String[]> dropDownMap) {
        this.dropDownMap = dropDownMap;
    }


    public TitleHandler(Map<Integer, String[]> dropDownMap,
                        Map<String, List<String>> dropDownMap2,
                        Integer rowSize) {
        this.dropDownMap = dropDownMap;
        this.dropDownMap2 = dropDownMap2;
        this.rowSize = rowSize;
    }


    @Override
    public void beforeSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {

    }

    /**
     * easyExcel 自定义拦截器，
     * 无法实现一个sheet页放置多个下拉选项，所以创建多个sheet
     * <p>
     * 借鉴：
     * https://rstyro.github.io/blog/2021/05/28/Easyexcel%E5%B8%B8%E7%94%A8%E7%A4%BA%E4%BE%8B%E4%BB%A3%E7%A0%81/
     *
     * @param writeWorkbookHolder
     * @param writeSheetHolder
     */
    @Override
    public void afterSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
        dropDownCascade(writeWorkbookHolder, writeSheetHolder);
        fixedDownBox(writeWorkbookHolder, writeSheetHolder);

    }

    /**
     * 固定下拉框
     *
     * @param writeWorkbookHolder
     * @param writeSheetHolder
     */
    private void fixedDownBox(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
        Sheet sheet = writeSheetHolder.getSheet();
        DataValidationHelper helper = sheet.getDataValidationHelper();
        int aa = 1;
        for (Map.Entry<Integer, String[]> entry : dropDownMap.entrySet()) {
            Integer key = entry.getKey();
            String[] strings = entry.getValue();
            //https://blog.csdn.net/qq_42747210/article/details/113063645
            // 创建sheet，突破下拉框255的限制
            //获取一个workbook
            Workbook workbook = writeWorkbookHolder.getWorkbook();
            //定义sheet的名称
            String sheetName = "mappingSheet" + key;
            //1.创建一个隐藏的sheet
            Sheet hiddenSheet = workbook.createSheet(sheetName);
            Name category1Name = workbook.createName();
            category1Name.setNameName(sheetName);
            // 设置隐藏
            workbook.setSheetHidden(aa, true);
            aa++;
            //2.循环赋值（为了防止下拉框的行数与隐藏域的行数相对应，将隐藏域加到结束行之后）
            for (int i = 0, length = strings.length; i < length; i++) {
                // i:表示你开始的行数  0表示你开始的列数
                hiddenSheet.createRow(i).createCell(0).setCellValue(strings[i]);
            }
            //4 $A$1:$A$N代表 以A列1行开始获取N行下拉数据
            category1Name.setRefersToFormula(sheetName + "!$A$1:$A$" + (strings.length));
            //5 将刚才设置的sheet引用到你的下拉列表中
            CellRangeAddressList addressList = new CellRangeAddressList(1, rowSize, key, key);
            DataValidationConstraint constraint8 = helper.createFormulaListConstraint(sheetName);
            DataValidation dataValidation3 = helper.createValidation(constraint8, addressList);
            writeSheetHolder.getSheet().addValidationData(dataValidation3);
        }
    }

    /**
     * 下拉级联
     *
     * @param writeWorkbookHolder
     * @param writeSheetHolder
     */
    private void dropDownCascade(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
        //获取一个workbook
        Workbook workbook = writeWorkbookHolder.getWorkbook();
        //定义sheet的名称
        String sheetName = "mappingSheet";
        //1.创建一个隐藏的sheet
        Sheet hiddenSheet = workbook.createSheet(sheetName);
        Name category1Name = workbook.createName();
        category1Name.setNameName(sheetName);
        // 设置隐藏
        workbook.setSheetHidden(1, true);
        int rowIndex = insertName(dropDownMap2, hiddenSheet, null, workbook, sheetName);
        Sheet sheet = writeSheetHolder.getSheet();
        DataValidationHelper helper = sheet.getDataValidationHelper();
        //4 拉取隐藏页 $A$1:$A$N代表 以A列1行开始获取N行下拉数据
        category1Name.setRefersToFormula(sheetName + "!$A$1:$A$" + rowIndex);
        //5 将刚才设置的sheet引用到你的下拉列表中
        CellRangeAddressList addressList = new CellRangeAddressList(1, rowSize, 4, 4);
        DataValidationConstraint constraint8 = helper.createFormulaListConstraint(sheetName);
        DataValidation dataValidation3 = helper.createValidation(constraint8, addressList);
        // 阻止输入非下拉选项的值
        stopWriteIn(dataValidation3);
        sheet.addValidationData(dataValidation3);
        //下拉框的联动
        setIndirect(sheet, helper, rowSize - 1);
    }

    private void stopWriteIn(DataValidation dataValidation) {
        // 阻止输入非下拉选项的值
        dataValidation.setErrorStyle(DataValidation.ErrorStyle.STOP);
        dataValidation.setShowErrorBox(true);
        dataValidation.setSuppressDropDownArrow(true);
        dataValidation.createErrorBox("提示", "此值与单元格定义格式不一致");
    }

    private void setIndirect(Sheet sheet, DataValidationHelper helper, int length) {
        int index = 1;
        for (int i = 0; i < length; i++) {
            int index1 = index + i;
            int index2 = index + i + 1;
            CellRangeAddressList addressList2 = new CellRangeAddressList(index1, index1, 5, 5);
            String listFormula2 = "INDIRECT($E$" + index2 + ")";
            DataValidationConstraint formulaListConstraint2 = helper.createFormulaListConstraint(listFormula2);
            DataValidation validation2 = helper.createValidation(formulaListConstraint2, addressList2);
            stopWriteIn(validation2);
            sheet.addValidationData(validation2);
        }

    }


    private int insertName(Map<String, List<String>> dropDownMap3, Sheet hiddenSheet, Integer rowIndex, Workbook workbook, String sheetName) {
        rowIndex = null == rowIndex ? 0 : rowIndex;
        for (Map.Entry<String, List<String>> entry : dropDownMap3.entrySet()) {
            String parentValue = entry.getKey();
            int length = 1;
            List<String> childValues = entry.getValue();
            Row row = hiddenSheet.createRow(rowIndex++);
            row.createCell(0).setCellValue(parentValue);
            if (CollectionUtils.isNotEmpty(childValues)) {
                length = childValues.size();
                for (int j = 0; j < length; j++) {
                    Cell cell = row.createCell(j + 1);
                    cell.setCellValue(childValues.get(j));
                }
            } else {
                Cell cell = row.createCell(1);
                cell.setCellValue("");
            }
            // 添加名称管理器
            String range = getRange(1, rowIndex, length);
            Name name = workbook.createName();
            //key不可重复
            name.setNameName(parentValue);
            String formula = sheetName + "!" + range;
            name.setRefersToFormula(formula);
        }
        return rowIndex;
    }

    public static String getRange(int offset, int rowNum, int colCount) {
        String start = getColNum(offset);
        String end = getColNum(colCount);
        String format = "$%s$%s:$%s$%s";
        return String.format(format, start, rowNum, end, rowNum);
    }

    public static String getColNum(int num) {
        int MAX_NUM = 26;
        char initChar = 'A';
        if (num == 0) {
            return initChar + "";
        } else if (num > 0 && num < MAX_NUM) {
            int result = num % MAX_NUM;
            return (char) (initChar + result) + "";
        } else if (num >= MAX_NUM) {
            int result = num / MAX_NUM;
            int mod = num % MAX_NUM;
            String starNum = getColNum(result - 1);
            String endNum = getColNum(mod);
            return starNum + endNum;
        }
        return "";
    }
}
