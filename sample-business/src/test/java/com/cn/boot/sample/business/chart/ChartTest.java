package com.cn.boot.sample.business.chart;

import com.cn.boot.sample.business.util.chart.ChartUtil;
import org.junit.jupiter.api.Test;

/**
 * @author Chen Nan
 */

public class ChartTest {

    @Test
    public void createPieChart(){
        ChartUtil.createPieChart();
    }

    @Test
    public void createLineChart(){
        ChartUtil.createLineChart();
    }

    @Test
    public void createBarChart(){
        ChartUtil.createBarChart();
    }
}
