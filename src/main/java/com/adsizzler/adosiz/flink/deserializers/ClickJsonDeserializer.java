package com.adsizzler.adosiz.flink.deserializers;

import com.adsizzler.adosiz.flink.domain.Click;
import com.adsizzler.adosiz.flink.utils.Json;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.typeutils.PojoTypeInfo;
import org.apache.flink.streaming.util.serialization.DeserializationSchema;

import java.io.IOException;
import java.util.Objects;

/**
 * Created by Ankush on 02/03/17.
 */
@Slf4j
public class ClickJsonDeserializer implements DeserializationSchema<Click> {

    @Override
    public Click deserialize(final byte[] bytes) throws IOException {
        if(Objects.isNull(bytes)){
            return null;
        }
        Click click = null;
        try{
            val json = new String(bytes);
            click = Json.toObject(json, Click.class);
        }
        catch(final Exception ex){
            log.error("", ex);
        }
        return click;
    }

    @Override
    public boolean isEndOfStream(final Click nextElement) {
        return false;
    }

    //NOTE THIS.
    @Override
    public TypeInformation<Click> getProducedType() {
        return PojoTypeInfo.of(Click.class);
    }
}
