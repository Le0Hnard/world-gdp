package com.demo.worldgdp.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Setter
@Getter
public class CountryLanguage {

    private Country country;
    @NotNull
    private String countryCode;
    @NotNull
    @Size(max = 30)
    private String language;
    @NotNull
    @Size(max = 1, min = 1)
    private String isOfficial;
    @NotNull
    private Double percentage;

}
