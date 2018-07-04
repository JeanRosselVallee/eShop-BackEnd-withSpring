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
@Table(name = "users")
public class User {

	// ------------------------
	// PRIVATE FIELDS
	// ------------------------

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotNull
	@Column(unique = true)
	private String email;

	@NotNull
	@Column(unique = true)
	private String apikey;

	@NotNull
	private String password;

	@NotNull
	private String name;

	@NotNull
	private boolean admin;

	// ------------------------
	// PUBLIC METHODS
	// ------------------------

	public User() {}

	public User(
			String p_email, 
			String p_name, 
			String p_password, 
			String p_apikey, 
			boolean p_admin
	) {
		this.email 		= p_email;
		this.name 		= p_name;
		this.password 	= p_password;
		this.apikey 	= p_apikey;
		this.admin 		= p_admin;
	}

	public String getApikey() {
		return apikey;
	}

	public void setApikey(String apikey) {
		this.apikey = apikey;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isAdmin() {
		return admin;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

	public long getId() {
		return id;
	}

	public void setId(long value) {
		this.id = value;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String value) {
		this.email = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String value) {
		this.name = value;
	}
	
	public String toString() {
		String data = "{";
	
		data += " \"id\": \""		+this.id		+"\",";
		data += " \"name\": \""		+this.name		+"\",";
		data += " \"email\": \""	+this.email		+"\", ";
		data += " \"admin\": \""	+this.admin		+"\", ";
		data += " \"apikey\": \""	+this.apikey	+"\", ";
		data += " \"password\": \"" +this.password	+"\"";
		
		data += "}";
		
		return data;
		
	}

} // class User
