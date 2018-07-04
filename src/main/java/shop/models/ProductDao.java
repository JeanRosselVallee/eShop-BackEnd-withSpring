package shop.models;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import shop.utils.Utils;

/**
 * This class is used to access data for the Product entity.
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
public class ProductDao {
  
  // ------------------------
  // PUBLIC METHODS
  // ------------------------
  
  /**
   * Save the product in the database.
   */
  public void create(Product product) {
    entityManager.persist(product);
    return;
  }
  
  /**
   * Delete the product from the database.
   */
  public void delete(Product product) {
    if (entityManager.contains(product))
      entityManager.remove(product);
    else
      entityManager.remove(entityManager.merge(product));
    return;
  }
  
  /**
   * Return all the products stored in the database.
   */
  @SuppressWarnings("unchecked")
  public List<Product> getAll() {
	Utils.logMethodName();
    return entityManager.createQuery("from Product").getResultList();
  }

  /**
   * Return the product having the passed id.
   */
  public Product getById(long id) {
    return entityManager.find(Product.class, id);
  }

  /**
   * Update the passed product in the database.
   */
  public void update(Product product) {
    entityManager.merge(product);
    return;
  }

  // ------------------------
  // PRIVATE FIELDS
  // ------------------------
  
  // An EntityManager will be automatically injected from entityManagerFactory
  // setup on DatabaseConfig class.
  @PersistenceContext
  private EntityManager entityManager;
  
} // class ProductDao
