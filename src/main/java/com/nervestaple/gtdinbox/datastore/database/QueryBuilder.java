package com.nervestaple.gtdinbox.datastore.database;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Date;

/**
 * Provides a class for building queries based on class.
 */
public class QueryBuilder<T> {

    /**
     * The class for which we'll be querying.
     */
    private Class clazz;

    /**
     * Creates a new instance.
     * @param clazz The class for which we'll be querying
     */
    public QueryBuilder(Class clazz) {
        this.clazz = clazz;
    }

    /**
     * Constructs a query for finding all items of the given type that have not been deleted.
     *
     * @return TypedQuery
     * @throws DataBaseManagerException
     */
    public TypedQuery<T> buildNotDeletedQuery() throws DataBaseManagerException {

        EntityManager entityManager = getEntityManager();
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> query = builder.createQuery(clazz);
        Root<T> item = query.from(clazz);
        query.select(item).where(builder.or(builder.isNull(item.get("deleted")),
                builder.equal(item.get("deleted"), false)));

        return entityManager.createQuery(query);
    }

    /**
     * Constructs a query for finding all items of the given type that have been deleted.
     *
     * @return TypedQuery
     * @throws DataBaseManagerException
     */
    public TypedQuery<T> buildDeletedQuery() throws DataBaseManagerException {

        EntityManager entityManager = getEntityManager();
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> query = builder.createQuery(clazz);
        Root<T> item = query.from(clazz);
        query.select(item).where(builder.and(builder.isNotNull(item.get("deleted")),
                builder.equal(item.get("deleted"), true)));

        return entityManager.createQuery(query);
    }

    /**
     * Constructs a query for finding all items of the given type that have been archived,
     * based on the provided date.
     *
     * @param date All items completed before the provided date are considered "archived"
     * @return Typed Query
     * @throws DataBaseManagerException
     */
    public TypedQuery<T> buildArchivedQuery(Date date) throws DataBaseManagerException {

        EntityManager entityManager = getEntityManager();
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> query = builder.createQuery(clazz);
        Root<T> item = query.from(clazz);
        query.select(item).where(builder.lessThanOrEqualTo(item.get("completedDate"), date));

        return entityManager.createQuery(query);
    }

    private EntityManager getEntityManager() throws DataBaseManagerException {
        return DataBaseManager.getInstance().getEntityManager();
    }
}