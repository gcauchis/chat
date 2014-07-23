package com.gc;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Projections;
import org.springframework.orm.hibernate3.HibernateSystemException;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.gc.common.dal.exception.DAOException;

/**
 * The Class AbstractGenericHibernateDAO.
 * 
 * @param <DTO> the DTO
 * @param <ID> the ID
 * @author g.migliorini
 */
public abstract class AbstractGenericHibernateDAO<DTO extends IDataTransfertObject, ID extends Serializable> extends HibernateDaoSupport
    implements IGenericDAO<DTO, ID> {

  /** The Constant serialVersionUID. */
  private static final long    serialVersionUID = 6332810969432234504L;

  /** The dto class. */
  private Class<? extends DTO> dtoClass;

  /**
   * Instantiates a new abstract generic hibernate dao.
   */
  @SuppressWarnings("unchecked")
  protected AbstractGenericHibernateDAO() {
    ParameterizedType parameterizedType = (ParameterizedType) getClass().getGenericSuperclass();
    dtoClass = (Class<? extends DTO>) parameterizedType.getActualTypeArguments()[0];
  }

  /**
   * Gets the hibernate session.
   * 
   * @return the hibernate session
   */
  public Session getHibernateSession() {
    return getSession();
  }

  /**
   * {@inheritDoc}
   * 
   * @see com.gc.common.dal.dao.api.IGenericDAO#searchById(java.io.Serializable)
   */
  @SuppressWarnings("unchecked")
  public DTO searchById(ID id)
      throws DAOException {
    if (id == null) {
      return null;
    }

    getLog().debug("Getting '{}' with id: {}", getDtoClass().getName(), id);
    try {
      DTO instance = (DTO) getHibernateTemplate().get(getDtoClass(), id);
      if (instance == null) {
        getLog().debug("Get successful, no instance found");
      } else {
        getLog().debug("Get successful, instance found");
      }
      return instance;
    } catch(RuntimeException re) {
      getLog().error("Get failed", re);
      throw new DAOException(re, DAOException.ERROR_CODE.READ);
    }
  }

  /**
   * {@inheritDoc}
   * 
   * @see com.gc.common.dal.dao.api.IGenericDAO#searchByCriteria(org.hibernate.criterion.DetachedCriteria)
   */
  @SuppressWarnings("unchecked")
  public List<DTO> searchByCriteria(DetachedCriteria criteria) {
    getLog().debug("Getting '{}' with criteria: {}", getDtoClass().getName(), criteria.toString());
    List<DTO> results = getHibernateTemplate().findByCriteria(criteria);
    getLog().debug("Search by criteria successful, result size: {}", Integer.valueOf(results.size()));
    return results;
  }
  
  /**
   * {@inheritDoc}
   * @see com.gc.common.dal.dao.api.IGenericDAO#searchByProjectionCriteria(org.hibernate.criterion.DetachedCriteria)
   */
  @SuppressWarnings("unchecked")
  public List<Object[]> searchByProjectionCriteria(DetachedCriteria criteria) {
	getLog().debug("Getting '{}' with projection criteria: {}", getDtoClass().getName(), criteria.toString());
    List<Object[]> results = getHibernateTemplate().findByCriteria(criteria);
    getLog().debug("Search by projection criteria successful, result size: {}", Integer.valueOf(results.size()));
    return results;
  }

  /**
   * {@inheritDoc}
   * 
   * @see com.gc.common.dal.dao.api.IGenericDAO#searchByExample(com.gc.common.model.api.IDataTransfertObject)
   */
  public List<DTO> searchByExample(final DTO dto)
      throws DAOException {
    return searchByExample(dto, -1, -1);
  }

  /**
   * {@inheritDoc}
   * 
   * @see com.gc.common.dal.dao.api.IGenericDAO#getSearchByExampleNumber(com.gc.common.model.api.IDataTransfertObject)
   */
  public int getSearchByExampleNumber(final DTO dto) {
    getLog().debug("Gets number of result for '{}' using example '{}'", getDtoClass().getName(), dto.toString());

    try {
      DetachedCriteria criteria = DetachedCriteria.forClass(getDtoClass());
      criteria.add(Example.create(dto));
      criteria.setProjection(Projections.rowCount());

      Integer resultNumber = (Integer) getHibernateTemplate().findByCriteria(criteria).get(0);
      getLog().debug("Gets number of result successful, result number: {}", resultNumber);
      return resultNumber.intValue();
    } catch(RuntimeException re) {
      getLog().error("Gets number of result all failed", re);
      throw re;
    }
  }

  /**
   * {@inheritDoc}
   * 
   * @see com.gc.common.dal.dao.api.IGenericDAO#searchByExample(com.gc.common.model.api.IDataTransfertObject, int, int)
   */
  @SuppressWarnings("unchecked")
  public List<DTO> searchByExample(final DTO dto, int firstResult, int maxResults)
      throws DAOException {
    getLog().debug("Searching '{}' by example from {} (max results = {}, sorting = {})",
        new Object[] { getDtoClass().getName(), Integer.valueOf(firstResult), Integer.valueOf(maxResults), "" });
    try {
      DetachedCriteria criteria = DetachedCriteria.forClass(getDtoClass());
      criteria.add(Example.create(dto));

      criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

      List<DTO> results = getHibernateTemplate().findByCriteria(criteria, firstResult, maxResults);
      getLog().debug("Search by example successful, result size: {}", Integer.valueOf(results.size()));
      return results;
    } catch(RuntimeException re) {
      getLog().error("Search by example failed", re);
      throw new DAOException(re, DAOException.ERROR_CODE.READ);
    }
  }

  /**
   * {@inheritDoc}
   * 
   * @see com.gc.common.dal.dao.api.IGenericDAO#searchAll()
   */
  @SuppressWarnings("unchecked")
  public List<DTO> searchAll() {
    getLog().debug("Searching all '{}'", getDtoClass().getName());
    return getHibernateTemplate().loadAll(getDtoClass());
  }

  /**
   * {@inheritDoc}
   * 
   * @see com.gc.common.dal.dao.api.IGenericDAO#searchAll(int, int)
   */
  @SuppressWarnings("unchecked")
  public List<DTO> searchAll(int firstResult, int maxResults) {
    getLog().debug("Searching all '{}' from {} (max results = {}, sorting = {})",
        new Object[] { getDtoClass().getName(), Integer.valueOf(firstResult), Integer.valueOf(maxResults), "" });
    try {
      DetachedCriteria criteria = DetachedCriteria.forClass(getDtoClass());

      criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

      List<DTO> results = getHibernateTemplate().findByCriteria(criteria, firstResult, maxResults);
      getLog().debug("Search all successful, result size: {}", Integer.valueOf(results.size()));
      return results;
    } catch(RuntimeException re) {
      getLog().error("Search all failed", re);
      throw re;
    }
  }

  /**
   * {@inheritDoc}
   * 
   * @see com.gc.common.dal.dao.api.IGenericDAO#getSearchAllNumber()
   */
  public int getSearchAllNumber() {
    getLog().debug("Gets number of result for '{}'", getDtoClass().getName());

    try {
      DetachedCriteria criteria = DetachedCriteria.forClass(getDtoClass());
      criteria.setProjection(Projections.rowCount());

      Integer resultNumber = (Integer) getHibernateTemplate().findByCriteria(criteria).get(0);
      getLog().debug("Gets number of result successful, result number: {}", resultNumber);
      return resultNumber.intValue();
    } catch(RuntimeException re) {
      getLog().error("Gets number of result failed", re);
      throw re;
    }
  }

  /**
   * {@inheritDoc}
   * 
   * @see com.gc.common.dal.dao.api.IGenericDAO#delete(java.io.Serializable)
   */
  public void delete(ID id)
      throws DAOException {
    delete(searchById(id));
  }

  /**
   * {@inheritDoc}
   * 
   * @see com.gc.common.dal.dao.api.IGenericDAO#delete(com.gc.common.model.api.IDataTransfertObject)
   */
  public void delete(DTO dto)
      throws DAOException {
    if (isNull(dto)) {
      return;
    }

    getLog().debug("Deleting '{}'", getDtoClass().getName());
    try {
      getHibernateTemplate().delete(dto);
      getLog().debug("Delete successful");
    } catch(RuntimeException re) {
      getLog().error("Delete failed", re);
      throw new DAOException(re, DAOException.ERROR_CODE.DELETE);
    }
  }

  /**
   * {@inheritDoc}
   * 
   * @see com.gc.common.dal.dao.api.IGenericDAO#createOrUpdate(com.gc.common.model.api.IDataTransfertObject)
   */
  public void createOrUpdate(DTO dto)
      throws DAOException {
    if (isNull(dto)) {
      return;
    }

    getLog().debug("Create or Update '{}'", getDtoClass().getName());
    try {
      try {
        getHibernateTemplate().saveOrUpdate(dto);
      } catch(HibernateSystemException hse) {
        getHibernateTemplate().merge(dto);
      }
      getLog().debug("Create or Update successful");
    } catch(RuntimeException re) {
      getLog().error("Create or Update failed", re);
      throw new DAOException(re, DAOException.ERROR_CODE.CREATE_UPDATE);
    }
  }

  /**
   * {@inheritDoc}
   * 
   * @see com.gc.common.dal.dao.api.IGenericDAO#getAllId()
   */
  @SuppressWarnings("unchecked")
  public List<ID> getAllId() {
    String query = "select dto.id from " + getDtoClass().getSimpleName() + " as dto ";
    List<ID> list = (List<ID>) getSession().createQuery(query).list();
    return list;
  }

  /**
   * Gets the dto class.
   * 
   * @return the dto class
   */
  protected Class<? extends DTO> getDtoClass() {
    return dtoClass;
  }

  /**
   * Checks if is null.
   * 
   * @param dto the dto
   * @return true, if is null
   */
  private boolean isNull(DTO dto) {
    if (dto == null) {
      return true;
    }
    return false;
  }

  /**
   * {@inheritDoc}
   * 
   * @see com.gc.common.dal.dao.api.IGenericDAO#evict(com.gc.common.model.api.IDataTransfertObject)
   */
  public void evict(DTO dto) {
    this.getHibernateTemplate().evict(dto);
  }

  public void flush() {
    this.getHibernateTemplate().flush();
  }
}
