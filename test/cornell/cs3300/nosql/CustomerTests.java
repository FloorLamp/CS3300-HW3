package cornell.cs3300.nosql;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.mongodb.Mongo;
import cornell.cs3300.nosql.CustomerService.CustomerSortMethod;
import cornell.cs3300.nosql.impl.CustomerServiceImpl;
import cornell.cs3300.nosql.pojos.Customer;

public class CustomerTests {
	

	@Before
	public void clearDatabase() {
		try {
			Mongo mongo = new Mongo("localhost");
			mongo.dropDatabase("candyStoreDB");
		} catch (Exception e) {
		}
	}
	
	@Test
	public void addOneCustomer() {
		CustomerService service = new CustomerServiceImpl();

		try {
			service.addCustomer("Gary");
		} catch (ServerException e) {
			fail("Unexpected exception: " + e.toString());
		}

		Customer[] customers = null;
		try {
			customers = service.getCustomers(null, CustomerSortMethod.NAME,	true);
		} catch (ServerException e) {
			fail("Unexpected exception: " + e.toString());
		}
		assertNotNull(customers);
		assertEquals(1, customers.length);
		assertEquals("Gary", customers[0].getName());
	}
}
