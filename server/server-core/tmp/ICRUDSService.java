package com.gc;

import java.io.Serializable;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;

import com.acp.vision.dal.dao.api.IGenericDAO;
import com.acp.vision.dal.exception.DTOAlreadyExistsException;
import com.acp.vision.dal.exception.DTONotFoundException;
import com.acp.vision.dal.exception.DTONullPropertyException;
import com.acp.vision.exception.ServiceException;
import com.acp.vision.model.api.IID;
import com.acp.vision.service.IService;

/**
 * The Interface ICRUDSService.
 * 
 * @param <ID> The ID
 * @param <ID_TYPE> The ID Type
 * @author g.migliorini
 */
public interface ICRUDSService<ID extends IID<ID_TYPE>, ID_TYPE extends Serializable> extends IService {

  /**
   * Creates the.
   * 
   * @param id the id
   * @throws DTONullPropertyException the DTO null property exception
   * @throws DTOAlreadyExistsException the DTO already exists exception
   * @throws ServiceException the service exception
   */
  void create(ID id)
      throws DTONullPropertyException, DTOAlreadyExistsException, ServiceException;

  /**
   * Update.
   * 
   * @param id the id
   * @throws DTONullPropertyException the DTO null property exception
   * @throws DTONotFoundException the DTO not found exception
   * @throws ServiceException the service exception
   */
  void update(ID id)
      throws DTONullPropertyException, DTONotFoundException, ServiceException;

  /**
   * Delete.
   * 
   * @param id the id
   * @throws DTONullPropertyException the DTO null property exception
   * @throws DTONotFoundException the DTO not found exception
   * @throws ServiceException the service exception
   */
  void delete(ID id)
      throws DTONullPropertyException, DTONotFoundException, ServiceException;

  /**
   * Gets the all.
   * 
   * @return the all
   * @throws ServiceException the service exception
   */
  List<ID> getAll()
      throws ServiceException;

  /**
   * Gets the all.
   * 
   * @param firstResult the first result
   * @param maxResults the max results
   * @return the all
   * @throws ServiceException the service exception
   */
  List<ID> getAll(int firstResult, int maxResults)
      throws ServiceException;

  /**
   * Gets the all id.
   * 
   * @return the all id
   * @throws ServiceException the service exception
   */
  List<ID_TYPE> getAllId()
      throws ServiceException;

  /**
   * Gets the all number.
   * 
   * @return the all number
   * @throws ServiceException the service exception
   */
  long getAllNumber()
      throws ServiceException;

  /**
   * Gets the from id.
   * 
   * @param id the id
   * @return the from id
   * @throws ServiceException the service exception
   */
  ID getFromID(ID_TYPE id)
      throws ServiceException;

  /**
   * Gets the from id.
   * 
   * @param id the id
   * @return the from id
   * @throws ServiceException the service exception
   */
  ID getFromID(String id)
      throws ServiceException;

  /**
   * Gets the from example.
   * 
   * @param example the example
   * @return the from example
   * @throws ServiceException the service exception
   */
  List<ID> getFromExample(ID example)
      throws ServiceException;

  /**
   * Gets the from example.
   * 
   * @param example the example
   * @param firstResult the first result
   * @param maxResults the max results
   * @return the from example
   * @throws ServiceException the service exception
   */
  List<ID> getFromExample(ID example, int firstResult, int maxResults)
      throws ServiceException;

  /**
   * Gets the from example number.
   * 
   * @param example the example
   * @return the from example number
   * @throws ServiceException the service exception
   */
  long getFromExampleNumber(ID example)
      throws ServiceException;

  /**
   * Gets the aO.
   * 
   * @return the aO
   */
  IGenericDAO<ID, ID_TYPE> getAO();

  /**
   * Sets the ao.
   * 
   * @param typeAO the type ao
   */
  void setAO(IGenericDAO<ID, ID_TYPE> typeAO);

  /**
   * Builds the to.
   * 
   * @return the iD
   */
  ID buildTO();

  /**
   * Evict.
   * 
   * @param id the id
   */
  void evict(ID id);
  
    /**
     * Flush.
     */
  void flush();

    /**
     * Search all from criteria.
     * 
     * @param criteria the criteria
     * @return the list
     */
    List < ID > searchAllFromCriteria(DetachedCriteria criteria);

    /**
     * Search from criteria.
     * 
     * @param criteria the criteria
     * @return the iD
     */
    ID searchFromCriteria(DetachedCriteria criteria);
}
