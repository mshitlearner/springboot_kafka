package in.mshitlearner.service;

import org.springframework.stereotype.Service;

import in.mshitlearner.binding.Customer;

@Service
public class CustomerService {
	
	public void validateAgeOfCustomer(Customer customer) {
	    if(customer.getAge() < 18) {
        	 throw new RuntimeException("Under 18 Years age will not consider for the sales !");
        }
	}
}
