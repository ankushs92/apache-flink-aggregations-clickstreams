package com.adsizzler.adosiz.flink.aggregations;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Created by ankushsharma on 16/11/17.
 */
@Getter
@Setter
@Builder
public class AggregatedClicksByMinute {

    private Integer campaignId;
    private Integer pubId;
    private LocalDateTime minute;
    private Integer count;

}
