package microservice.enrollment_service.Messaging.RabbitMQConfig;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE_GRADE = "enrollment.grade.exchange";
    public static final String QUEUE_GRADE = "enrollment.grade.queue";
    public static final String ROUTING_KEY = "enrollment.grade.init";

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(EXCHANGE_GRADE);
    }

    @Bean
    public Queue gradeQueue() {
        return new Queue(QUEUE_GRADE);
    }

    @Bean
    public Binding binding(Queue gradeQueue, TopicExchange topicExchange) {
        return BindingBuilder.bind(gradeQueue).to(topicExchange).with(ROUTING_KEY);
    }

    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(jsonMessageConverter());
        return factory;
    }
}
