package cornell.cs3300.nosql.impl;

import com.google.code.morphia.Key;

import cornell.cs3300.nosql.CandyService;
import cornell.cs3300.nosql.ServerException;
import cornell.cs3300.nosql.pojos.Candy;

public class CandyServiceImpl implements CandyService {

	/**
	 * Constructs the default service implementation, which connects to the
	 * database "candyStoreDB" at the hostname "localhost"
	 */
	public CandyServiceImpl() {
		// NOTE: if you're using dependency injection, just call that
		// constructor from here using default values.
	}
	
	@Override
	public Key<Candy> addCandy(String candyName, float sweetness,
			float viscosity, float sourness, float nuts, float texture)
			throws ServerException {
		throw new IllegalStateException("Unimplemented");
	}
	
	@Override
	public Candy[] getCandy(Integer num, CandySortMethod sortBy,
			boolean descending) throws ServerException {
		throw new IllegalStateException("Unimplemented");
	}
}
