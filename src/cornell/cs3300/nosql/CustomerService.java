package cornell.cs3300.nosql;

import com.google.code.morphia.Key;

import cornell.cs3300.nosql.pojos.Customer;

/**
 * Service for customer data
 */
public interface CustomerService {

	/**
	 * Sorthing methods for customers
	 */
	public static enum CustomerSortMethod {
		/**
		 * The date the customer joined
		 */
		JOIN_DATE,
		/**
		 * The number of purchases by the customer
		 */
		PURCHASES,
		/**
		 * The name of the customer
		 */
		NAME
	}

	/**
	 * Adds a customer
	 * 
	 * @param name
	 * @return the id
	 */
	Key<Customer> addCustomer(String name) throws ServerException;

	/**
	 * Gets paginated customers, sorted by the {@link CustomerSortMethod}.
	 * 
	 * @param num
	 *            The number of results to return. If null, return all
	 * @param sortBy
	 *            sort method for the returned list
	 * @param descending
	 *            if the sort should be descending
	 * @return
	 */
	Customer[] getCustomers(Integer num, CustomerSortMethod sortBy,
			boolean descending) throws ServerException;

}
