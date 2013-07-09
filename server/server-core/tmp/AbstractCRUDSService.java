package com.gc;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gc.common.dal.dao.api.IGenericDAO;
import com.gc.common.dal.exception.DAOException;
import com.gc.common.dal.exception.DTOAlreadyExistsException;
import com.gc.common.dal.exception.DTONotFoundException;
import com.gc.common.dal.exception.DTONullPropertyException;
import com.gc.common.dal.service.ICRUDSService;
import com.gc.common.exception.ServiceException;
import com.gc.common.exception.ServiceException.EServiceExceptionType;
import com.gc.common.model.api.AbstractID;
import com.gc.common.model.api.IID;

/**
 * The Class AbstractCRUDSService.
 * 
 * @param <ID>
 *            The ID
 * @param <ID_TYPE>
 *            The ID Type
 */
public abstract class AbstractCRUDSService < ID extends IID < ID_TYPE >, ID_TYPE extends Serializable > implements
        ICRUDSService < ID, ID_TYPE > {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -7579819764963244505L;

    /** The log. */
    private static Logger log;

    /** The id ao. */
    private IGenericDAO < ID, ID_TYPE > idAO;

    /** The id class. */
    private Class < ? extends IID < ID_TYPE >> idClass;

    /**
     * Instantiates a new abstract cruds service.
     */
    @SuppressWarnings("unchecked")
    protected AbstractCRUDSService() {
        Type genericSuperClass = this.getClass().getGenericSuperclass();
        if (genericSuperClass instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) getClass().getGenericSuperclass();
            idClass = (Class < ? extends IID < ID_TYPE >>) parameterizedType.getActualTypeArguments()[0];
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.gc.common.service.ILoggable#getLog()
     */
    public Logger getLog() {
        if (log == null) {
            log = LoggerFactory.getLogger(this.getClass());
        }
        return log;
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.gc.common.dal.service.ICRUDSService#create(com.gc.common.model.api.IID)
     */
    public void create(ID id) throws DTONullPropertyException, DTOAlreadyExistsException, ServiceException {
        getLog().debug("Creating '{}' [{}]", idClass.toString(), id.toString());
        try {

            getAO().createOrUpdate(id);
        } catch (DAOException e) {
            throw new ServiceException(ServiceException.NO_METHOD_MESSAGE, e, EServiceExceptionType.DATABASE_ERROR);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.gc.common.dal.service.ICRUDSService#delete(com.gc.common.model.api.IID)
     */
    public void delete(ID id) throws DTONullPropertyException, DTONotFoundException, ServiceException {
        if (id != null) {
            getLog().debug("Deleting '{}' [{}]", idClass.toString(), id.toString());
            try {
                getAO().delete(id);
            } catch (DAOException e) {
                throw new ServiceException(ServiceException.NO_METHOD_MESSAGE, e, EServiceExceptionType.DATABASE_ERROR);
            }
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.gc.common.dal.service.ICRUDSService#getAO()
     */
    public IGenericDAO < ID, ID_TYPE > getAO() {
        return this.idAO;
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.gc.common.dal.service.ICRUDSService#getAll()
     */
    public List < ID > getAll() throws ServiceException {
        getLog().debug("Getting all '{}'", idClass.toString());
        try {
            List < ID > t = getAO().searchAll();
            return t;
        } catch (DAOException e) {
            throw new ServiceException(ServiceException.NO_METHOD_MESSAGE, e, EServiceExceptionType.DATABASE_ERROR);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.gc.common.dal.service.ICRUDSService#getAll(int, int)
     */
    public List < ID > getAll(int firstResult, int maxResults) throws ServiceException {
        getLog().debug("Getting all '{}'", idClass.toString());
        try {
            return getAO().searchAll(firstResult, maxResults);
        } catch (DAOException e) {
            throw new ServiceException(ServiceException.NO_METHOD_MESSAGE, e, EServiceExceptionType.DATABASE_ERROR);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.gc.common.dal.service.ICRUDSService#getAllId()
     */
    public List < ID_TYPE > getAllId() throws ServiceException {
        return getAO().getAllId();
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.gc.common.dal.service.ICRUDSService#getAllNumber()
     */
    public long getAllNumber() throws ServiceException {
        getLog().debug("Getting number of row for '{}'", idClass.toString());
        try {
            return getAO().getSearchAllNumber();
        } catch (DAOException e) {
            throw new ServiceException(ServiceException.NO_METHOD_MESSAGE, e, EServiceExceptionType.DATABASE_ERROR);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.gc.common.dal.service.ICRUDSService#getFromExample(com.gc.common.model.api.IID)
     */
    public List < ID > getFromExample(ID example) throws ServiceException {
        getLog().debug("Getting '{}' by example", idClass.toString());
        try {
            return getAO().searchByExample(example);
        } catch (DAOException e) {
            throw new ServiceException(ServiceException.NO_METHOD_MESSAGE, e, EServiceExceptionType.DATABASE_ERROR);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.gc.common.dal.service.ICRUDSService#getFromExample(com.gc.common.model.api.IID,
     *      int, int)
     */
    public List < ID > getFromExample(ID example, int firstResult, int maxResults) throws ServiceException {
        getLog().debug("Getting '{}' by example", idClass.toString());
        try {
            return getAO().searchByExample(example, firstResult, maxResults);
        } catch (DAOException e) {
            throw new ServiceException(ServiceException.NO_METHOD_MESSAGE, e, EServiceExceptionType.DATABASE_ERROR);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.gc.common.dal.service.ICRUDSService#getFromExampleNumber(com.gc.common.model.api.IID)
     */
    public long getFromExampleNumber(ID example) throws ServiceException {
        getLog().debug("Getting number of row for '{}' using example '{}'", idClass.toString(), example.toString());
        try {
            return getAO().getSearchByExampleNumber(example);
        } catch (DAOException e) {
            throw new ServiceException(ServiceException.NO_METHOD_MESSAGE, e, EServiceExceptionType.DATABASE_ERROR);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.gc.common.dal.service.ICRUDSService#getFromID(java.io.Serializable)
     */
    public ID getFromID(ID_TYPE id) throws ServiceException {
        getLog().debug("Getting '{}' with ID '{}'", idClass.toString(), id);
        try {
            return getAO().searchById(id);
        } catch (DAOException e) {
            throw new ServiceException(ServiceException.NO_METHOD_MESSAGE, e, EServiceExceptionType.DATABASE_ERROR);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.gc.common.dal.service.ICRUDSService#setAO(com.gc.common.dal.dao.api.IGenericDAO)
     */
    public void setAO(IGenericDAO < ID, ID_TYPE > idAO) {
        this.idAO = idAO;
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.gc.common.dal.service.ICRUDSService#update(com.gc.common.model.api.IID)
     */
    public void update(ID id) throws DTONullPropertyException, DTONotFoundException, ServiceException {
        getLog().debug("Updating '{}' [{}]", idClass.toString(), id.toString());
        try {
            getAO().createOrUpdate(id);
        } catch (DAOException e) {
            throw new ServiceException(ServiceException.NO_METHOD_MESSAGE, e, EServiceExceptionType.DATABASE_ERROR);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.gc.common.dal.service.ICRUDSService#buildTO()
     */
    public abstract ID buildTO();

    /**
     * {@inheritDoc}
     * 
     * @see com.gc.common.dal.service.ICRUDSService#evict(com.gc.common.model.api.IID)
     */
    public void evict(ID id) {
        this.getAO().evict(id);
    }

    public void flush() {
        ((IGenericDAO < ID, ID_TYPE >) getAO()).flush();
    }

    /**
     * Sets the ids to null (Flex default value is -1 and hibernate need a
     * 'null' id to persist this object).
     * 
     * @param dto
     *            the new id to null
     */
    protected void setIdToNull(AbstractID < Integer > dto) {
        if (dto.getId() != null && dto.getId() < 0) {
            dto.setId(null);
        }
    }

    /*
     * (non-Javadoc)
     * @see com.gc.common.dal.service.ICRUDSService#searchAllFromCriteria(org.hibernate.criterion.DetachedCriteria)
     */
    public List < ID > searchAllFromCriteria(DetachedCriteria criteria) {
        List < ID > liste = getAO().searchByCriteria(criteria);
        if (liste != null && liste.size() > 0) {
            return liste;
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * @see com.gc.common.dal.service.ICRUDSService#searchFromCriteria(org.hibernate.criterion.DetachedCriteria)
     */
    public ID searchFromCriteria(DetachedCriteria criteria) {
        List < ID > liste = getAO().searchByCriteria(criteria);
        if (liste != null && liste.size() > 0) {
            return liste.get(0);
        }
        return null;
    }

}
