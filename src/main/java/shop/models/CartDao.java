package shop.models;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;


import org.springframework.stereotype.Repository;

import shop.utils.Utils;

/**
 * This class is used to access data for the Cart entity.
 * Repository annotation allows the component scanning support to find and 
 * configure the DAO wihtout any XML configuration and also provide the Spring 
 * exceptiom translation.
 * Since we've setup setPackagesToScan and transaction manager on
 * DatabaseConfig, any bean method annotated with Transactional will cause
 * Spring to magically call begin() and commit() at the start/end of the
 * method. If exception occurs it will also call rollback().
 */
@Repository
@Transactional
public class CartDao {
  
  // ------------------------
  // PUBLIC METHODS
  // ------------------------
  
  /**
   * Save the cart in the database.
   */
  public void create(Cart cart) {Utils.logMethodName();
    entityManager.persist(cart);
    return;
  }
  
  /**
   * Delete the cart from the database.
   */
  public void delete(Cart cart) {Utils.logMethodName();
    if (entityManager.contains(cart))
      entityManager.remove(cart);
    else
      entityManager.remove(entityManager.merge(cart));
    return;
  }
  
  /**
   * Return all the carts stored in the database.
   */
  @SuppressWarnings("unchecked")
  public List<Cart> getAll() {Utils.logMethodName();
    return entityManager.createQuery("from Cart").getResultList();
  }

  /**
   * Return the cart having the passed id.
   */
  public Cart getById(long id) {Utils.logMethodName();
    return entityManager.find(Cart.class, id);
  }

  /**
   * Update the passed cart in the database.
   */
  public void update(Cart cart) {Utils.logMethodName();
    entityManager.merge(cart);
    return;
  }
  
  /**
   * Return the cart id having the passed user id.
   */
  public Long getCartIdByUserId(String user_id) 
  	{ Utils.logMethodName();
	  

		 
		System.out.println("p_user_id=" + user_id);
	    
	    Query sqlQuery = entityManager.createQuery("from Cart where owner_id = :user_id");
	    
	    try {
	    	Cart selectedCart = (Cart) sqlQuery.setParameter("user_id", user_id).getSingleResult();
	    	Long cart_id = selectedCart.getId() ;
	        System.out.println( "cart_id=" + cart_id.toString() );
	    	return cart_id ;
	    } catch (final NoResultException nre) {Utils.logMethodName();
			
			// To Do Error Handling 
			System.out.println("catched error");
	        return null;
	    }
	  
    //return entityManager.find(Cart.class, user_id);
  }

  // ------------------------
  // PRIVATE FIELDS
  // ------------------------
  
  // An EntityManager will be automatically injected from entityManagerFactory
  // setup on DatabaseConfig class.
  @PersistenceContext
  private EntityManager entityManager;
  
} // class CartDao
