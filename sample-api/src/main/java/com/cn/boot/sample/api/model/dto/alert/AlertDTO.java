package com.cn.boot.sample.api.model.dto.alert;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Chen Nan
 */
@NoArgsConstructor
@Data
public class AlertDTO {
    private String receiver;
    private String status;
    private List<AlertsDTO> alerts;
    private GroupLabelsDTO groupLabels;
    private CommonLabelsDTO commonLabels;
    private CommonAnnotationsDTO commonAnnotations;
    private String externalURL;
    private String version;
    private String groupKey;
    private Integer truncatedAlerts;

    @NoArgsConstructor
    @Data
    public static class GroupLabelsDTO {
        private String did;
        private String faultCode;
        private String mid;
    }

    @NoArgsConstructor
    @Data
    public static class CommonLabelsDTO {
        private String alertname;
        private String did;
        private String exportedJob;
        private String faultCode;
        private String instance;
        private String job;
        private String levelCode;
        private String mid;
        private String prometheus;
    }

    @NoArgsConstructor
    @Data
    public static class CommonAnnotationsDTO {
        private String type;
        private String value;
    }

    @NoArgsConstructor
    @Data
    public static class AlertsDTO {
        private String status;
        private LabelsDTO labels;
        private AnnotationsDTO annotations;
        private String startsAt;
        private String endsAt;
        private String generatorURL;
        private String fingerprint;

        @NoArgsConstructor
        @Data
        public static class LabelsDTO {
            private String alertname;
            private String did;
            private String exportedJob;
            private String faultCode;
            private String instance;
            private String job;
            private String levelCode;
            private String mid;
            private String prometheus;
        }

        @NoArgsConstructor
        @Data
        public static class AnnotationsDTO {
            private String type;
            private String value;
        }
    }
}
