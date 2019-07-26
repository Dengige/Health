package com.itheima.test;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.*;

/**
 * poi入门案例
 */
public class ExcelTest {


    /**
     * 方式一：从Excel文件读取数据
     */
    //@Test
    public void readExcel1() throws IOException {
        //获取一个Excel对象
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook("C:\\working\\read.xlsx");
        //获取一个标签页
        //xssfWorkbook.getSheet("sheet1")
        XSSFSheet sheetAt = xssfWorkbook.getSheetAt(0);
        //循环遍历标签页
        for (Row rows : sheetAt) {
            //循环遍历每一列
            for (Cell cell : rows) {
                System.out.println(cell.getStringCellValue());
            }
            System.out.println("********************************************");
        }
        //关闭资源
        xssfWorkbook.close();
    }


    /**
     * 方式二：从Excel文件读取数据
     */
    //@Test
    public void readExcel2() throws IOException {
        //获取一个Excel对象
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook("C:\\working\\read.xlsx");
        //获取一个标签页
        //xssfWorkbook.getSheet("sheet1")
        XSSFSheet sheetAt = xssfWorkbook.getSheetAt(0);
        int lastRowNum = sheetAt.getLastRowNum();//获取最后一行行号 行号从0开始
        System.out.println(lastRowNum);
         for(int i =0;i<=lastRowNum;i++){
             //根据i获取每一行对象
             XSSFRow row = sheetAt.getRow(i);
             short lastCellNum = row.getLastCellNum();//当前每一行多少列
             for(int j = 0;j<lastCellNum;j++){
                //循环每一列数据
                 XSSFCell cell = row.getCell(j);
                 System.out.println(cell.getStringCellValue());
             }
             System.out.println("*************************************************");
         }
        //关闭资源
        xssfWorkbook.close();
    }

    /**
     * 创建Excel
     *
     */
    //@Test
    public void createExcel() throws IOException {
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook();//空Excel对象
        //创建标签页
        XSSFSheet xssfSheet = xssfWorkbook.createSheet("员工信息");
        //创建标题行
        XSSFRow titleRow = xssfSheet.createRow(0);
        titleRow.createCell(0).setCellValue("编号");
        titleRow.createCell(1).setCellValue("姓名");
        titleRow.createCell(2).setCellValue("年龄");

        // 数据库查询 List<Object>
        //list.for
        //数据行
        XSSFRow dataRow = xssfSheet.createRow(1);
        dataRow.createCell(0).setCellValue("001");
        dataRow.createCell(1).setCellValue("老李");
        dataRow.createCell(2).setCellValue("18");
        //仅仅在内存中创建一个Excel对象
        //通过输出流将excel对象从内存中写入磁盘中
        OutputStream outputStream = new FileOutputStream(new File("C:\\working\\write.xlsx"));
        xssfWorkbook.write(outputStream);
        outputStream.flush();//刷新
        outputStream.close();//输出流关闭
        xssfWorkbook.close();//关闭

    }
}
