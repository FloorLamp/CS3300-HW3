package cornell.cs3300.nosql.pojos;

import org.bson.types.ObjectId;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.mongodb.DBObject;

/**
 * Stores information used for doing k-means clustering
 */
@Entity
public class ClusterPoint {
	@Id
	private ObjectId id;
	private float sweetness;
	private float viscosity;
	private float sourness;
	private float nuts;
	private float texture;
	private float rating;
	
	public ClusterPoint() { }

	public ClusterPoint(float sweetness, float viscosity, float sourness,
			float nuts, float texture, float rating) {
		this.sweetness = sweetness;
		this.viscosity = viscosity;
		this.sourness = sourness;
		this.nuts = nuts;
		this.texture = texture;
		this.rating = rating;
	}
	
	public ClusterPoint(DBObject obj) {
		this.sweetness = ((Double)(obj.get("sweetness"))).floatValue();
	}
	
	public ObjectId getId() {
		return id;
	}
	public void setId(ObjectId id) {
		this.id = id;
	}
	public float getSweetness() {
		return sweetness;
	}
	public void setSweetness(float sweetness) {
		this.sweetness = sweetness;
	}
	public float getViscosity() {
		return viscosity;
	}
	public void setViscosity(float viscosity) {
		this.viscosity = viscosity;
	}
	public float getSourness() {
		return sourness;
	}
	public void setSourness(float sourness) {
		this.sourness = sourness;
	}
	public float getNuts() {
		return nuts;
	}
	public void setNuts(float nuts) {
		this.nuts = nuts;
	}
	public float getTexture() {
		return texture;
	}
	public void setTexture(float texture) {
		this.texture = texture;
	}
	public float getRating() {
		return rating;
	}
	public void setRating(float rating) {
		this.rating = rating;
	}
	public String toString() {
		return "sweetness " + sweetness + ", viscosity " + viscosity + ", sourness " 
				+ sourness + ", nuts " + nuts + ", texture " + texture + ", rating " + rating;
	}
}
