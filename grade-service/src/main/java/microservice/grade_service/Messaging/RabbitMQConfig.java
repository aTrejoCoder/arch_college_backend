package microservice.grade_service.Messaging;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    public static final String AH_EXCHANGE = "academic.history.exchange";
    public static final String AH_QUEUE = "academic.history.queue";
    public static final String ROUTING_AH_KEY = "academic.history.create";

    public static final String EG_EXCHANGE = "enrollment.grade.exchange";
    public static final String EG_QUEUE = "enrollment.grade.queue";
    public static final String ROUTING_EG_KEY = "enrollment.grade.init";

    // Academic History Exchange and Queue
    @Bean
    public TopicExchange academicHistoryExchange() { // Change to TopicExchange
        return new TopicExchange(AH_EXCHANGE);
    }

    @Bean
    public Queue academicHistoryQueue() {
        return new Queue(AH_QUEUE, true, false, false);
    }

    @Bean
    public Binding binding(Queue academicHistoryQueue, TopicExchange academicHistoryExchange) {
        return BindingBuilder.bind(academicHistoryQueue)
                .to(academicHistoryExchange)
                .with(ROUTING_AH_KEY);
    }

    // Enrollment Grade Exchange and Queue
    @Bean
    public TopicExchange enrollmentGradeExchange() {
        return new TopicExchange(EG_EXCHANGE);
    }

    @Bean
    public Queue enrollmentGradeQueue() {
        return new Queue(EG_QUEUE, true, false, false);
    }

    @Bean
    public Binding enrollmentGradeBinding(Queue enrollmentGradeQueue, TopicExchange enrollmentGradeExchange) {
        return BindingBuilder.bind(enrollmentGradeQueue)
                .to(enrollmentGradeExchange)
                .with(ROUTING_EG_KEY);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        rabbitTemplate.setExchange(AH_EXCHANGE);
        rabbitTemplate.setRoutingKey(ROUTING_EG_KEY);
        return rabbitTemplate;
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
