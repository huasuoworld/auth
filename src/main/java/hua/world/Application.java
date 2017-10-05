package hua.world;

import java.util.List;

import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import hua.world.mq.ChatAnnotation;
import hua.world.po.Customer;
import hua.world.repository.CustomerRepository;
import hua.world.ssl.HttpClientUtils;

@SpringBootApplication
@Controller
@ComponentScan(basePackages="hua.world")
@WebFilter
@EnableScheduling
//@ImportResource(locations={"classpath:client-context.xml"})
//@CrossOrigin(allowCredentials="true", origins="http://localhost:4201")
public class Application {
	
	@Autowired
	private CustomerRepository repository;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
	@RequestMapping("/world/save")
	@ResponseBody
	public List<Customer> save() {
		FilterRegistrationBean a = null;
		// save a couple of customers
		repository.save(new Customer("Alice", "Smith"));
		repository.save(new Customer("Bob", "Smith"));

		// fetch all customers
		System.out.println("Customers found with findAll():");
		System.out.println("-------------------------------");
		for (Customer customer : repository.findAll()) {
			System.out.println(customer);
		}
		System.out.println();
		// fetch an individual customer
		System.out.println("Customer found with findByFirstName('Alice'):");
		System.out.println("--------------------------------");
		System.out.println(repository.findByFirstName("Alice"));

		System.out.println("Customers found with findByLastName('Smith'):");
		System.out.println("--------------------------------");
		for (Customer customer : repository.findByLastName("Smith")) {
			System.out.println(customer);
		}

		return repository.findAll();
	}
	
	@Autowired
	private ChatAnnotation chatAnnotation;
	
	@RequestMapping("/world/findAll")
	@ResponseBody
	public List<Customer> findAll(HttpServletRequest request, HttpServletResponse response) {
		List<Customer> list = repository.findAll();
		System.out.println("findAll" + list);
		chatAnnotation.sendAll(System.currentTimeMillis()+"findAll>>"+list);
		return list;
	}
	
	@RequestMapping("/world/findByFirstName")
	@ResponseBody
	public Customer findByFirstName(String firstName) {
		return repository.findByFirstName(firstName);
	}
	
	@RequestMapping("/world/findByLastName")
	@ResponseBody
	public Customer findByLastName(String lastName) {
		return repository.findByFirstName(lastName);
	}
	
	@Bean
	public FilterRegistrationBean filterBean() {
		FilterRegistrationBean bean = new FilterRegistrationBean();
		bean.setFilter(new AccessFilter());
		bean.setName("accessFilter");
		bean.addUrlPatterns("/world/*");
		return bean;
	}
	
	@Bean
	public HttpComponentsClientHttpRequestFactory factory() {
		try {
			CloseableHttpClient httpClient = HttpClientUtils.acceptsUntrustedCertsHttpClient();
			HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
			return clientHttpRequestFactory;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Bean
	public RestTemplate restTemp() {
		RestTemplate restTemplate = new RestTemplate(factory());
		return restTemplate;
	}
	
	@Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
}
