package com.cn.boot.sample.geoip2.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Chen Nan
 */
@Data
public class AddressRsp implements Serializable {
    private String countryIsoCode;
    private String countryName;
    private String countryNames;
    private String subdivisionName;
    private String subdivisionIsoCode;
    private String cityName;
    private String postalCode;
    private Double locationLatitude;
    private Double locationLongitude;
}
