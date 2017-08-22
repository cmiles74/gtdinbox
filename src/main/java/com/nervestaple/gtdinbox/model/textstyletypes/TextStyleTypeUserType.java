package com.nervestaple.gtdinbox.model.textstyletypes;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.UserType;
import org.hibernate.HibernateException;
import org.apache.commons.collections.CollectionUtils;

import java.sql.Types;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.io.Serializable;
import java.util.Arrays;

/**
 * Provides a Hibernate UserType for storing the TextStyleType to the database for an entity.
 *
 * @author Christopher Miles
 * @version 1.0
 */
public class TextStyleTypeUserType implements UserType {

    /**
     * Types that can be used to persist this user type to the database.
     */
    public static final int[] SQL_TYPES = { Types.VARCHAR };

    public int[] sqlTypes() {
        return( SQL_TYPES );
    }

    /**
     * The class this user type is representing.
     * @return TextStyleType
     */
    public Class returnedClass() {
        return( TextStyleType.class );
    }

    public boolean equals( Object object1, Object object2 ) {

        return( object1 == object2 );
    }

    public int hashCode( Object object ) throws HibernateException {

        return( object.hashCode() );
    }

    /**
     * Return a "deep copy" of the object for comparing snapshot states.
     * @param object object
     * @return object
     */
    public Object deepCopy( Object object ) {

        return( object );
    }

    /**
     * Informs Hibernate wether or not this class is mutable.
     * @return false
     */
    public boolean isMutable() {

        return( false );
    }

    /**
     * Transform the object into its cache-able representation.
     * @param value TextStyleType
     * @return Serializable
     * @throws HibernateException
     */
    public Serializable disassemble( Object value ) throws HibernateException {

        return( ( Serializable ) value );
    }

    /**
     * Turn a cache-able version of the object back into a real object.
     * @param cached Serializable
     * @param owner Entity
     * @return TextStyleType
     * @throws HibernateException
     */
    public Object assemble( Serializable cached, Object owner ) throws HibernateException {

        return( cached );
    }

    /**
     * During a merge, replace the existing targe on an entity with the original.
     * @param original TextStyleType
     * @param target TestSyleType
     * @param owner Entity
     * @return TextStyleType
     * @throws HibernateException
     */
    public Object replace( Object original, Object target, Object owner ) throws HibernateException {

        return( original );
    }

    /**
     * Returns a TextStyleType for the given result set.
     * @param resultSet Database results
     * @param names Result column names
     * @param owner Entity
     * @return TextStyleType
     * @throws HibernateException
     * @throws SQLException
     */
    @Override
    public Object nullSafeGet(ResultSet resultSet, String[] names,
                              SharedSessionContractImplementor owner,
                              Object o) throws HibernateException, SQLException {

        String name = resultSet.getString( names[ 0 ] );

        return( resultSet.wasNull() ? null : TextStyleType.getInstance( name ) );
    }

    /**
     * Alters the prepared statement to store the VARCHAR equivalent of our TextStyleType.
     * @param statement Database query
     * @param value TextStyleType
     * @param index Index in query to change
     * @param owner Entity
     * @throws HibernateException
     * @throws SQLException
     */
    @Override
    public void nullSafeSet(PreparedStatement statement, Object value, int index,
                            SharedSessionContractImplementor owner)
            throws HibernateException, SQLException {

        if( value == null ) {
            statement.setNull( index, Types.VARCHAR );
        } else {
            statement.setString( index, value.toString() );
        }
    }
}
