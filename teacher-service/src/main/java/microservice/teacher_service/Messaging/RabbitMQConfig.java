package microservice.teacher_service.Messaging;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    public static final String TEACHER_EXCHANGE = "teacher.schedule.exchange";
    public static final String TEACHER_QUEUE = "teacher.schedule.queue";
    public static final String TEACHER_CREATE_ROUTING_KEY = "teacher.schedule.create";
    public static final String TEACHER_DELETE_ROUTING_KEY = "teacher.schedule.delete";


    @Bean
    public DirectExchange teacherExchange() {
        return new DirectExchange(TEACHER_EXCHANGE);
    }

    @Bean
    public Queue teacherQueue() {
        return new Queue(TEACHER_QUEUE, true);
    }

    @Bean
    public Binding createBinding(Queue teacherQueue, DirectExchange teacherExchange) {
        return BindingBuilder.bind(teacherQueue)
                .to(teacherExchange)
                .with(RabbitMQConfig.TEACHER_CREATE_ROUTING_KEY);
    }

    @Bean
    public Binding deleteBinding(Queue teacherQueue, DirectExchange teacherExchange) {
        return BindingBuilder.bind(teacherQueue)
                .to(teacherExchange)
                .with(RabbitMQConfig.TEACHER_DELETE_ROUTING_KEY);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
