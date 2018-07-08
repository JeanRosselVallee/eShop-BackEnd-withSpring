package shop.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import shop.models.Cart;
import shop.models.CartDao;
import shop.models.User;
import shop.models.UserDao;
import shop.utils.Custom;
import shop.utils.Utils;
import shop.controllers.CartController;

/**
 * Class UserController
 * Access to back-end server page limited to Angular front-end server 
 */
@Controller
@CrossOrigin(
		origins = { "http://localhost:4200" }, 
		methods= {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE, RequestMethod.PUT})
public class UserController {

	// ------------------------
	// PUBLIC METHODS
	// ------------------------

	/**
	 * User Authentication
	 * @param email
	 * @param password 
	 * Access : everyone
	 */
	@RequestMapping(value = "/users/auth", method=RequestMethod.POST)
	@ResponseBody
	public String auth(
			
			@RequestParam(value="email") String p_email, 
			@RequestParam(value="password") String p_password
			
	) { Utils.logMethodName();
		// TODO Auto-generated method stub
		User current = userDao.getByEmail(p_email);
		String encrypted = Utils.encrypt(p_password);
		
		if( current != null && current.getPassword().equals(encrypted) ) { Utils.logMethodName();
			return current.toString();
		}
	
		
		return Custom.getJsonError(Custom.AUTH_ERROR);
	}

	
	/**
	 * Create a new user 
	 * @param email
	 * @param name
	 * @param password	
	 * @return string Success as Json
	 * A user and its cart are created at the same time
	 */
	
	@RequestMapping(value = "/users/add", method=RequestMethod.POST)
	@ResponseBody
	public String create(	
			@RequestParam(value="email") String p_email, 
			@RequestParam(value="name") String p_name, 
			@RequestParam(value="password") String p_password
	) { Utils.logMethodName();
		User current;
		try 
		{
			current = new User(
					p_email, 
					p_name, 
					Utils.encrypt(p_password), 
					Utils.newApiKey(), 
					false
			);
			
			userDao.create(current);
		} 
		catch (Exception ex) 
		{
			return Custom.getJsonError(Custom.CREATE_ERROR);
		}

		createCartAfterUser(current);
		
		return Custom.getJsonSuccess();
	}
	  
	  
	  private void createCartAfterUser(User creator)
	  {
	      final String target_uri = "http://localhost:8080/carts/add";

	      RestTemplate  sentRequest = new RestTemplate ();
		  
	      HttpHeaders headers = new HttpHeaders();
          headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		  MultiValueMap<String, String> body = 
					new LinkedMultiValueMap<String, String>();					
		  body.add("apikey", "9b943d3cd5694d12b788de17b0571df3649bccb2");
		  body.add("user_id", creator.getId().toString() );
		  
		  HttpEntity<MultiValueMap<String, String>> requestEntity = 
					new HttpEntity<MultiValueMap<String, String>> (body, headers);
		  
	      String result = 
	    		  sentRequest.postForObject(
					target_uri, 
					requestEntity, 
					String.class);

	      System.out.println(result);
	  }

	
	
	/**
	 * Delete the user
	 * @param user_id
	 * @param apikey
	 * @return string Error as Json  
	 */
	@RequestMapping(value = "/users/remove", method=RequestMethod.DELETE)
	@ResponseBody
	public String delete(
			@RequestParam(value="user_id") long p_userid, 
			@RequestParam(value="apikey") String p_apikey
	) { Utils.logMethodName();
		try {
			
			
			User toDeleteUser = userDao.getById(p_userid);
			User currentUser = userDao.getByApiKey(p_apikey);
			
			// si je suis un admin ou s'il s'agit de mon compte
			if( currentUser.isAdmin() || 
				( currentUser.getId() == toDeleteUser.getId() ) 
			) { Utils.logMethodName();
				userDao.delete(toDeleteUser);
			}
			else {
				return Custom.getJsonError(Custom.BAD_ROLE_ERROR);
			}
			
			
		} catch (Exception ex) { Utils.logMethodName();
			return Custom.getJsonError(Custom.DELETE_ERROR);
		}
		
		return Custom.getJsonSuccess();
	}

	/**
	 * Retrieve all users
	 * @param apikey
	 * @return string Error as Json
	 */
	@RequestMapping(value = "/users", method=RequestMethod.GET)
	@ResponseBody
	public String getAll(
			@RequestParam(value="apikey") String p_apikey
	) { Utils.logMethodName();
		try {
			
			User currentUser = userDao.getByApiKey(p_apikey);
			
			if( currentUser.isAdmin() )
			{
				String p_users = userDao.getAll().toString();
				//System.out.println(p_users);
				return p_users;
			}
			else
				return Custom.getJsonError(Custom.BAD_ROLE_ERROR);
			
		} catch (Exception ex) { Utils.logMethodName();
			return Custom.getJsonError(Custom.READ_ERROR);
		}
	}

	/**
	 * Update the user
	 * @param user_id
	 * @param apikey	 
	 * @param email
	 * @param name
	 * @param password
	 * @param admin	
	 * @return string Error as Json
	 */
	@RequestMapping(value = "/users/update", method=RequestMethod.PUT)
	@ResponseBody
	public String update(
			@RequestParam(value="user_id") long p_userid, 
			@RequestParam(value="apikey") String p_apikey, 
			@RequestParam(value="email") String p_email, 
			@RequestParam(value="name") String p_name, 
			@RequestParam(value="password") String p_password, 
			@RequestParam(value="admin", defaultValue="false" ) boolean p_admin
	) { Utils.logMethodName();
		try {
			User toUpdateUser 	= userDao.getById(p_userid);
			User currentUser 	= userDao.getByApiKey(p_apikey);
			
			// si je suis un admin ou s'il s'agit de mon compte
			if( currentUser.isAdmin() || currentUser.getId() == p_userid ) { Utils.logMethodName();
				
				toUpdateUser.setEmail(p_email);
				toUpdateUser.setName(p_name);
				toUpdateUser.setPassword(Utils.encrypt(p_password));
				
				// seul l'admin peut accorder le droit d'amin
				if( currentUser.isAdmin() ) { Utils.logMethodName();
					toUpdateUser.setAdmin( p_admin );
				}
				
				userDao.update(toUpdateUser);
			}else {
				return Custom.getJsonError(Custom.BAD_ROLE_ERROR);
			}
			
			
		} catch (Exception ex) { Utils.logMethodName();
			return Custom.getJsonError(Custom.UPDATE_ERROR);
		}
		return Custom.getJsonSuccess();
	}

	// ------------------------
	// PRIVATE FIELDS
	// ------------------------

	// Wire the UserDao used inside this controller.
	@Autowired
	private UserDao userDao;

} // class UserController
