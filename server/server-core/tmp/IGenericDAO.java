package com.gc;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;

import com.gc.common.dal.exception.DAOException;
import com.gc.common.service.ILoggable;

/**
 * The Interface IGenericDAO.
 * 
 * @param <DTO> the DTO
 * @param <ID> the ID
 * @author g.migliorini
 */
public interface IGenericDAO<DTO extends IDataTransfertObject, ID extends Serializable> extends ILoggable {

  /**
   * Search by id.
   * 
   * @param id the id
   * @return the dTO
   * @throws DAOException the DAO exception
   */
  DTO searchById(ID id)
      throws DAOException;

  /**
   * Search by criteria.
   * 
   * @param criteria the criteria
   * @return the list< dt o>
   */
  List<DTO> searchByCriteria(DetachedCriteria criteria);

  /**
   * Search by projection criteria.
   * 
   * @param criteria the criteria
   * @return the list
   */
  List<?> searchByProjectionCriteria(DetachedCriteria criteria);
  
  /**
   * Search by example.
   * 
   * @param dto the dto
   * @return the list< dt o>
   * @throws DAOException the DAO exception
   */
  List<DTO> searchByExample(final DTO dto)
      throws DAOException;

  /**
   * Search by example.
   * 
   * @param dto the dto
   * @param firstResult the first result
   * @param maxResults the max results
   * @return the list< dt o>
   * @throws DAOException the DAO exception
   */
  List<DTO> searchByExample(final DTO dto, int firstResult, int maxResults)
      throws DAOException;

  /**
   * Search all.
   * 
   * @return the list< dt o>
   */
  List<DTO> searchAll();

  /**
   * Search all.
   * 
   * @param firstResult the first result
   * @param maxResults the max results
   * @return the list< dt o>
   */
  List<DTO> searchAll(int firstResult, int maxResults);

  /**
   * Gets the search all number.
   * 
   * @return the search all number
   */
  int getSearchAllNumber();

  /**
   * Gets the search by example number.
   * 
   * @param dto the dto
   * @return the search by example number
   */
  int getSearchByExampleNumber(final DTO dto);

  /**
   * Creates the or update.
   * 
   * @param dto the dto
   * @throws DAOException the DAO exception
   */
  void createOrUpdate(DTO dto)
      throws DAOException;

  /**
   * Delete.
   * 
   * @param id the id
   * @throws DAOException the DAO exception
   */
  void delete(ID id)
      throws DAOException;

  /**
   * Delete.
   * 
   * @param dto the dto
   * @throws DAOException the DAO exception
   */
  void delete(DTO dto)
      throws DAOException;

  /**
   * Gets the all id.
   * 
   * @return the all id
   */
  List<ID> getAllId();

  /**
   * Evict.
   * 
   * @param dto the dto
   */
  void evict(DTO dto);
  

  /**
   * Flush.
   */
  void flush();

  /**
   * Gets the hibernate session.
   * 
   * @return the hibernate session
   */
  Session getHibernateSession();
  
}