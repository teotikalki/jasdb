/*
 * The JASDB software and code is Copyright protected 2011 and owned by Renze de Vries
 * 
 * All the code and design principals in the codebase are also Copyright 2011 
 * protected and owned Renze de Vries. Any unauthorized usage of the code or the 
 * design and principals as in this code is prohibited.
 */
package com.oberasoftware.jasdb.engine.search;

import com.oberasoftware.jasdb.core.properties.IntegerValue;
import com.oberasoftware.jasdb.core.properties.LongValue;
import com.oberasoftware.jasdb.api.session.Property;
import com.oberasoftware.jasdb.core.properties.StringValue;
import com.oberasoftware.jasdb.api.session.Value;
import com.oberasoftware.jasdb.api.exceptions.JasDBStorageException;
import com.oberasoftware.jasdb.api.index.keys.Key;
import com.oberasoftware.jasdb.core.index.keys.LongKey;
import com.oberasoftware.jasdb.core.index.keys.StringKey;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * User: renarj
 * Date: 1/30/12
 * Time: 8:27 PM
 */
public class PropertyKeyMapper {
    protected static Key mapToKey(Property property) throws JasDBStorageException {
        return mapToKey(property.getFirstValue());
    }
    
    protected static Set<Key> mapToKeys(Property property) throws JasDBStorageException {
        List<Value> values = property.getValues();
        Set<Key> mappedKeys = new HashSet<>(values.size());
        for(Value value : values) {
            mappedKeys.add(mapToKey(value));
        }
        return mappedKeys;
    }
    
    private static Key mapToKey(Value value) throws JasDBStorageException {
        if(value instanceof StringValue) {
            return new StringKey(value.toString());
        } else if(value instanceof LongValue) {
            return new LongKey(((LongValue)value).toLong());
        } else if(value instanceof IntegerValue) {
            throw new JasDBStorageException("Not yet implemented");
        } else {
            throw new JasDBStorageException("Unsupported key type: " + value.getClass().getName());
        }

    }
}
