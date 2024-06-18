package pack1;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.clients.producer.RecordMetadata;
// import org.apache.kafka.clients.producer.RoundRobinPartitioner;
import org.apache.kafka.clients.producer.Callback;
import java.util.Properties;

public class ProducerStickPartitionerExample {
    public static void main(String[] args) {
        // create Producer Properties & Setting up properties
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "127.0.0.1:9092");
        properties.setProperty("key.serializer", StringSerializer.class.getName());
        properties.setProperty("value.serializer", StringSerializer.class.getName());

        //  // add batch size
        // properties.setProperty("batch.size", "2");

        // // configure partitioner
        // properties.setProperty("partitioner.class",RoundRobinPartitioner.class.getName());

        // create the Producer
        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);

        // create a Producer Record
        ProducerRecord<String, String> producerRecord = new ProducerRecord<>("demo_java", "message-1");

        // send data
        for (int i=0; i<5; i++) {
            producer.send(producerRecord, new Callback() {
                @Override
                public void onCompletion(RecordMetadata metadata, Exception e) {
                    // executes every time a record successfully sent or an exception is thrown
                    if (e == null) {
                        // the record was successfully sent
                        System.out.println("Received new metadata \n" +
                                "Topic: " + metadata.topic() + "|" +  "Partition: " + metadata.partition() + "|" + "Offset: " + metadata.offset() + "|" +
                                "Timestamp: " + metadata.timestamp());
                    } else {
                        System.out.println("Error while producing"+e);
                    }
                }
            });

            try {Thread.sleep(500);} 
            catch (InterruptedException e) {e.printStackTrace(); }
        }

        // flush and close
        producer.flush();
        producer.close();
    }
}
