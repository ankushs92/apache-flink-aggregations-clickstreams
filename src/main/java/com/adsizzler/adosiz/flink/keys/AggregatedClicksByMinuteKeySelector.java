package com.adsizzler.adosiz.flink.keys;

import com.adsizzler.adosiz.flink.domain.Click;
import lombok.val;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple3;

import java.time.LocalDateTime;

/**
 * Created by ankushsharma on 16/11/17.
 */
public class AggregatedClicksByMinuteKeySelector implements KeySelector<Click, Tuple3<Integer, Integer, LocalDateTime>> {

    @Override
    public Tuple3<Integer, Integer, LocalDateTime> getKey(final Click click) throws Exception
    {
        val campaignId = click.getCampaignId();
        val pubId = click.getPubId();
        val timestamp = click.getTimestamp();
        return new Tuple3<>(campaignId, pubId, timestamp);
    }
}
