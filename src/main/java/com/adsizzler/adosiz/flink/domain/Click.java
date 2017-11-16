package com.adsizzler.adosiz.flink.domain;

import com.adsizzler.adosiz.flink.jackson.JacksonLocalDateTimeDeserializer;
import com.adsizzler.adosiz.flink.jackson.UUIDDeserializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Created by Ankush on 20/02/17.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class Click {

        @JsonProperty("uid")
        @JsonDeserialize(using=UUIDDeserializer.class)
        private UUID uid;

        @JsonProperty("timestamp")
        @JsonDeserialize(using = JacksonLocalDateTimeDeserializer.class)
        private LocalDateTime timestamp;

        @JsonProperty("campaignId")
        private Integer campaignId;

        @JsonProperty("pubId")
        private Integer pubId;

        @JsonProperty("ip")
        private String ip;

        @JsonProperty("city")
        private String city;

        @JsonProperty("country")
        private String country;

        @JsonProperty("province")
        private String province;

}
