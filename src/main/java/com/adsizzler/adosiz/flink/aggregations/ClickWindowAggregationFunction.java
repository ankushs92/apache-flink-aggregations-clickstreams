package com.adsizzler.adosiz.flink.aggregations;

import com.adsizzler.adosiz.flink.domain.Click;
import lombok.val;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.shaded.com.google.common.collect.Iterables;
import org.apache.flink.streaming.api.functions.windowing.WindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import java.time.LocalDateTime;

/**
 * Created by ankushsharma on 16/11/17.
 */
public class ClickWindowAggregationFunction implements WindowFunction<Click, AggregatedClicksByMinute, Tuple3<Integer, Integer, LocalDateTime>, TimeWindow> {

    @Override
    public void apply(
            final Tuple3<Integer, Integer, LocalDateTime> tuple,
            final TimeWindow timeWindow,
            final Iterable<Click> clickStream,
            final Collector<AggregatedClicksByMinute> collector
    ) throws Exception
    {
        final Integer campaignId = tuple.getField(0);
        final Integer pubId = tuple.getField(1);
        final LocalDateTime timestamp = tuple.getField(2);

        //The main counting bit
        val count = Iterables.size(clickStream);
        val aggregatedResult = AggregatedClicksByMinute
                                    .builder()
                                        .campaignId(campaignId)
                                        .pubId(pubId)
                                        .timestamp(timestamp)
                                        .count(count)
                                    .build();
        collector.collect(aggregatedResult);
    }
}
