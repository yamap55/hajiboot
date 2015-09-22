package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.example.domain.Customer;
import com.example.repository.CustomerRepository;

@EnableAutoConfiguration
@ComponentScan
public class App implements CommandLineRunner {
	@Autowired
	CustomerRepository customerRepository;

	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}

	@Override
	public void run(String... arg0) throws Exception {
		Customer created = customerRepository.save(new Customer(null, "Hidetoshi", "Dekisugi"));
		System.out.println(created + "is Created!");

		// ページング
		Pageable pageable = new PageRequest(0, 3);
		Page<Customer> page = customerRepository.findAll(pageable);

		System.out.println("1ページあたりのデータ数=" + page.getSize());
		System.out.println("現在のページ=" + page.getNumber());
		System.out.println("全ページ数=" + page.getTotalPages());
		System.out.println("全データ数=" + page.getTotalElements());
		page.getContent().forEach(System.out::println);

		// customerRepository.findAllOrderByName().forEach(System.out::println);
	}
}
