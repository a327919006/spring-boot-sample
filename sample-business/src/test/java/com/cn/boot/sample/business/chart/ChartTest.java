package com.cn.boot.sample.business.chart;

import com.cn.boot.sample.business.util.chart.ChartUtil;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

/**
 * @author Chen Nan
 */

public class ChartTest {

    @Test
    @SneakyThrows
    public void createPieChart(){
        ChartUtil.createPieChart();
    }
}
