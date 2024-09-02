package in.mshitlearner.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.mshitlearner.binding.Customer;
import in.mshitlearner.service.CustomerService;

@RestController
@RequestMapping("/customers")
public class CustomerController {

	@Autowired
	public CustomerService customerService;

	@PostMapping(value = "/all")
	public String getAllCustomers(@RequestBody Customer customer) {
		List<Customer> lstCustomers = new ArrayList<>();
		// For Dummy Data
		//for (int i = 1; i <= 5; i++) {
		//	lstCustomers.add(new Customer(i, "Customer-" + i, i , "customer@gmail.com"));
		//}
		lstCustomers.add(customer);
		String result = customerService.publishCustomerObjectMessage(lstCustomers);
		return result;
	}
}
