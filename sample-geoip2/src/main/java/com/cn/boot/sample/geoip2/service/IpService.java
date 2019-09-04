package com.cn.boot.sample.geoip2.service;

import com.cn.boot.sample.geoip2.model.AddressRsp;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.record.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;

/**
 * @author Chen Nan
 */
@Slf4j
@Service
public class IpService {
    private DatabaseReader reader;

    public IpService() throws IOException {
        File database = new File("E:\\software\\GeoLite2-City_20190827\\GeoLite2-City.mmdb");

        reader = new DatabaseReader.Builder(database).build();
    }

    public AddressRsp printIpAddress(String ip) throws IOException, GeoIp2Exception {
        InetAddress ipAddress = InetAddress.getByName(ip);
        CityResponse response = reader.city(ipAddress);

        Country country = response.getCountry();
        Subdivision subdivision = response.getMostSpecificSubdivision();
        City city = response.getCity();
        Postal postal = response.getPostal();
        Location location = response.getLocation();

        AddressRsp rsp = new AddressRsp();
        rsp.setCountryIsoCode(country.getIsoCode());
        rsp.setCountryName(country.getName());
        rsp.setCountryNames(country.getNames().get("zh-CN"));
        rsp.setSubdivisionName(subdivision.getName());
        rsp.setSubdivisionIsoCode(subdivision.getIsoCode());
        rsp.setCityName(city.getName());
        rsp.setPostalCode(postal.getCode());
        rsp.setLocationLatitude(location.getLatitude());
        rsp.setLocationLongitude(location.getLongitude());
        return rsp;
    }
}
