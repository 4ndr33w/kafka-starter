# Read Me First
Kafka-starter. С конвертером

### Чтобы использовать требуется: 

#### Разрешить переопределение бинов в конфигурации: 
    spring.main.allow-bean-definition-overriding=true

#### отключаем автоконфигурацию бинов в приложении для используемого стартера
    @SpringBootApplication(exclude = {
    org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration.class
    })
    public class Application {

	    public static void main(String[] args) {
		    SpringApplication.run(Application.class, args);
	    }
    }

### Чтобы приложение подписалось на получение событий
Требуется на главный класс приложени повесить аннотация @EnableKafka
Для отправки сообщений не требуется, только для приема

    @EnableKafka
    @SpringBootApplication(exclude = {
    org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration.class
    })
    public class Application {

	    public static void main(String[] args) {
		    SpringApplication.run(Application.class, args);
	    }
    }

# Producer

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;
    @Autowired
    private KafkaMessageConverter kafkaMessageConverter;

    public void publish() {

        User user = new User();
        user.setEmail("testEmail");
        user.setId((long)12345131);
        user.setName("testName");

        // toKafkaMessage("topicName", "key", object)
        var message = kafkaMessageConverter.toKafkaMessage("test-topic", user.getId().toString(), user);
        kafkaTemplate.send(message);
    }

# Consumer

    @Autowired
    private KafkaMessageConverter kafkaMessageConverter;

    @KafkaHandler
    @KafkaListener(
            topics = "${properties.kafka.consumer.topic}",
            groupId = "${properties.kafka.consumer.group-id}")
    public User listen(Message<byte[]> message) throws Exception {

        // передаем помимо самого собщения сам класс
        User user = kafkaMessageConverter.fromKafkaMessage(message, User.class);

        return user;
    }

#### Все готово к использованию