package com.cn.boot.sample.business.station;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Chen Nan
 */
@NoArgsConstructor
@Data
public class StationListRsp {

    private String code;
    private Object text;
    private List<StationListDTO> data;
    private PageLimitDTO pageLimit;

    @NoArgsConstructor
    @Data
    public static class PageLimitDTO {
        private Boolean limited;
        private Integer currentPageNo;
        private Integer pageLength;
        private Integer totalCount;
        private Integer startRowNo;
        private Integer endRowNo;
        private Boolean onlyGetTotalCnt;
        private Boolean onlyGetRows;
        private Integer totalPageCount;
        private Boolean hasPreviousPage;
        private Boolean hasNextPage;
    }

    @NoArgsConstructor
    @Data
    public static class StationListDTO {
        private String id;
        private String city;
        private String name;
        private String address;
        private Double gisGcj02Lat;
        private Double gisGcj02Lng;
        private Integer type;
        private String modifyTime;
        private Integer stubGroupType;
        private Integer chargeMode;
        private String tfInfo;
        private Integer parkingFree;
        private String parkingFeeInfo;
        private Boolean reservable;
        private Integer hasGun;
        private String serviceTime;
        private String cspId;
        private String kws;
        private Integer acCnt;
        private Integer dcCnt;
        private Integer acIdleCnt;
        private Integer dcIdleCnt;
        private Integer isShow;
        private String flags;
        private String operateCategory;
        private String parkingLimit;
        private Double distance;
        private String trusteeshipInfo;
        private String noticeInfo;
        private Double eleFee;
        private Double serFee;
        private Double eleFeeDiscounted;
        private Double serFeeDiscounted;
        private Integer isFavorite;
        private Integer isLastCharge;
        private List<String> labels;
        private String brandIcon;
        private Integer appStatus;
        private String miUrl;
        private Integer lvStubCnt;
        private Integer hvStubCnt;
        private Integer halvStubCnt;
    }
}
