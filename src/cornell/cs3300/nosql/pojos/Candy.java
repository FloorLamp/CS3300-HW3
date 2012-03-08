package cornell.cs3300.nosql.pojos;

import org.bson.types.ObjectId;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;

@Entity
public class Candy {
	
	@Id
	private ObjectId id;
	private String candyName;
	private float sweetness;
	private float viscosity;
	private float sourness;
	private float nuts;
	private float texture;
	private float[] rating;
	private float avgRating;
	private int purchases;
	
	public Candy() { }
	
	public Candy(String candyName, float sweetness, float viscosity, float sourness, float nuts, float texture) {
		this.candyName = candyName;
		this.sweetness = sweetness;
		this.viscosity = viscosity;
		this.sourness = sourness;
		this.nuts = nuts;
		this.texture = texture;
	}

	public String getCandyName() {
		return candyName;
	}
	public void setCandyName(String candyName) {
		this.candyName = candyName;
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

	public float[] getRating() {
		return rating;
	}

	public void setRating(float[] rating) {
		this.rating = rating;
	}

	public int getPurchases() {
		return purchases;
	}

	public void setPurchases(int purchases) {
		this.purchases = purchases;
	}

	public float getAvgRating() {
		float sum = 0f;
		for (float f : rating) {
			sum += f;
		}
		this.avgRating = sum / rating.length;
		return avgRating;
	}
	
	public String toString() {
		return "candyName " + candyName + ", sweetness " + sweetness 
				+ ", viscosity " + viscosity + ", sourness " + sourness + ", nuts " + nuts + ", texture " + texture;
	}
}
