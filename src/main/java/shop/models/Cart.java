package shop.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;


import shop.models.Product;

/**
 * Represents an User for this web application.
 */
@Entity
@Table(name = "carts")
public class Cart {

	// ------------------------
	// PRIVATE FIELDS
	// ------------------------

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	//@OneToMany()
	@ManyToMany()
	private List<Product> products = new ArrayList<>();
	
	@OneToOne()
	private User owner = null;

	

	// ------------------------
	// PUBLIC METHODS
	// ------------------------

	public Cart() {}
	
	public Cart(User p_owner) {
		this.setOwner(p_owner);
	}

	
	
	public Long getId() {
		return id;
	}
	
	public User getOwner() {
		return owner;
	}



	public void setOwner(User owner) {
		this.owner = owner;
	}



	public void setId(Long id) {
		this.id = id;
	}



	public List<Product> getProducts() {
		return products;
	}



	public void setProducts(List<Product> products) {
		this.products = products;
	}



	public String toString() {
		String data = "{";
	
		data += " \"id\": \""		+this.id							+"\",";
		data += " \"products\": \""	+this.getProducts().toString()		+"\",";
		data += " \"user_id\": \""	+this.getOwner().getId()			+"\"";
		
		data += "}";
		
		return data;
		
	}

} // class User
