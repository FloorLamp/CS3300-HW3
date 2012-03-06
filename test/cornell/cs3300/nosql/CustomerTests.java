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
			customers = service.getCustomers(null, CustomerSortMethod.NAME,	false);
		} catch (ServerException e) {
			fail("Unexpected exception: " + e.toString());
		}
		assertNotNull(customers);
		assertEquals(1, customers.length);
		assertEquals("Gary", customers[0].getName());
	}
	
	@Test
	public void addMultipleCustomers() {
		CustomerService service = new CustomerServiceImpl();

		try {
			service.addCustomer("Harold");
			service.addCustomer("Kumar");
			service.addCustomer("Gandalf");
			service.addCustomer("Frodo");
		} catch (ServerException e) {
			fail("Unexpected exception: " + e.toString());
		}

		Customer[] customers = null;
		try {
			customers = service.getCustomers(null, CustomerSortMethod.JOIN_DATE, false);
		} catch (ServerException e) {
			fail("Unexpected exception: " + e.toString());
		}
		assertNotNull(customers);
		assertEquals(4, customers.length);
		assertEquals("Harold", customers[0].getName());
		assertEquals("Kumar", customers[1].getName());
		assertEquals("Gandalf", customers[2].getName());
		assertEquals("Frodo", customers[3].getName());
	}
	
	@Test
	public void sortByName() {
		CustomerService service = new CustomerServiceImpl();

		try {
			service.addCustomer("Bob");
			service.addCustomer("Alice");
			service.addCustomer("Zoolander");
			service.addCustomer("Carol");
		} catch (ServerException e) {
			fail("Unexpected exception: " + e.toString());
		}

		Customer[] customers = null;
		try {
			customers = service.getCustomers(null, CustomerSortMethod.NAME, false);
		} catch (ServerException e) {
			fail("Unexpected exception: " + e.toString());
		}
		assertNotNull(customers);
		assertEquals(4, customers.length);
		assertEquals("Alice", customers[0].getName());
		assertEquals("Bob", customers[1].getName());
		assertEquals("Carol", customers[2].getName());
		assertEquals("Zoolander", customers[3].getName());

		try {
			customers = service.getCustomers(null, CustomerSortMethod.NAME, true);
		} catch (ServerException e) {
			fail("Unexpected exception: " + e.toString());
		}
		assertNotNull(customers);
		assertEquals(4, customers.length);
		assertEquals("Zoolander", customers[0].getName());
		assertEquals("Carol", customers[1].getName());
		assertEquals("Bob", customers[2].getName());
		assertEquals("Alice", customers[3].getName());
	}
	
	@Test
	public void limitResults() {
		CustomerService service = new CustomerServiceImpl();

		try {
			service.addCustomer("Harry");
			service.addCustomer("Hermione");
			service.addCustomer("Ron");
			service.addCustomer("Voldemort");
			service.addCustomer("Tom Riddle");
		} catch (ServerException e) {
			fail("Unexpected exception: " + e.toString());
		}

		Customer[] customers = null;
		try {
			customers = service.getCustomers(3, CustomerSortMethod.JOIN_DATE, false);
		} catch (ServerException e) {
			fail("Unexpected exception: " + e.toString());
		}
		assertNotNull(customers);
		assertEquals(3, customers.length);
		assertEquals("Harry", customers[0].getName());
		assertEquals("Hermione", customers[1].getName());
		assertEquals("Ron", customers[2].getName());
	}
}
