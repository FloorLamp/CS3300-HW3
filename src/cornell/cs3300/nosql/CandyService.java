package cornell.cs3300.nosql;

import com.google.code.morphia.Key;

import cornell.cs3300.nosql.pojos.Candy;

/**
 * Service for candy data
 */
public interface CandyService {
	/**
	 * Sorting method for retrieving candy
	 */
	public static enum CandySortMethod {
		/**
		 * The averaged rating
		 */
		RATING,
		/**
		 * The number of purchases
		 */
		PURCHASES,
		/**
		 * The name of the customer
		 */
		NAME
	}

	/**
	 * Adds a candy. Each attribute is on a scale from 0 to 1
	 * 
	 * @param candyName
	 * @param sweetness
	 *            not sweet to very sweet
	 * @param viscosity
	 *            soft to hard
	 * @param sourness
	 *            low to high
	 * @param nuts
	 *            none to many
	 * @param texture
	 *            smooth to hard
	 * @return the candy's id
	 */
	Key<Candy> addCandy(String candyName, float sweetness, float viscosity,
			float sourness, float nuts, float texture) throws ServerException;

	/**
	 * Gets paginated customers, sorted by the {@link CustomerSortMethod}.
	 * 
	 * @param num
	 *            The number of results to return. If null, return all
	 * @param sortBy
	 *            sort method for the returned candy.
	 * @param descending
	 * @return
	 */
	Candy[] getCandy(Integer num, CandySortMethod sortBy, boolean descending)
			throws ServerException;
}
