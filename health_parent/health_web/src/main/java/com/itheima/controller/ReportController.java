package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.service.MemberSerivce;
import com.itheima.service.ReportService;
import com.itheima.service.SetmealService;
import com.itheima.utils.DateUtils;
import net.sf.jxls.transformer.XLSTransformer;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 报表控制层代码
 */
@RequestMapping("/report")
@RestController
public class ReportController {


    @Reference
    private MemberSerivce memberSerivce;

    @Reference
    private SetmealService setmealService;


    @Reference
    private ReportService reportService;

    /**
     * 会员数量折线图
     */
    @RequestMapping(value = "/getMemberReport",method = RequestMethod.GET)
    public Result getMemberReport(){
        Map<String,Object> map = new HashMap<>();
        List<String> months = new ArrayList<>();

        //1.获取当前时间
        Calendar calendar = Calendar.getInstance();
        //2.往前推12个月
        calendar.add(Calendar.MONTH,-12);//12月前时间

        for(int i = 1;i<=12;i++){
            //3.每次+1 得到时间 存入list
            calendar.add(Calendar.MONTH,1);
            Date time = calendar.getTime();
            //把时间转-  ==>2019-07
            String nowDate = new SimpleDateFormat("yyyy-MM").format(time);
            months.add(nowDate);
        }
        //select count(*) from t_member where regTime <= '2019-06-31'
        List<Integer> memberCount  = memberSerivce.findMemberCountByMonth(months);
        map.put("months",months);//月份数据
        map.put("memberCount",memberCount);//会员数量
        return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS,map);
    }


    /**
     * 获取套餐预约占比的饼图
     */
    @RequestMapping(value = "/getSetmealReport",method = RequestMethod.GET)
    public Result getSetmealReport(){
        /*"setmealNames":['入职体检套餐','测试套餐','血常规套餐'],
        "setmealCount":[
        {value:10,name:'入职体检套餐'},
        {value:2,name:'测试套餐'},
        {value:5,name:'血常规套餐'}
        ]*/
        //查询套餐名称 以及套餐预约数量  select ts.name,count(o.id) value from t_setmeal ts,t_order o where ts.id = o.setmeal_id group by ts.id

        try {
            List<Map> setmealCount =setmealService.setmealCount();
            List<String> setmealNames = new ArrayList<>();//
            if(setmealCount != null && setmealCount.size()>0){
                for (Map map : setmealCount) {
                    String name = (String)map.get("name");
                    setmealNames.add(name);
                }
            }
            Map<String,Object> map = new HashMap<>();
            map.put("setmealNames",setmealNames);//套餐名称
            map.put("setmealCount",setmealCount);//套餐名称和套餐数量  页面会根据套餐数量自动计算占比
            return new Result(true, MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS,map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_SETMEAL_COUNT_REPORT_FAIL);

        }
    }


    /**
     * getBusinessReportData
     */
    @RequestMapping(value = "/getBusinessReportData",method = RequestMethod.GET)
    public Result getBusinessReportData(){
        //1.定义返回结果Map
        try {
            Map<String,Object> rsMap = reportService.getBusinessReportData();
            return new Result(true, MessageConstant.GET_BUSINESS_REPORT_SUCCESS,rsMap);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }
    }

    /**
     * 导出运营数据报表
     */
    /*@RequestMapping(value = "/exportBusinessReport",method = RequestMethod.GET)
    public void exportBusinessReport(HttpServletRequest request, HttpServletResponse response){
        try {
            Map<String,Object> rsMap = reportService.getBusinessReportData();
            //获取模板路径
            String filePath = request.getSession().getServletContext().getRealPath("template")+File.separator+"report_template.xlsx";
            //1.得到Excel对象
            XSSFWorkbook xssfWorkbook = new XSSFWorkbook(new FileInputStream(new File(filePath)));
            //2.为运营统计数据日期赋值
            XSSFSheet sheet = xssfWorkbook.getSheetAt(0);//标签页
            XSSFRow row = sheet.getRow(2);//获取第二行
            row.getCell(5).setCellValue((String)rsMap.get("reportDate"));


            Integer todayNewMember = (Integer) rsMap.get("todayNewMember");
            Integer totalMember = (Integer) rsMap.get("totalMember");
            Integer thisWeekNewMember = (Integer) rsMap.get("thisWeekNewMember");
            Integer thisMonthNewMember = (Integer) rsMap.get("thisMonthNewMember");
            Integer todayOrderNumber = (Integer) rsMap.get("todayOrderNumber");
            Integer thisWeekOrderNumber = (Integer) rsMap.get("thisWeekOrderNumber");
            Integer thisMonthOrderNumber = (Integer) rsMap.get("thisMonthOrderNumber");
            Integer todayVisitsNumber = (Integer) rsMap.get("todayVisitsNumber");
            Integer thisWeekVisitsNumber = (Integer) rsMap.get("thisWeekVisitsNumber");
            Integer thisMonthVisitsNumber = (Integer) rsMap.get("thisMonthVisitsNumber");
            List<Map> hotSetmeal = (List<Map>) rsMap.get("hotSetmeal");

            row = sheet.getRow(4);
            row.getCell(5).setCellValue(todayNewMember);//新增会员数（本日）
            row.getCell(7).setCellValue(totalMember);//总会员数

            row = sheet.getRow(5);
            row.getCell(5).setCellValue(thisWeekNewMember);//本周新增会员数
            row.getCell(7).setCellValue(thisMonthNewMember);//本月新增会员数

            row = sheet.getRow(7);
            row.getCell(5).setCellValue(todayOrderNumber);//今日预约数
            row.getCell(7).setCellValue(todayVisitsNumber);//今日到诊数

            row = sheet.getRow(8);
            row.getCell(5).setCellValue(thisWeekOrderNumber);//本周预约数
            row.getCell(7).setCellValue(thisWeekVisitsNumber);//本周到诊数

            row = sheet.getRow(9);
            row.getCell(5).setCellValue(thisMonthOrderNumber);//本月预约数
            row.getCell(7).setCellValue(thisMonthVisitsNumber);//本月到诊数
            
            //热门套餐循环
            if(hotSetmeal != null && hotSetmeal.size()>0){
                int rowNum =12;//从12行开始
                for (Map map : hotSetmeal) {
                    //创建行
                    XSSFRow setmealRow = sheet.getRow(rowNum);
                    setmealRow.getCell(4).setCellValue((String)map.get("name"));
                    setmealRow.getCell(5).setCellValue(map.get("setmeal_count").toString());
                    setmealRow.getCell(6).setCellValue(map.get("proportion").toString());
                    setmealRow.getCell(7).setCellValue((String)map.get("remark"));
                    ///写完一行数据后
                    rowNum ++;
                }
            }

            //3.以文件流形式 下载本地磁盘 通过response对象返回文件流
            OutputStream outputStream = response.getOutputStream();
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");//excel文件
            response.setHeader("content-Disposition", "attachment;filename=report.xlsx");//以文件附件形式下载 文件名是report.xlsx
            xssfWorkbook.write(outputStream);

            //4.关闭资源
            outputStream.flush();
            outputStream.close();
            xssfWorkbook.close();
            //return new Result(true, MessageConstant.GET_BUSINESS_REPORT_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            //return new Result(false, MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }
    }*/

    /**
     * 简单导出运营数据统计 jxsl-core模板技术 （针对poi）
     * @param request
     * @param response
     */
    @RequestMapping(value = "/exportBusinessReport",method = RequestMethod.GET)
    public void exportBusinessReport(HttpServletRequest request, HttpServletResponse response){
        try {
            Map<String,Object> rsMap = reportService.getBusinessReportData();
            //获取模板路径
            String filePath = request.getSession().getServletContext().getRealPath("template")+File.separator+"report_template.xlsx";
            //1.得到Excel对象
            XSSFWorkbook xssfWorkbook = new XSSFWorkbook(new FileInputStream(new File(filePath)));

            XLSTransformer transformer = new XLSTransformer();
            transformer.transformWorkbook(xssfWorkbook, rsMap);

            //3.以文件流形式 下载本地磁盘 通过response对象返回文件流
            OutputStream outputStream = response.getOutputStream();
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");//excel文件
            response.setHeader("content-Disposition", "attachment;filename=report.xlsx");//以文件附件形式下载 文件名是report.xlsx
            xssfWorkbook.write(outputStream);

            //4.关闭资源
            outputStream.flush();
            outputStream.close();
            xssfWorkbook.close();
            //return new Result(true, MessageConstant.GET_BUSINESS_REPORT_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            //return new Result(false, MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }
    }
}
