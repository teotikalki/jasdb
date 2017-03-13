package com.oberasoftware.jasdb.entitymapper.types;

import com.oberasoftware.jasdb.api.entitymapper.PropertyMetadata;
import com.oberasoftware.jasdb.api.entitymapper.TypeMapper;
import com.oberasoftware.jasdb.api.exceptions.JasDBStorageException;
import com.oberasoftware.jasdb.api.exceptions.RuntimeJasDBException;
import com.oberasoftware.jasdb.api.session.Property;
import com.oberasoftware.jasdb.api.session.Value;
import com.oberasoftware.jasdb.core.properties.MultivalueProperty;

import java.util.ArrayList;
import java.util.List;

import static com.oberasoftware.jasdb.entitymapper.types.TypeMapperFactory.getTypeMapper;

/**
 * @author Renze de Vries
 */
public class ListEntityMapper implements TypeMapper<List<?>> {
    @Override
    public boolean isSupportedType(Class<?> type) {
        return List.class.isAssignableFrom(type);
    }

    @Override
    public List<?> mapToRawType(Object value) {
        if(value instanceof List) {
            return (List<?>) value;
        } else {
            throw new RuntimeJasDBException("Invalid type, cannot cast object: " + value + " to a list");
        }
    }

    @Override
    public Object mapToEmptyValue() {
        return new ArrayList<>();
    }

    @Override
    public Value mapToValue(Object value) {
        return null;
    }

    @Override
    public Property mapToProperty(String propertyName, Object value) {
        Property property = new MultivalueProperty(propertyName, true);
        List<?> values = mapToRawType(value);
        values.forEach(v -> {
            try {
                TypeMapper typeMapper = getTypeMapper(v.getClass());
                property.addValue(typeMapper.mapToValue(v));
            } catch (JasDBStorageException e) {
                throw new RuntimeJasDBException("Unable to map list", e);
            }

        });

        return property;
    }

    @Override
    public Object mapFromProperty(PropertyMetadata propertyMetadata, Property property) {
        return property.getValueObjects();
    }
}
