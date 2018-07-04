package shop.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


import shop.models.User;
import shop.models.UserDao;
import shop.utils.Custom;
import shop.utils.Utils;

/**
 * Class UserController
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
	 * Create a new user 
	 */
	@RequestMapping(value = "/users/auth", method=RequestMethod.POST)
	@ResponseBody
	public String auth(
			
			@RequestParam(value="email") String p_email, 
			@RequestParam(value="password") String p_password
			
	) {
		// TODO Auto-generated method stub
		User current = userDao.getByEmail(p_email);
		String encrypted = Utils.encrypt(p_password);
		
		if( current != null && current.getPassword().equals(encrypted) ) {
			return current.toString();
		}
	
		
		return Custom.getJsonError(Custom.AUTH_ERROR);
	}

	
	/**
	 * Create a new user 
	 */
	@RequestMapping(value = "/users/add", method=RequestMethod.POST)
	@ResponseBody
	public String create(	
			@RequestParam(value="email") String p_email, 
			@RequestParam(value="name") String p_name, 
			@RequestParam(value="password") String p_password
	) {
		try 
		{
			User user = new User(
					p_email, 
					p_name, 
					Utils.encrypt(p_password), 
					Utils.newApiKey(), 
					false
			);
			
			userDao.create(user);
		} 
		catch (Exception ex) 
		{
			return Custom.getJsonError(Custom.CREATE_ERROR);
		}
		
		return Custom.getJsonSuccess();
	}

	/**
	 * Delete the user
	 */
	@RequestMapping(value = "/users/remove", method=RequestMethod.DELETE)
	@ResponseBody
	public String delete(
			@RequestParam(value="user_id") long p_userid, 
			@RequestParam(value="apikey") String p_apikey
	) {
		try {
			
			
			User toDeleteUser = userDao.getById(p_userid);
			User currentUser = userDao.getByApiKey(p_apikey);
			
			// si je suis un admin ou s'il s'agit de mon compte
			if( currentUser.isAdmin() || 
				( currentUser.getId() == toDeleteUser.getId() ) 
			) {
				userDao.delete(toDeleteUser);
			}
			else {
				return Custom.getJsonError(Custom.BAD_ROLE_ERROR);
			}
			
			
		} catch (Exception ex) {
			return Custom.getJsonError(Custom.DELETE_ERROR);
		}
		
		return Custom.getJsonSuccess();
	}

	/**
	 * Retrieve all users
	 */
	@RequestMapping(value = "/users", method=RequestMethod.GET)
	@ResponseBody

	public String getAll(
			@RequestParam(value="apikey") String p_apikey
	) {
		try {
			
			User currentUser = userDao.getByApiKey(p_apikey);
			
			if( currentUser.isAdmin() )
				return userDao.getAll().toString();
			else
				return Custom.getJsonError(Custom.BAD_ROLE_ERROR);
			
		} catch (Exception ex) {
			return Custom.getJsonError(Custom.READ_ERROR);
		}
	}

	/**
	 * Update the user
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
	) {
		try {
			User toUpdateUser 	= userDao.getById(p_userid);
			User currentUser 	= userDao.getByApiKey(p_apikey);
			
			// si je suis un admin ou s'il s'agit de mon compte
			if( currentUser.isAdmin() || currentUser.getId() == p_userid ) {
				
				toUpdateUser.setEmail(p_email);
				toUpdateUser.setName(p_name);
				toUpdateUser.setPassword(Utils.encrypt(p_password));
				
				// seul l'admin peut accorder le droit d'amin
				if( currentUser.isAdmin() ) {
					toUpdateUser.setAdmin( p_admin );
				}
				
				userDao.update(toUpdateUser);
			}else {
				return Custom.getJsonError(Custom.BAD_ROLE_ERROR);
			}
			
			
		} catch (Exception ex) {
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
