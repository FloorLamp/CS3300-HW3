package cornell.cs3300.nosql;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import com.google.code.morphia.Key;
import com.mongodb.Mongo;
import cornell.cs3300.nosql.CandyService.CandySortMethod;
import cornell.cs3300.nosql.CustomerService.CustomerSortMethod;
import cornell.cs3300.nosql.ServerException.ErrorType;
import cornell.cs3300.nosql.impl.CandyServiceImpl;
import cornell.cs3300.nosql.impl.CustomerServiceImpl;
import cornell.cs3300.nosql.impl.ReportingServiceImpl;
import cornell.cs3300.nosql.pojos.Candy;
import cornell.cs3300.nosql.pojos.ClusterCentroid;
import cornell.cs3300.nosql.pojos.Customer;

public class ReportingTests {
	
	Random rand = new Random();
	
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

		Key<Customer> custId = null;
		Key<Candy> candyId = null;
		Candy[] candies = null;
		float[] ratings = null;
		
		// Adds one rating
		try {
			custId = us.addCustomer("Gary");
			candyId = cs.addCandy("Gummy Bears", 0.9f, 0.3f, 0.1f, 0.0f, 0.1f);
			rs.rateCandy(custId, candyId, 0.5f);
			candies = cs.getCandy(null, CandySortMethod.NAME, false);
			ratings = candies[0].getRating();
		} catch (ServerException e) {
			fail("Unexpected exception: " + e.toString());
		}

		assertNotNull(candies);
		assertEquals(1, candies.length);
		assertEquals("Gummy Bears", candies[0].getCandyName());
		assertNotNull(ratings);
		assertEquals(1, ratings.length);
		assertTrue(ratings[0] == 0.5f);
		
		// Adds another rating, checks average rating
		try {
			custId = us.addCustomer("Professor Oak");
			rs.rateCandy(custId, candyId, 0.2f);
			candies = cs.getCandy(null, CandySortMethod.NAME, false);
			ratings = candies[0].getRating();
		} catch (ServerException e) {
			fail("Unexpected exception: " + e.toString());
		}
		
		assertNotNull(candies);
		assertEquals(1, candies.length);
		assertEquals("Gummy Bears", candies[0].getCandyName());
		assertNotNull(ratings);
		assertEquals(2, ratings.length);
		assertTrue(ratings[1] == 0.2f);
		assertTrue(candies[0].getAvgRating() == 0.35f);
		
		// Sort by rating
		try {
			for (int i = 0; i < 10; i++) {
				candyId = cs.addCandy("Candy"+i, 0f, 0f, 0f, 0f, 0f);
				rs.rateCandy(custId, candyId, (float)rand.nextDouble());
			}
			candies = cs.getCandy(null, CandySortMethod.RATING, false);
		} catch (ServerException e) {
			fail("Unexpected exception: " + e.toString());
		}
		
		for (int i = 0; i < candies.length - 1; i++) {
			if (candies[i].getAvgRating() > candies[i+1].getAvgRating()) {
				fail("Sort by ratings failed");
			}
		}
		
		// Invalid customer
		try {			
			rs.rateCandy(new Key<Customer>("Customer", "badid12345"), candyId, 0.2f);
		} catch (ServerException e) {
			assertEquals(e.getErrorType(), ErrorType.CUSTOMER_NOT_FOUND);
		}
		
		// Invalid candy
		try {			
			custId = us.addCustomer("Misty");
			rs.rateCandy(custId, new Key<Candy>("Candy", "badid12345"), 0.2f);
		} catch (ServerException e) {
			assertEquals(e.getErrorType(), ErrorType.CANDY_NOT_FOUND);
		}
		
		// Invalid rating
		try {			
			custId = us.addCustomer("Team Rocket");
			rs.rateCandy(custId, candyId, 1.5f);
		} catch (ServerException e) {
			assertEquals(e.getErrorType(), ErrorType.INVALID_INPUT);
		}
	}
	
	@Test
	public void purchaseCandy() {
		CustomerService us = new CustomerServiceImpl();
		CandyService cs = new CandyServiceImpl();
		ReportingService rs = new ReportingServiceImpl();

		Key<Candy> candyId = null;
		Key<Candy> candyId2 = null;
		Key<Customer> custId = null;
		Candy[] candies = null;
		Customer[] customers = null;
		String[] purchases = null;
		int purchaseCount = 0;
		
		// Purchase one candy once
		try {
			custId = us.addCustomer("Willy Wonka");
			candyId = cs.addCandy("Wonka Bar", 1f, 0.5f, 0.1f, 0.2f, 0.4f);
			rs.purchaseCandy(custId, candyId);
			purchases = rs.getCandyPurchased(custId);
			candies = cs.getCandy(null, CandySortMethod.NAME, false);
			purchaseCount = candies[0].getPurchases();
		} catch (ServerException e) {
			fail("Unexpected exception: " + e.toString());
		}

		assertNotNull(candies);
		assertNotNull(purchases);
		assertEquals(purchases.length, 1);
		assertEquals(purchases[0], "Wonka Bar");
		assertEquals(purchaseCount, 1);
		
		// Purchase same candy again
		try {
			rs.purchaseCandy(custId, candyId);
			purchases = rs.getCandyPurchased(custId);
			candies = cs.getCandy(null, CandySortMethod.NAME, false);
			purchaseCount = candies[0].getPurchases();
		} catch (ServerException e) {
			fail("Unexpected exception: " + e.toString());
		}

		assertNotNull(purchases);
		assertEquals(purchases.length, 1);
		assertEquals(purchases[0], "Wonka Bar");
		assertEquals(purchaseCount, 2);
		
		// Purchase different candy
		try {
			candyId2 = cs.addCandy("Hershey's Bar", 1f, 0.5f, 0.1f, 0.2f, 0.6f);
			rs.purchaseCandy(custId, candyId2);
			purchases = rs.getCandyPurchased(custId);
			candies = cs.getCandy(null, CandySortMethod.NAME, true);
			purchaseCount = candies[1].getPurchases();
		} catch (ServerException e) {
			fail("Unexpected exception: " + e.toString());
		}

		assertNotNull(purchases);
		assertEquals(purchases.length, 2);
		assertEquals(purchases[0], "Wonka Bar");
		assertEquals(purchases[1], "Hershey's Bar");
		assertEquals(purchaseCount, 1);
		
		// Sort by purchases
		try {
			custId = us.addCustomer("Charlie");
			rs.purchaseCandy(custId, candyId);
			Key<Candy> candyId3 = cs.addCandy("Reese's", 1f, 0.4f, 0.1f, 0.7f, 0.6f);
			custId = us.addCustomer("Dr. Seuss");
			for (int i = 0; i < 5; i++) {
				rs.purchaseCandy(custId, candyId3);
			}
			customers = us.getCustomers(null, CustomerSortMethod.PURCHASES, true);
			candies = cs.getCandy(null, CandySortMethod.PURCHASES, true);
		} catch (ServerException e) {
			fail("Unexpected exception: " + e.toString());
		}
		
		assertNotNull(customers);
		assertEquals(customers.length, 3);
		assertEquals(customers[0].getName(), "Dr. Seuss");
		assertEquals(customers[1].getName(), "Willy Wonka");
		assertEquals(customers[2].getName(), "Charlie");

		assertNotNull(candies);
		assertEquals(candies.length, 3);
		assertEquals(candies[0].getCandyName(), "Reese's");
		assertEquals(candies[1].getCandyName(), "Wonka Bar");
		assertEquals(candies[2].getCandyName(), "Hershey's Bar");
		
		// Purchasing - Invalid customer
		try {			
			rs.purchaseCandy(new Key<Customer>("Customer", "badid12345"), candyId);
		} catch (ServerException e) {
			assertEquals(e.getErrorType(), ErrorType.CUSTOMER_NOT_FOUND);
		}
		
		// Invalid candy
		try {			
			Key<Customer> custIdBad = us.addCustomer("Misty");
			rs.purchaseCandy(custIdBad, new Key<Candy>("Candy", "badid12345"));
		} catch (ServerException e) {
			assertEquals(e.getErrorType(), ErrorType.CANDY_NOT_FOUND);
		}
		
		// Get purchases - Invalid customer
		try {
			rs.getCandyPurchased(new Key<Customer>("Customer", "badid12345"));
		} catch (ServerException e) {
			assertEquals(e.getErrorType(), ErrorType.CUSTOMER_NOT_FOUND);
		}
	}

	@Test
	public void clustering() {
		CustomerService us = new CustomerServiceImpl();
		CandyService cs = new CandyServiceImpl();
		ReportingService rs = new ReportingServiceImpl();

		Key<Candy> candyId = null;
		
		// Adds 10 candies, rates them, and runs clustering with 3 centroids, minimum rating of 0.5, and 5 iterations
		try {
			Key<Customer> custId = us.addCustomer("Candy Man");
			for (int i = 0; i < 10; i++) {
				candyId = cs.addCandy("Candy"+i, (float)rand.nextDouble(), (float)rand.nextDouble(), 
						(float)rand.nextDouble(), (float)rand.nextDouble(), (float)rand.nextDouble());
				rs.rateCandy(custId, candyId, (float)rand.nextDouble());
			}
			rs.runClustering(3, 0.0f, 2);
			ClusterCentroid[] prefs = rs.getPreferenceClusters();
			assertEquals(prefs.length, 3);
//			for (ClusterCentroid c : prefs) {
//				System.out.println(c.getCandyCentroid());
//			}
		} catch (ServerException e) {
			fail("Unexpected exception: " + e.toString());
		}
	}
}
