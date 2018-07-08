package shop.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import shop.models.Cart;
import shop.models.CartDao;
import shop.models.ProductDao;
import shop.models.User;
import shop.models.UserDao;
import shop.utils.Custom;
import shop.utils.Utils;
import shop.models.Product;
import shop.models.ProductDao;

/**
 * Class CartController
 */
@Controller
@CrossOrigin(
		origins = { "http://localhost:4200" }, 
		methods= {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE, RequestMethod.PUT}) 
public class CartController {

	// ------------------------
	// PUBLIC METHODS
	// ------------------------
	
	/**
	 * Create a new cart 
	 */
	@RequestMapping(value = "/carts/add", method=RequestMethod.POST)
	@ResponseBody
	public String create(
			@RequestParam(value="apikey") String p_apikey, 
			@RequestParam(value="user_id") Long p_userid
	) { Utils.logMethodName();
		try 
		{

			User creator = userDao.getByApiKey(p_apikey);

			User targetUser = userDao.getById(p_userid);
			
			if
			( 
					( creator != null  && targetUser != null )
					&& 
					( creator.isAdmin() || creator.getId() == targetUser.getId()  ) 
			)
			{ 
				Cart cart = new Cart(targetUser);
				System.out.println(cart);
				cartDao.create(cart);
			}
			else {
				return Custom.getJsonError(Custom.BAD_ROLE_ERROR);
			}
		} 
		catch (Exception ex) 
		{ Utils.logMethodName();
			return Custom.getJsonError(Custom.CREATE_ERROR);
		}
		
		return Custom.getJsonSuccess();
	}
	
	/**
	 * Create a new cart when a new user is created
	 */	
	/*
	public static String createAfterUser( User owner)
	{ Utils.logMethodName();
		
		try 
		{
			Cart cart = new Cart(owner);
			System.out.println(cart);
			cartDao.create(cart);		
		} 
		catch (Exception ex) 
		{ Utils.logMethodName();
			return Custom.getJsonError(Custom.CREATE_ERROR);
		}
		
		return Custom.getJsonSuccess();
	
	}
*/
	
	/**
	 * Delete the cart
	 */
	@RequestMapping(value = "/carts/remove", method=RequestMethod.DELETE)
	@ResponseBody
	public String delete(
			@RequestParam(value="cart_id") long p_cartid, 
			@RequestParam(value="apikey") String p_apikey
	) { Utils.logMethodName();
		try {
			
			User current = userDao.getByApiKey(p_apikey);
			
			if( current != null && current.isAdmin() ) { Utils.logMethodName();
				Cart cart = cartDao.getById(p_cartid);
				cartDao.delete(cart);
			}
			else {
				return Custom.getJsonError(Custom.BAD_ROLE_ERROR);
			}
			
			
		} catch (Exception ex) { Utils.logMethodName();
			return Custom.getJsonError(Custom.DELETE_ERROR);
		}
		
		return Custom.getJsonSuccess();
	}

	
	//To Do : get all carts for admin user
	/**
	 * Retrieve all carts
	 */
	@RequestMapping(value = "/carts", method=RequestMethod.GET)
	@ResponseBody

	public String getAll( 
			@RequestParam(value="apikey") String p_apikey 
	) { Utils.logMethodName();
		try {
			User current = userDao.getByApiKey(p_apikey);
			
			if( current != null && current.isAdmin() )
				return cartDao.getAll().toString();
			else
				return Custom.getJsonError(Custom.BAD_ROLE_ERROR);
			
		} catch (Exception ex) { Utils.logMethodName();
			return Custom.getJsonError(Custom.READ_ERROR);
		}
	}
	
	
	/**
	 * Retrieve a cart by id
	 */
	
	
	/**
	 * Retrieve a cart_id by user_id
	 */
	@RequestMapping(value = "/carts/get-cart-by-id", method=RequestMethod.GET)
	@ResponseBody

	public String getProductsInCart( 
			@RequestParam(value="apikey") String p_apikey, 
			@RequestParam(value="cart_id") Long p_cartid
	) { Utils.logMethodName();
		try {
			User current = userDao.getByApiKey(p_apikey);
			Cart cart    = cartDao.getById(p_cartid);
			
			
			
			if( 
					current 		!= null &&  // si l'utilisateur correspondant à l'apikey existe 
					cart 			!= null	&&  // ainsi que le panier
					cart.getOwner() != null &&  // que le panier est possédé par quelqu'un
					(
							current.isAdmin()	// que l'utilisateur courant est admin 
								|| 				// ou 
							cart.getOwner().getId() == current.getId() // si le panier lui appartient 
					)
			) { Utils.logMethodName();
				// only array attribute products is sent
			
				return cart.getProducts().toString();
			}
			else {
				return Custom.getJsonError(Custom.BAD_ROLE_ERROR);
			}
			
		} catch (Exception ex) { Utils.logMethodName();
			return Custom.getJsonError(Custom.READ_ERROR);
		}
	}
	@RequestMapping(value = "/carts/get-cart-id", method=RequestMethod.GET)
	@ResponseBody
	
	public Long getCartIdByUserId(
			@RequestParam(value="user_id") String p_user_id, 
			@RequestParam(value="apikey") String p_apikey 
	) { Utils.logMethodName();
		try {
			
			User current = userDao.getByApiKey(p_apikey);
			Long cart_id = cartDao.getCartIdByUserId(p_user_id);
			System.out.println("cart_id=" + cart_id );			
			return cart_id;        
			/*
			if	( 
					current 		!= null &&  // si l'utilisateur correspondant à l'apikey existe
					(
							current.isAdmin()	// que l'utilisateur courant est admin 
								|| 				// ou 
							cart.getOwner().getId() == current.getId() // si le panier lui appartient 
					)
				)
				return cartDao.getAll().toString();
			else
				return Custom.getJsonError(Custom.BAD_ROLE_ERROR);
				*/
			
		} catch (Exception ex) { Utils.logMethodName();
			//return Custom.getJsonError(Custom.READ_ERROR);
		
		// To Do Error Handling 
			System.out.println("catched error");
			return null;
		}
	}
	
	/**
	 * Update the cart
	 */
	@RequestMapping(value = "/carts/add-product", method=RequestMethod.PUT)
	@ResponseBody
	public String addProductToCart(
			@RequestParam(value="apikey") String p_apikey, 
			@RequestParam(value="product_id") Long p_productid,
			@RequestParam(value="cart_id") Long p_cartid
	) { Utils.logMethodName();
		try {
			
			User current 	= userDao.getByApiKey(p_apikey);
			Cart cart 		= cartDao.getById(p_cartid);
			Product product = productDao.getById(p_productid);
			
						
			if( 
					current 		!= null &&  // si l'utilisateur correspondant à l'apikey existe 
					product 		!= null	&&  // ainsi que le produit .. 
					cart 			!= null	&&  // et le panier 
					cart.getOwner() != null &&  // que le panier est possédé par quelqu'un
					(
							current.isAdmin()	// que l'utilisateur courant est admin 
								|| 				// ou 
							cart.getOwner().getId() == current.getId() // si le panier lui appartient 
					)
			) { Utils.logMethodName();
				// on ajoute le produit au panier
				cart.getProducts().add(product);
				cartDao.update(cart);
			}
			else {
				return Custom.getJsonError(Custom.BAD_ROLE_ERROR);
			}
			
			
		} catch (Exception ex) { Utils.logMethodName();
			return Custom.getJsonError(Custom.UPDATE_ERROR);
		}
		
		return Custom.getJsonSuccess();
	}
	
	
	/**
	 * Update the cart
	 */
	@RequestMapping(value = "/carts/remove-product", method=RequestMethod.DELETE)
	@ResponseBody
	public String removeProductFromCart(
			@RequestParam(value="apikey") String p_apikey, 
			@RequestParam(value="product_id") Long p_productid,
			@RequestParam(value="cart_id") Long p_cartid
	) { Utils.logMethodName();
		try {
			
			User current 	= userDao.getByApiKey(p_apikey);
			Cart cart 		= cartDao.getById(p_cartid);
			Product product = productDao.getById(p_productid);
						
			if( 
					current 		!= null &&  // si l'utilisateur correspondant à l'apikey existe 
					product 		!= null	&&  // ainsi que le produit .. 
					cart 			!= null	&&  // et le panier 
					cart.getOwner() != null &&  // que le panier est possédé par quelqu'un
					(
							current.isAdmin()	// que l'utilisateur courant est admin 
								|| 				// ou 
							cart.getOwner().getId() == current.getId() // si le panier lui appartient 
					)
			) { Utils.logMethodName();
				// on retire le produit du panier
				cart.getProducts().remove(product);
				cartDao.update(cart);
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
	private CartDao cartDao;
	//private static CartDao cartDao;
	
	@Autowired
	private ProductDao productDao;

} // class CartController
