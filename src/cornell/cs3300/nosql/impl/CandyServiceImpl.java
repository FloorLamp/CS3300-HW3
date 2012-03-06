package cornell.cs3300.nosql.impl;

import java.net.UnknownHostException;
import java.util.List;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.Key;
import com.google.code.morphia.Morphia;
import com.google.code.morphia.query.Query;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

import cornell.cs3300.nosql.CandyService;
import cornell.cs3300.nosql.ServerException;
import cornell.cs3300.nosql.pojos.Candy;
import cornell.cs3300.nosql.pojos.Customer;

public class CandyServiceImpl implements CandyService {

	private Mongo mongo;
	private Morphia morphia;
	private Datastore ds;
	/**
	 * Constructs the default service implementation, which connects to the
	 * database "candyStoreDB" at the hostname "localhost"
	 */
	public CandyServiceImpl() {
		this("localhost", "candyStoreDB");
	}
	
	public CandyServiceImpl(String dbHost, String dbName) {
		try {
			 mongo = new Mongo(dbHost);
			 morphia = new Morphia();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (MongoException e) {
			e.printStackTrace();
		}
		morphia.map(Candy.class);
		ds = morphia.createDatastore(mongo, dbName);
		ds.ensureIndexes();
		ds.ensureCaps();
	}
	
	@Override
	public Key<Candy> addCandy(String candyName, float sweetness, float viscosity, float sourness, float nuts, float texture)
			throws ServerException {
		ds.save(new Candy(candyName, sweetness, viscosity, sourness, nuts, texture));
		return ds.find(Candy.class, "candyName =", candyName).getKey();
	}
	
	@Override
	public Candy[] getCandy(Integer num, CandySortMethod sortBy, boolean descending) throws ServerException {
		String sort = descending ? "-" : ""; // If descending, prepend '-'
		switch(sortBy) {
		case NAME:
			sort += "candyName";
			break;
		case PURCHASES:
			break;
		case RATING:
			break;
		}
		
		Query<Candy> q = ds.find(Candy.class).order(sort);
		if (num != null)
			q = q.limit(num);
		List<Candy> candies = q.asList();
		Candy[] candy = new Candy[candies.size()];
		int i = 0;
		for (Candy c : candies) {
			candy[i] = c;
			i++;
		}
		return candy;
	}
}
