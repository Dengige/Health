package com.itheima.service;

import java.util.Map;

/**
 * 运营数据统计报表
 */
public interface ReportService {
    Map<String,Object> getBusinessReportData() throws Exception;
}
