package cornell.cs3300.nosql.impl;

import java.net.UnknownHostException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.Key;
import com.google.code.morphia.Morphia;
import com.google.code.morphia.query.Query;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

import cornell.cs3300.nosql.CustomerService;
import cornell.cs3300.nosql.ServerException;
import cornell.cs3300.nosql.ServerException.ErrorType;
import cornell.cs3300.nosql.pojos.Customer;

public class CustomerServiceImpl implements CustomerService {

	private static final Logger log = LoggerFactory.getLogger(CustomerServiceImpl.class);
	
	private Mongo mongo;
	private Morphia morphia;
	private Datastore ds;
	/**
	 * Constructs the default service implementation, which connects to the
	 * database "candyStoreDB" at the hostname "localhost"
	 */
	public CustomerServiceImpl() {
		// NOTE: if you're using dependency injection, just call that
		// constructor from here using default values.
		this("localhost", "candyStoreDB");
	}
	
	public CustomerServiceImpl(String dbHost, String dbName) {
		try {
			 mongo = new Mongo(dbHost);
			 morphia = new Morphia();
		} catch (UnknownHostException e) {
			log.error("Unknown host");
			e.printStackTrace();
		} catch (MongoException e) {
			e.printStackTrace();
		}
		morphia.map(Customer.class);
		ds = morphia.createDatastore(mongo, dbName);
		ds.ensureIndexes();
		ds.ensureCaps();
		
		log.info("Connected to " + dbName + " on " + dbHost);
	}

	@Override
	public Key<Customer> addCustomer(String userName) throws ServerException {
		if (userName.trim().equals("")) {
			log.error("Customer invalid");
			throw new ServerException(ErrorType.INVALID_INPUT, "User name cannot be empty");
		}
		ds.save(new Customer(userName.trim()));
		log.info("Added new customer " + userName);
		return ds.find(Customer.class, "name =", userName).getKey();
	}

	@Override
	public Customer[] getCustomers(Integer num, CustomerSortMethod sortBy, boolean descending) throws ServerException {
		if (num != null && num <= 0) {
			log.error("Number invalid");
			throw new ServerException(ErrorType.INVALID_INPUT, "Number to get must be greater than 0");			
		}
		
		String sort = descending ? "-" : ""; // If descending, prepend '-'
		switch(sortBy) {
		case JOIN_DATE:
			sort += "joinDate";
			break;
		case NAME:
			sort += "name";
			break;
		case PURCHASES:
			sort += "purchaseCount";
			break;
		}
		Query<Customer> q = ds.find(Customer.class).order(sort);
		if (num != null)
			q = q.limit(num);
		List<Customer> customers = q.asList();
		Customer[] customer = new Customer[customers.size()];
		int i = 0;
		for (Customer c : customers) {
			customer[i] = c;
			i++;
		}
		log.info("Getting " + ((num == null) ? "all" : num) + 
				" customers sorted by " + sortBy + ", " + (descending ? "descending" : "ascending"));
		return customer;
	}
}
