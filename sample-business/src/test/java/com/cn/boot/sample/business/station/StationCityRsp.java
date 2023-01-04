package com.cn.boot.sample.business.station;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * {
 * 	"code": "200",
 * 	"text": null,
 * 	"data": "{\"status\":0,\"result\":{\"location\":{\"lng\":119.45559999999998,\"lat\":25.989389991111876},\"formatted_address\":\"福建省福州市马尾区昭忠路\",\"business\":\"罗星,罗星西路\",\"addressComponent\":{\"country\":\"中国\",\"country_code\":0,\"country_code_iso\":\"CHN\",\"country_code_iso2\":\"CN\",\"province\":\"福建省\",\"city\":\"福州市\",\"city_level\":2,\"district\":\"马尾区\",\"town\":\"\",\"town_code\":\"\",\"adcode\":\"350105\",\"street\":\"昭忠路\",\"street_number\":\"\",\"direction\":\"\",\"distance\":\"\"},\"pois\":[],\"roads\":[],\"poiRegions\":[{\"direction_desc\":\"内\",\"name\":\"船政文化景区\",\"tag\":\"旅游景点;风景区\",\"uid\":\"93df0dc3f246af2b882fb57a\",\"distance\":\"0\"}],\"sematic_description\":\"船政文化景区内,船都西北92米\",\"cityCode\":300}}",
 * 	"pageLimit": null
 * }
 * @author Chen Nan
 */
@NoArgsConstructor
@Data
public class StationCityRsp {

    private String code;
    private Object text;
    private String data;
}
