package cornell.cs3300.nosql.pojos;

import java.util.Date;

import com.google.code.morphia.annotations.Entity;

@Entity
public class Customer {

	private String name;
	private Date joinDate;
	
	public Customer(String name) {
		this.name = name;
		this.joinDate = new Date();
	}
	
	public String getName() {
		return name;
	}

	public Date getJoinDate() {
		return joinDate;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setJoinDate(Date joinDate) {
		this.joinDate = joinDate;
	}
}
