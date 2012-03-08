package cornell.cs3300.nosql.pojos;

/**
 * Stores information about the resulting centroids of k-means clustering
 */
public class ClusterCentroid {

	private String _id;
	private String className;
	private float sweetness;
	private float viscosity;
	private float sourness;
	private float nuts;
	private float texture;
	private float rating;

	private Candy candyCentroid;
	
	public ClusterCentroid() { }
	
	public ClusterCentroid(float sweetness, float viscosity, float sourness, float nuts, float texture) {
		candyCentroid = new Candy("", sweetness, viscosity, sourness, nuts, texture);
	}
	/**
	 * Gets the calculated centroid data
	 */
	public Candy getCandyCentroid() {
		return candyCentroid;
	}
	public void setCandyCentroid(Candy candyCentroid) {
		this.candyCentroid = candyCentroid;
	}
}
