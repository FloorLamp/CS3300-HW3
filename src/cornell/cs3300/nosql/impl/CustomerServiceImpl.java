package cornell.cs3300.nosql.impl;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.Key;
import com.google.code.morphia.Morphia;

import cornell.cs3300.nosql.CustomerService;
import cornell.cs3300.nosql.ServerException;
import cornell.cs3300.nosql.pojos.Customer;

public class CustomerServiceImpl implements CustomerService {

	Datastore ds;
	/**
	 * Constructs the default service implementation, which connects to the
	 * database "candyStoreDB" at the hostname "localhost"
	 */
	public CustomerServiceImpl() {
		// NOTE: if you're using dependency injection, just call that
		// constructor from here using default values.
		ds = new Morphia().createDatastore("candyStoreDB");
		ds.ensureIndexes();
		ds.ensureCaps();
	}

	@Override
	public Key<Customer> addCustomer(String userName) throws ServerException {
		ds.save(new Customer(userName));
		return ds.find(Customer.class, "name =", userName).getKey();
	}

	@Override
	public Customer[] getCustomers(Integer num, CustomerSortMethod sortBy, boolean descending) throws ServerException {
		return (Customer[])ds.find(Customer.class).asList().toArray();
	}
}
