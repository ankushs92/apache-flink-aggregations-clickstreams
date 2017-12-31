**What is this?** 

This is a sample(but resembles actual production code) on how to do simple counting over streams over a time window using Apache Flink. 

Data is streamed from Apache Kafka, broken down into multiple `DataStream`'s by tuple : `(campaignId, pubId, minute)` where minute is an instance of Java 8 `LocalDateTime` rounded off to the minute. For ex : 2017-01-01 12:12:12 => 2017-01-01 12:12:00. 


TODO : More README

