package shop.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * Represents an User for this web application.
 */
@Entity
@Table(name = "products")
public class Product {

	// ------------------------
	// PRIVATE FIELDS
	// ------------------------

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotNull
	private String title;

	@NotNull
	private String url;

	@NotNull
	private Long price;

	// ------------------------
	// PUBLIC METHODS
	// ------------------------

	public Product() {}
	
	public Product( String p_title, String p_url, Long p_price ) {
		this.setPrice(p_price);
		this.setTitle(p_title);
		this.setUrl(p_url);
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public String getUrl() {
		return url;
	}


	public void setUrl(String url) {
		this.url = url;
	}


	public Long getPrice() {
		return price;
	}


	public void setPrice(Long price) {
		this.price = price;
	}


	public String toString() {
		String data = "{";
	
		data += " \"id\": \""	+ this.id		+ "\",";
		data += " \"title\": \""+this.title+"\",";
		data += " \"url\": \""	+this.url+"\", ";
		data += " \"price\": \""+this.price+"\"";
		
		data += "}";
		
		return data;
		
	}

} // class User
