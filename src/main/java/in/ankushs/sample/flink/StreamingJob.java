package in.ankushs.sample.flink;


import in.ankushs.sample.flink.aggregations.AggregatedClicksByMinute;
import in.ankushs.sample.flink.aggregations.ClickWindowAggregationFunction;
import in.ankushs.sample.flink.constants.KafkaTopics;
import in.ankushs.sample.flink.deserializers.ClickJsonDeserializer;
import in.ankushs.sample.flink.domain.Click;
import in.ankushs.sample.flink.keys.AggregatedClicksByMinuteKeySelector;
import in.ankushs.sample.flink.serializers.JsonSerializer;
import lombok.val;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer08;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer08;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Properties;



public class StreamingJob {

	public static void main(String[] args) throws Exception {
		val flinkEnv = StreamExecutionEnvironment.getExecutionEnvironment();

		//Each click in the stream is a JSON string as bytes
		val jsonDeserializer =  new ClickJsonDeserializer();

		//Kafka is the source of the stream
		val kafkaConsumer = new FlinkKafkaConsumer08<Click>(KafkaTopics.CLICKS, jsonDeserializer, kafkaProperties());

		//Stream from Kafka
		val clicksStream = flinkEnv.addSource(kafkaConsumer);

		//Operations will be performed on this window, where the size of each window is 1 minute
		//Multifurcate the clickstream by a Key, which is nothing but a tuple of fields. Flink provides Tuple's implementation upto 25 fields
		//For example, here we have choosen to split a stream on the basis of (campaignId, pubId, timestamp)
		//Also, for our timestamp's value in the key, we round up the actual timestamp to the minute. For example, if our click timestamp is 2017-10-10 12:12:12, we round it up to 2017-10-10 12:12:00
		val clicksWindowedStream = clicksStream
					.keyBy(new AggregatedClicksByMinuteKeySelector())
					.timeWindow(Time.minutes(1));

		// The aggregations bit
		// Our aggregation function does a count on the click objects in the windowed stream
		val aggregatedClicksByMinuteStream = clicksWindowedStream
					.apply(new ClickWindowAggregationFunction())
					.name("Aggregate clicks by minute");

		//Serialize the result AggregatedClicksByMinute POJO to JSON
		val jsonSerializer = new JsonSerializer();
		val kafkaProducer = new FlinkKafkaProducer08<AggregatedClicksByMinute>(KafkaTopics.AGGREGATED_CLICKS, jsonSerializer, kafkaProperties());

		//Sink
		aggregatedClicksByMinuteStream.addSink(kafkaProducer);

     	// execute program
		flinkEnv.execute("Counting clicks in a click stream over a time window");
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

