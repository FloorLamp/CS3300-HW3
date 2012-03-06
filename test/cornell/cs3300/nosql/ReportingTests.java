package cornell.cs3300.nosql;

import static org.junit.Assert.*;

import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Test;

import com.google.code.morphia.Key;
import com.mongodb.Mongo;
import cornell.cs3300.nosql.CandyService.CandySortMethod;
import cornell.cs3300.nosql.impl.CandyServiceImpl;
import cornell.cs3300.nosql.impl.CustomerServiceImpl;
import cornell.cs3300.nosql.impl.ReportingServiceImpl;
import cornell.cs3300.nosql.pojos.Candy;
import cornell.cs3300.nosql.pojos.Customer;

public class ReportingTests {
	
	@Before
	public void clearDatabase() {
		try {
			Mongo mongo = new Mongo("localhost");
			mongo.dropDatabase("candyStoreDB");
		} catch (Exception e) {
		}
	}
	
	@Test
	public void rateCandy() {
		CustomerService us = new CustomerServiceImpl();
		CandyService cs = new CandyServiceImpl();
		ReportingService rs = new ReportingServiceImpl();

		Key<Candy> candyId = null;
		Candy[] candies = null;
		float[] ratings = null;
		
		try {
			Key<Customer> custId = us.addCustomer("Gary");
			candyId = cs.addCandy("Gummy Bears", 0.9f, 0.3f, 0.1f, 0.0f, 0.1f);
			rs.rateCandy(custId, candyId, 0.5f);
			candies = cs.getCandy(null, CandySortMethod.NAME, false);
			ratings = candies[0].getRating();
		} catch (ServerException e) {
			fail("Unexpected exception: " + e.toString());
		}

		System.out.println(ratings[0]);
		assertNotNull(candies);
		assertEquals(1, candies.length);
		assertEquals("Gummy Bears", candies[0].getCandyName());
		assertNotNull(ratings);
		assertEquals(1, ratings.length);
		assertTrue(ratings[0] == 0.5f);
		
		try {
			Key<Customer> custId = us.addCustomer("Professor Oak");
			rs.rateCandy(custId, candyId, 0.2f);
			candies = cs.getCandy(null, CandySortMethod.NAME, false);
			ratings = candies[0].getRating();
		} catch (ServerException e) {
			fail("Unexpected exception: " + e.toString());
		}
		
		System.out.println(ratings[1]);
		assertNotNull(candies);
		assertEquals(1, candies.length);
		assertEquals("Gummy Bears", candies[0].getCandyName());
		assertNotNull(ratings);
		assertEquals(2, ratings.length);
		assertTrue(ratings[1] == 0.2f);
	}	
}
