package com.adsizzler.adosiz.flink.serializers;

import com.adsizzler.adosiz.flink.aggregations.AggregatedClicksByMinute;
import com.adsizzler.adosiz.flink.utils.Json;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.flink.streaming.util.serialization.SerializationSchema;

import java.util.Objects;

/**
 * Created by Ankush on 06/03/17.
 */
@Slf4j
public class JsonSerializer implements SerializationSchema<AggregatedClicksByMinute> {

    @Override
    public byte[] serialize(final AggregatedClicksByMinute pojo) {
       byte[] result = new byte[0];
       if(Objects.nonNull(pojo)){
           try{
               val json = Json.toJson(pojo);
               result = json.getBytes();
           }
           catch(final Exception ex){
               log.error("", ex);
           }
       }
        return result;
    }
}
