package cornell.cs3300.nosql.pojos;

import java.util.Date;

import org.bson.types.ObjectId;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;

@Entity
public class Customer {

	@Id
	private ObjectId id;
	private String name;
	private Date joinDate;
	private String[] purchases;
	private int purchaseCount;
	
	public Customer() {	}
	
	public Customer(String name) {
		this.name = name;
		this.joinDate = new Date();
	}
	
	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Date getJoinDate() {
		return joinDate;
	}

//	public void setJoinDate(Date joinDate) {
//		this.joinDate = joinDate;
//	}

	public String[] getPurchases() {
		return purchases;
	}

	public void setPurchases(String[] purchases) {
		this.purchases = purchases;
	}

	public int getPurchaseCount() {
		return purchaseCount;
	}

	public void setPurchaseCount(int purchaseCount) {
		this.purchaseCount = purchaseCount;
	}
}
