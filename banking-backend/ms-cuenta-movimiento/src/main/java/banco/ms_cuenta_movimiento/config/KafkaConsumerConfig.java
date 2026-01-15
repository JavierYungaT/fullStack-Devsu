package banco.ms_cuenta_movimiento.config;


//import org.apache.kafka.clients.consumer.ConsumerConfig;
//import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//import org.springframework.kafka.annotation.EnableKafka;
//import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
//import org.springframework.kafka.core.ConsumerFactory;
//import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
//import org.springframework.kafka.support.serializer.JsonDeserializer;
import banco.ms_cuenta_movimiento.events.Event;

import java.util.HashMap;
import java.util.Map;

@Configuration
//@EnableKafka
public class KafkaConsumerConfig {

//    private final String bootstrapAddress = "kafka:9092";
//
//    @Bean
//    public ConsumerFactory<String, Event<?>> consumerFactory() {
//        Map<String, Object> props = new HashMap<>();
//        props.put(
//                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
//                bootstrapAddress);
//        props.put(
//                ConsumerConfig.GROUP_ID_CONFIG,"customers");
//        props.put(
//                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
//                StringDeserializer.class);
//        props.put(
//                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
//                JsonDeserializer.class);
//
//        JsonDeserializer<Event<?>> jsonDeserializer = new JsonDeserializer<>(Event.class, false);
//        jsonDeserializer.addTrustedPackages("com.banco.ms-cliente-persona.events", "com.banco.ms-cuenta-movimiento.events");
//        jsonDeserializer.setUseTypeHeaders(false);
//
//        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), jsonDeserializer);
//    }
//
//    @Bean
//    public ConcurrentKafkaListenerContainerFactory<String, Event<?>> kafkaListenerContainerFactory() {
//        ConcurrentKafkaListenerContainerFactory<String, Event<?>> factory =
//                new ConcurrentKafkaListenerContainerFactory<>();
//        factory.setConsumerFactory(consumerFactory());
//        return factory;
//    }

}
