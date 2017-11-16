package com.adsizzler.adosiz.flink.jobs;


import com.adsizzler.adosiz.flink.aggregations.AggregatedClicksByMinute;
import com.adsizzler.adosiz.flink.aggregations.ClickWindowAggregationFunction;
import com.adsizzler.adosiz.flink.deserializers.ClickJsonDeserializer;
import com.adsizzler.adosiz.flink.domain.Click;
import com.adsizzler.adosiz.flink.keys.AggregatedClicksByMinuteKeySelector;
import com.adsizzler.adosiz.flink.serializers.JsonSerializer;
import lombok.val;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer08;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer08;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

import static com.adsizzler.adosiz.flink.constants.KafkaTopics.AGGREGATED_CLICKS;
import static com.adsizzler.adosiz.flink.constants.KafkaTopics.CLICKS;


public class StreamingJob {

	private static final Logger logger = LoggerFactory.getLogger(StreamingJob.class);


	public static void main(String[] args) throws Exception {
		val env = StreamExecutionEnvironment.getExecutionEnvironment();

		//Each click in the stream is a JSON string as bytes
		val jsonDeserializer =  new ClickJsonDeserializer();

		val kafkaConsumer = new FlinkKafkaConsumer08<Click>(CLICKS, jsonDeserializer, kafkaProperties());

		val clicksStream = env.addSource(kafkaConsumer);

		//Operations will be performed on this window, where the size of each window is 1 minute
		//Split the clickstream by Key over a Time window of 1 minute
		val clicksWindowedStream = clicksStream
					.keyBy(new AggregatedClicksByMinuteKeySelector())
					.timeWindow(Time.minutes(1));

		// The aggregations bit
		val aggregatedClicksByMinuteStream = clicksWindowedStream
					.apply(new ClickWindowAggregationFunction())
					.name("Aggregate clicks by minute");

		//Serialize the result AggregatedClicksByMinute POJO to JSON
		val jsonSerializer = new JsonSerializer();
		val kafkaProducer = new FlinkKafkaProducer08<AggregatedClicksByMinute>(AGGREGATED_CLICKS, jsonSerializer, kafkaProperties());

		//Sink
		aggregatedClicksByMinuteStream.addSink(kafkaProducer);

     	// execute program
		env.execute("Ad tracking simple computations over a time window(like counting, summing etc)");
	}

	private static Properties kafkaProperties(){
		val properties = new Properties();
		properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
				StringDeserializer.class);
		properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
				ByteArrayDeserializer.class);
		properties.setProperty("zookeeper.connect", "localhost:2181"); // Zookeeper default host:port
		properties.setProperty("bootstrap.servers", "localhost:9092"); // Broker default host:port
		properties.setProperty("group.id", "flink-streams-consumer");
		return properties;
	}

}

