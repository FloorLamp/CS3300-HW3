package cornell.cs3300.nosql;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.mongodb.Mongo;
import cornell.cs3300.nosql.CandyService.CandySortMethod;
import cornell.cs3300.nosql.impl.CandyServiceImpl;
import cornell.cs3300.nosql.pojos.Candy;

public class CandyTests {
	
	@Before
	public void clearDatabase() {
		try {
			Mongo mongo = new Mongo("localhost");
			mongo.dropDatabase("candyStoreDB");
		} catch (Exception e) {
		}
	}
	
	@Test
	public void addOneCandy() {
		CandyService service = new CandyServiceImpl();

		try {
			service.addCandy("Gummy Bears", 0.9f, 0.3f, 0.1f, 0.0f, 0.1f);
		} catch (ServerException e) {
			fail("Unexpected exception: " + e.toString());
		}

		Candy[] candies = null;
		try {
			candies = service.getCandy(null, CandySortMethod.NAME,	false);
		} catch (ServerException e) {
			fail("Unexpected exception: " + e.toString());
		}
		assertNotNull(candies);
		assertEquals(1, candies.length);
		assertEquals("Gummy Bears", candies[0].getCandyName());
		assertTrue(candies[0].getSweetness() == 0.9f);
		assertTrue(candies[0].getViscosity() == 0.3f);
		assertTrue(candies[0].getSourness() == 0.1f);
		assertTrue(candies[0].getNuts() == 0.0f);
		assertTrue(candies[0].getTexture() == 0.1f);
	}
	
	
	@Test
	public void addMultipleCandies() {
		CandyService service = new CandyServiceImpl();

		try {
			service.addCandy("Gummy Bears", 0.8f, 0.3f, 0.1f, 0.0f, 0.1f);
			service.addCandy("Jolly Ranchers", 1.0f, 1.0f, 0.2f, 0.0f, 0.6f);
			service.addCandy("Skittles", 0.9f, 0.7f, 0.1f, 0.0f, 0.7f);
		} catch (ServerException e) {
			fail("Unexpected exception: " + e.toString());
		}

		Candy[] candies = null;
		try {
			candies = service.getCandy(null, CandySortMethod.NAME, false);
		} catch (ServerException e) {
			fail("Unexpected exception: " + e.toString());
		}
		assertNotNull(candies);
		assertEquals(3, candies.length);
		assertEquals("Gummy Bears", candies[0].getCandyName());
		assertEquals("Jolly Ranchers", candies[1].getCandyName());
		assertEquals("Skittles", candies[2].getCandyName());
	}
}
