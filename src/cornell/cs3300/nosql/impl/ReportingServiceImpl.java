package cornell.cs3300.nosql.impl;

import java.net.UnknownHostException;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.Key;
import com.google.code.morphia.Morphia;
import com.google.code.morphia.query.UpdateOperations;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

import cornell.cs3300.nosql.ReportingService;
import cornell.cs3300.nosql.ServerException;
import cornell.cs3300.nosql.ServerException.ErrorType;
import cornell.cs3300.nosql.pojos.Candy;
import cornell.cs3300.nosql.pojos.ClusterCentroid;
import cornell.cs3300.nosql.pojos.Customer;

public class ReportingServiceImpl implements ReportingService {

	private Mongo mongo;
	private Morphia morphia;
	private Datastore ds;
	/**
	 * Constructs the default service implementation, which connects to the
	 * database "candyStoreDB" at the hostname "localhost"
	 */
	public ReportingServiceImpl() {
		this("localhost", "candyStoreDB");
	}
	
	public ReportingServiceImpl(String dbHost, String dbName) {
		try {
			 mongo = new Mongo(dbHost);
			 morphia = new Morphia();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (MongoException e) {
			e.printStackTrace();
		}
		ds = morphia.createDatastore(mongo, dbName);
		ds.ensureIndexes();
		ds.ensureCaps();
	}

	@Override
	public void rateCandy(Key<Customer> userId, Key<Candy> candyId, float rating) throws ServerException {
		if (ds.get(Customer.class, userId.getId()) == null) {
			throw new ServerException(ErrorType.CUSTOMER_NOT_FOUND, "Customer not found in database");
		}
		if (ds.get(Candy.class, candyId.getId()) == null) {
			throw new ServerException(ErrorType.CANDY_NOT_FOUND, "Candy not found in database");
		} 
		if (rating < 0.0f || rating > 1.0f) {
			throw new ServerException(ErrorType.INVALID_INPUT, "Rating must be between 0 and 1");
		}
		UpdateOperations<Candy> ops = ds.createUpdateOperations(Candy.class).add("rating", rating);
		ds.update(candyId, ops);
	}

	@Override
	public void purchaseCandy(Key<Customer> userId, Key<Candy> candyId) throws ServerException {
		Candy candy = ds.get(Candy.class, candyId.getId());
		Customer customer = ds.get(Customer.class, userId.getId());
		if (customer == null) {
			throw new ServerException(ErrorType.CUSTOMER_NOT_FOUND, "Customer not found in database");
		}
		if (candy == null) {
			throw new ServerException(ErrorType.CANDY_NOT_FOUND, "Candy not found in database");
		}
		UpdateOperations<Customer> addCandy = ds.createUpdateOperations(Customer.class).add("purchases", candy.getCandyName());
		ds.update(customer, addCandy);
		
		UpdateOperations<Candy> incPurchases = ds.createUpdateOperations(Candy.class).inc("purchases");
		ds.update(candy, incPurchases);
	}

	@Override
	public String[] getCandyPurchased(Key<Customer> userId) throws ServerException {
		Customer customer = ds.get(Customer.class, userId.getId());
		if (customer == null) {
			throw new ServerException(ErrorType.CUSTOMER_NOT_FOUND, "Customer not found in database");
		}
		return customer.getPurchases();
	}

	@Override
	public void runClustering(int numSeeds, float minimumRating, int interations) throws ServerException {
		throw new IllegalStateException("Unimplemented");
	}

	@Override
	public ClusterCentroid[] getPreferenceClusters() throws ServerException {
		throw new IllegalStateException("Unimplemented");
	}
}
