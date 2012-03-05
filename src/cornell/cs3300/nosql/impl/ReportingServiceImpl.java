package cornell.cs3300.nosql.impl;

import com.google.code.morphia.Key;

import cornell.cs3300.nosql.ReportingService;
import cornell.cs3300.nosql.ServerException;
import cornell.cs3300.nosql.pojos.Candy;
import cornell.cs3300.nosql.pojos.ClusterCentroid;
import cornell.cs3300.nosql.pojos.Customer;

public class ReportingServiceImpl implements ReportingService {

	/**
	 * Constructs the default service implementation, which connects to the
	 * database "candyStoreDB" at the hostname "localhost"
	 */
	public ReportingServiceImpl() {
		// NOTE: if you're using dependency injection, just call that
		// constructor from here using default values.
	}

	@Override
	public void rateCandy(Key<Customer> userId, Key<Candy> candyId, float rating)
			throws ServerException {
		throw new IllegalStateException("Unimplemented");
	}

	@Override
	public void purchaseCandy(Key<Customer> userId, Key<Candy> candyId)
			throws ServerException {
		throw new IllegalStateException("Unimplemented");
	}

	@Override
	public String[] getCandyPurchased(Key<Customer> userId)
			throws ServerException {
		throw new IllegalStateException("Unimplemented");
	}

	@Override
	public void runClustering(int numSeeds, float minimumRating, int interations)
			throws ServerException {
		throw new IllegalStateException("Unimplemented");
	}

	@Override
	public ClusterCentroid[] getPreferenceClusters() throws ServerException {
		throw new IllegalStateException("Unimplemented");
	}
}
