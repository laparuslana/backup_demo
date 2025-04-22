package com.example.demo.Controller;


import com.example.demo.Model.Common.ActivityChartService;
import com.example.demo.Model.Common.ChartDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ActivityController {

    private final ActivityChartService chartService;

    public ActivityController(ActivityChartService chartService) {
        this.chartService = chartService;
    }

    @GetMapping("/activity-stats")
    public List<ChartDTO> getChartStats() {
        return chartService.getActivityChart();
    }
}
