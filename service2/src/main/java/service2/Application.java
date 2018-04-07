package service2;

import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
@RestController
@SpringBootApplication
public class Application {

	private static final Logger log = LoggerFactory.getLogger(Application.class);
	private final AtomicLong counter = new AtomicLong();

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/api2")
	public Message echo() {
		return echo("service2");
	}

	
	@RequestMapping(method = RequestMethod.GET, value = "/api2/{message}")
	public Message echo(@PathVariable String message) {
		Message data = new Message(counter.incrementAndGet(), message);
		log.info(data.toString());
		return data;
	}
}