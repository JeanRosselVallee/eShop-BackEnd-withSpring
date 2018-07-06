package shop.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


import shop.models.Product;
import shop.models.ProductDao;
import shop.models.User;
import shop.models.UserDao;
import shop.utils.Custom;
import shop.utils.Utils;

/**
 * Class ProductController
 */
@Controller
//access to the website from the same computer
@CrossOrigin(origins = { "http://localhost:4200" } )
//Test the access to the website from other computers in the same LAN 
//@CrossOrigin(	origins = { "*" }, methods= {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE, RequestMethod.PUT} )
public class ProductController {
	// ------------------------
	// PUBLIC METHODS
	// ------------------------
	/**
	 * Create a new product 
	 */
	@RequestMapping(value = "/products/add", method=RequestMethod.POST)
	@ResponseBody
	public String create(
			@RequestParam(value="apikey") String p_apikey, 
			@RequestParam(value="title") String p_title, 
			@RequestParam(value="url") String p_url, 
			@RequestParam(value="price") Long p_price
	) { Utils.logMethodName();
		try 
		{
			User current = userDao.getByApiKey(p_apikey);
			
			if( current != null && current.isAdmin() ) { Utils.logMethodName();
				Product product = new Product(p_title, p_url,p_price);
				productDao.create(product);
			}
			else {
				return Custom.getJsonError(Custom.BAD_ROLE_ERROR);
			}
		} 
		catch (Exception ex) 
		{
			return Custom.getJsonError(Custom.CREATE_ERROR);
		}
		
		return Custom.getJsonSuccess();
	}

	/**
	 * Delete the product
	 */
	@RequestMapping(value = "/products/remove", method=RequestMethod.DELETE)
	@ResponseBody
	public String delete(
			@RequestParam(value="product_id") long p_productid, 
			@RequestParam(value="apikey") String p_apikey
	) { Utils.logMethodName();
		try {
			
			User current = userDao.getByApiKey(p_apikey);
			
			if( current != null && current.isAdmin() ) { Utils.logMethodName();
				Product product = productDao.getById(p_productid);
				productDao.delete(product);
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
	 * Retrieve all products
	 */
	@RequestMapping(value = "/products", method=RequestMethod.GET)
	@ResponseBody

	public String getAll() { Utils.logMethodName();
		Utils.logMethodName();
		try {
			return productDao.getAll().toString();
			
		} catch (Exception ex) { Utils.logMethodName();
			return Custom.getJsonError(Custom.READ_ERROR);
		}
	}

	/**
	 * Update the product
	 */
	@RequestMapping(value = "/products/update", method=RequestMethod.PUT)
	@ResponseBody
	public String update(
			@RequestParam(value="product_id") long p_productid, 
			@RequestParam(value="apikey") String p_apikey, 
			@RequestParam(value="title") String p_title, 
			@RequestParam(value="url") String p_url, 
			@RequestParam(value="price") Long p_price
	) { Utils.logMethodName();
		try {
			
			User current = userDao.getByApiKey(p_apikey);
			Product product = productDao.getById(p_productid);
			
			if( current != null && current.isAdmin() ) { Utils.logMethodName();
			
				if( product != null ) { Utils.logMethodName();
				
					product.setPrice(p_price);
					product.setUrl(p_url);
					product.setTitle(p_title);
					productDao.update(product);
					
				}else {
					return Custom.getJsonError(Custom.UPDATE_ERROR);
				}
				
			}
			else {
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
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private ProductDao productDao;

} // class ProductController
