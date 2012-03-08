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

import cornell.cs3300.nosql.CandyService;
import cornell.cs3300.nosql.ServerException;
import cornell.cs3300.nosql.ServerException.ErrorType;
import cornell.cs3300.nosql.pojos.Candy;

public class CandyServiceImpl implements CandyService {

	private static final Logger log = LoggerFactory.getLogger(CandyServiceImpl.class);
	
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
			log.error("Unknown host");
			e.printStackTrace();
		} catch (MongoException e) {
			e.printStackTrace();
		}
		morphia.map(Candy.class);
		ds = morphia.createDatastore(mongo, dbName);
		ds.ensureIndexes();
		ds.ensureCaps();
		
		log.info("Connected to " + dbName + " on " + dbHost);
	}
	
	@Override
	public Key<Candy> addCandy(String candyName, float sweetness, float viscosity, float sourness, float nuts, float texture)
			throws ServerException {
		if (candyName.trim().equals("")) {
			log.error("Candy name invalid");
			throw new ServerException(ErrorType.INVALID_INPUT, "Candy name cannot be empty");
		}
		if (sweetness < 0 || sweetness > 1) {
			log.error("Sweetness invalid");
			throw new ServerException(ErrorType.INVALID_INPUT, "Sweetness must be between 0 and 1");
		}
		if (viscosity < 0 || viscosity > 1) {
			log.error("Viscosity invalid");
			throw new ServerException(ErrorType.INVALID_INPUT, "Viscosity must be between 0 and 1");
		}
		if (sourness < 0 || sourness > 1) {
			log.error("Sourness invalid");
			throw new ServerException(ErrorType.INVALID_INPUT, "Sourness must be between 0 and 1");
		}
		if (nuts < 0 || nuts > 1) {
			log.error("Nuts invalid");
			throw new ServerException(ErrorType.INVALID_INPUT, "Nuts must be between 0 and 1");
		}
		if (texture < 0 || texture > 1) {
			log.error("Texture invalid");
			throw new ServerException(ErrorType.INVALID_INPUT, "Texture must be between 0 and 1");
		}
		
		ds.save(new Candy(candyName, sweetness, viscosity, sourness, nuts, texture));
		log.info("Added new candy " + candyName + " with sweetness " + sweetness 
				+ ", viscosity " + viscosity + ", sourness " + sourness + ", nuts " + nuts + ", texture " + texture);
		return ds.find(Candy.class, "candyName =", candyName).getKey();
	}
	
	@Override
	public Candy[] getCandy(Integer num, CandySortMethod sortBy, boolean descending) throws ServerException {
		if (num != null && num <= 0) {
			log.error("Number invalid");
			throw new ServerException(ErrorType.INVALID_INPUT, "Number to get must be greater than 0");			
		}
		
		String sort = descending ? "-" : ""; // If descending, prepend '-'
		switch(sortBy) {
		case NAME:
			sort += "candyName";
			break;
		case PURCHASES:
			sort += "purchases";
			break;
		case RATING:
			sort += "avgRating";
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
		log.info("Getting " + ((num == null) ? "all" : num) + 
				" candies sorted by " + sortBy + ", " + (descending ? "descending" : "ascending"));
		return candy;
	}
}
