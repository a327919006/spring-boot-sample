package com.cn.boot.sample.business.station;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Chen Nan
 */
@NoArgsConstructor
@Data
public class StationDetailRsp {

    private String code;
    private Object text;
    private StationDetailDTO data;
    private Object pageLimit;

    @NoArgsConstructor
    @Data
    public static class StationDetailDTO {
        private String id;
        private String city;
        private String name;
        private String address;
        private Double gisGcj02Lat;
        private Double gisGcj02Lng;
        private Double gisBd09Lat;
        private Double gisBd09Lng;
        private Integer type;
        private Integer stubGroupType;
        private String parkingInfo;
        private Integer parkingFree;
        private String stubGroupInfo;
        private String serviceTime;
        private String parkingFeeInfo;
        private String cspId;
        private String totalFeeInfo;
        private String stubCntInfo;
        private String equipmentOperatorId;
        private String equipmentOwnerId;
        private Integer paymentType;
        private Boolean placeHoldFree;
        private Integer distance;
        private String miniImgUrl;
        private Boolean isBoutique;
        private String operateCategory;
        private String flags;
        private String adminTel;
        private String tel;
        private List<?> stubList;
        private List<String> imgUrls;
        private List<ActivityListDTO> activityList;
        private Integer successCarNum;
        private Integer commentCnt;
        private Double discountedFee;
        private String discountVal;
        private List<Double> feeTotalAll;
        private List<Double> feeTotalAllDiscounted;
        private String totalFeeInfoDiscounted;
        private Integer currentTimeSlot;
        private Integer isFavorite;
        private String guideMap;
        private List<RoadInfoListDTO> roadInfoList;
        private String roadInfoUser;
        private String noticInfo;
        private String cspName;
        private String cspImg;
        private String totalFeeInfoEx;
        private String currentTime;
        private Integer acCnt;
        private Integer dcCnt;
        private Integer acIdleCnt;
        private Integer dcIdleCnt;
        private String maxKw;
        private List<String> labels;
        private Double totalFee;

        @NoArgsConstructor
        @Data
        public static class ActivityListDTO {
            private String id;
            private String description;
            private String activityType;
            private String giftType;
            private String giftValue;
            private String beginTime;
            private String endTime;
            private List<?> ruleList;
            private Integer isBest;
        }

        @NoArgsConstructor
        @Data
        public static class RoadInfoListDTO {
            private String imgUrl;
            private String info;
        }
    }
}
