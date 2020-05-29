package com.pandemic.coronaviruscases.controller;

import com.pandemic.coronaviruscases.models.LocationStats;
import com.pandemic.coronaviruscases.service.CoronaVirusDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    CoronaVirusDataService coronaVirusDataService;

    @GetMapping("/")
    public String home(Model model) {
        List<LocationStats> globalStats = coronaVirusDataService.getAllStats();
        int totalGlobalCases = globalStats.stream().mapToInt(stat -> stat.getLatestTotalCase()).sum();
        int totalNewCasesSinceLastDay = globalStats.stream().mapToInt(stat -> stat.getIncreasedCasesFromLastDay()).sum();
        int totalNewCasesSinceLastWeek = globalStats.stream().mapToInt(stat -> stat.getIncreasedCasesFromLastWeek()).sum();
        model.addAttribute("locationStats", globalStats);
        model.addAttribute("totalReportedCases", totalGlobalCases);
        model.addAttribute("totalNewCasesSinceLastDay", totalNewCasesSinceLastDay);
        model.addAttribute("totalNewCasesSinceLastWeek", totalNewCasesSinceLastWeek);
        return "home";
    }

}
