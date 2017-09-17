package hua.world;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import hua.world.po.Customer;
import hua.world.repository.CustomerRepository;

@SpringBootApplication
@Controller
public class Application {

	@Autowired
	private CustomerRepository repository;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
	@RequestMapping("/world/save")
	@ResponseBody
	public List<Customer> save() {
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
	
	@RequestMapping("/world/findAll")
	@ResponseBody
	public List<Customer> findAll() {
		return repository.findAll();
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
}
