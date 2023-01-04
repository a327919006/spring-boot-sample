package com.cn.boot.sample.business.station;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Chen Nan
 */
@NoArgsConstructor
@Data
public class CityLocationRsp {

    private String status;
    private ResultDTO result;

    @NoArgsConstructor
    @Data
    public static class ResultDTO {
        private LocationDTO location;
        private Integer precise;
        private Integer confidence;
        private String level;

        @NoArgsConstructor
        @Data
        public static class LocationDTO {
            private Double lng;
            private Double lat;
        }
    }
}
