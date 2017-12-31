**What is this?** 

This is a sample(but resembles actual production code) on how to do simple counting over a time window using Apache Flink. 
Go directly to `StreamingJob` to see what's happening. Detailed comments are there, shouldn't be hard to follow.

Overview of what's happening underneath:

1. Data is streamed from Apache Kafka. Each click object in the stream is a `JSON` string. Deserialize JSON to POJO as shown in [this](https://github.com/ankushs92/apache-flink-aggregations-clickstreams/blob/master/src/main/java/in/ankushs/sample/flink/deserializers/ClickJsonDeserializer.java) class
2. Group `DataStream<Click>` by a key, which is a n-tuple of fields. You create your own Key by implementing `KeySelector` interface. In our case, our 3-tuple is : `(campaignId, pubId, minute)` where minute is an instance of Java 8 `LocalDateTime` rounded off to the minute. For ex : 2017-01-01 12:12:12 => 2017-01-01 12:12:00. 
3. Define your `TimeWindow`. Check [this](https://flink.apache.org/news/2015/12/04/Introducing-windows.html) article for windowing semantics in Apache Flink
4. This is perhaps the most important step. You now have with you a collection of objects in a defined time window. You can all sorts of reductions here. We are concerned with just counting the number of clicks in our time window, as shown [here](https://github.com/ankushs92/apache-flink-aggregations-clickstreams/blob/master/src/main/java/in/ankushs/sample/flink/aggregations/ClickWindowCountFunction.java) .
5. Serialize to JSON and send the message to Kafka!

Similarly, you can create different WindowedStream's and perform counting operations such as :

1. Count clicks for a country, city and state over a time window of 1 minute. The key in such a case would be : (campaignId, pubId, minute, country, city, state).

2. Count clicks for different devices. Key : (campaignId, pubId, minute, platform, platformVersion)

