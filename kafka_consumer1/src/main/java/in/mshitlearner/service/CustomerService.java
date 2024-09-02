package in.mshitlearner.service;

import org.springframework.stereotype.Service;

import in.mshitlearner.binding.Customer;
import in.mshitlearner.util.KafkaConfigConstants;

@Service
public class CustomerService {
	
	public void validateAgeOfCustomer(Customer customer) {
	    if(customer.getAge() == KafkaConfigConstants.age) {
        	 throw new RuntimeException("Below age will not consider for the sales !");
        }
	}
}
