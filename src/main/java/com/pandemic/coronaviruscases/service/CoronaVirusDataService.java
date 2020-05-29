package com.pandemic.coronaviruscases.service;

import com.pandemic.coronaviruscases.models.LocationStats;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@Service
@Getter
@Setter
@NoArgsConstructor
public class CoronaVirusDataService {

    private static final String COVID19_DATA_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv";

    private List<LocationStats> allStats = new ArrayList<>();
    @PostConstruct
    @Scheduled(cron = "* * 1 * * *")
    public void fetchCOVID19Data() throws IOException, InterruptedException {
        List<LocationStats> newStats = new ArrayList<>();
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(COVID19_DATA_URL))
                .build();

        HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        StringReader csvBodyReader = new StringReader(httpResponse.body());
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(csvBodyReader);
        for (CSVRecord record : records) {
            LocationStats locationStat = new LocationStats();
            locationStat.setState(record.get("Province/State"));
            locationStat.setCountry(record.get("Country/Region"));
            int totalGlobalCases = Integer.valueOf(record.get(record.size() - 1));
            int totalCasesUntilLastDay = Integer.valueOf(record.get(record.size() - 2));
            int totalCasesUntilLastWeek = Integer.valueOf(record.get(record.size() - 8));
            locationStat.setLatestTotalCase(totalGlobalCases);
            locationStat.setIncreasedCasesFromLastDay(totalGlobalCases - totalCasesUntilLastDay);
            locationStat.setIncreasedCasesFromLastWeek(totalGlobalCases - totalCasesUntilLastWeek);
            newStats.add(locationStat);
        }
        this.allStats = newStats;
    }
}

