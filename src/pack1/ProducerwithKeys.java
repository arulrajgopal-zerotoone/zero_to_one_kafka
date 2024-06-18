package pack1;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.clients.producer.Callback;
import java.util.Properties;
import java.util.Random;

public class ProducerwithKeys {

    public static void main(String[] args) {

        // create Producer Properties & Setting up properties
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "127.0.0.1:9092");
        properties.setProperty("key.serializer", StringSerializer.class.getName());
        properties.setProperty("value.serializer", StringSerializer.class.getName());

        // create the Producer
        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);
        Random random = new Random();

        for (int i=0; i<10; i++) {

            String rand_num_converted_str = String.valueOf(random.nextInt(6));
            String topic = "demo_java"; String key = rand_num_converted_str; String value = "hello world " + i; 
            
            // create a Producer Record
            ProducerRecord<String, String> producerRecord = new ProducerRecord<>(topic, key, value);

            producer.send(producerRecord, new Callback() {
                @Override
                public void onCompletion(RecordMetadata metadata, Exception e) {
                    // executes every time a record successfully sent or an exception is thrown
                    if (e == null) {
                        // the record was successfully sent
                        System.out.println("Received new metadata \n" +
                                "Topic: " + metadata.topic() + "|" +  "Partition: " + metadata.partition() + "|" + "Offset: " + metadata.offset() + "|" +
                                "Key: " + key + "|" +  "Timestamp: " + metadata.timestamp());
                    } else {
                        System.out.println("Error while producing"+e);
                    }
                }
            });

            try {Thread.sleep(500);} 
            catch (InterruptedException e) {e.printStackTrace();
            }
        }

        // flush and close
        producer.flush();
        producer.close();
        
    }
}
