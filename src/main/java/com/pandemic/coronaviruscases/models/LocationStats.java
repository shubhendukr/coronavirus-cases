package com.pandemic.coronaviruscases.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LocationStats {

    private String state;
    private String country;
    private int latestTotalCase;

    @Override
    public String toString() {
        return "LocationStats{" +
                    "state='" + state + '\'' +
                    ", country='" + country + '\'' +
                    ", latestTotalCases=" + latestTotalCase +
                '}';
    }
}
