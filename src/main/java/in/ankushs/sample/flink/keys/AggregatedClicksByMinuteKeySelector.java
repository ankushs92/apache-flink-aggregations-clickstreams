package in.ankushs.sample.flink.keys;

import in.ankushs.sample.flink.domain.Click;
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
        val clickTimestamp = click.getTimestamp();
        //Round up to the minute by setting seconds = 0
        val minute = LocalDateTime.of(
                clickTimestamp.getYear(),
                clickTimestamp.getMonth(),
                clickTimestamp.getDayOfMonth(),
                clickTimestamp.getHour(),
                clickTimestamp.getMinute(),
               0
        );
        return new Tuple3<>(campaignId, pubId, minute);
    }
}
