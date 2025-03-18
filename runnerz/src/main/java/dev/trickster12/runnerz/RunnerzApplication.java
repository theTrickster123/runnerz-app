package dev.trickster12.runnerz;


import dev.trickster12.runnerz.user.User;
import dev.trickster12.runnerz.user.UserHttpClient;
import dev.trickster12.runnerz.user.UserRestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.util.List;


@SpringBootApplication
public class RunnerzApplication {

	private static final Logger log = LoggerFactory.getLogger(RunnerzApplication.class);

	public static void main(String[] args) {

		SpringApplication.run(RunnerzApplication.class, args);

	}


	@Bean
	UserHttpClient userHttpClient() {
		RestClient restClient = RestClient.create("https://jsonplaceholder.typicode.com/");
		HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(RestClientAdapter.create(restClient)).build();
		return factory.createClient(UserHttpClient.class);
	}

	// version with HttpClient Interface
	@Bean
	CommandLineRunner runner(UserHttpClient client) {
		return args -> {
			List<User> users = client.findAll();
			System.out.println(users);

			User user = client.findById(2);
			System.out.println(user);
		};
	}



	// version with RestClient
	@Bean
	CommandLineRunner runner2(UserRestClient client) {
		return args -> {
			List<User> users = client.findAll();
			System.out.println(users);

			User user = client.findById(2);
			System.out.println(user);
		};
	}

}


