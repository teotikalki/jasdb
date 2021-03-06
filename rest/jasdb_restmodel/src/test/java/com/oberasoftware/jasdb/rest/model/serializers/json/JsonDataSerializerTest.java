/*
 * The JASDB software and code is Copyright protected 2011 and owned by Renze de Vries
 * 
 * All the code and design principals in the codebase are also Copyright 2011 
 * protected and owned Renze de Vries. Any unauthorized usage of the code or the 
 * design and principals as in this code is prohibited.
 */
package com.oberasoftware.jasdb.rest.model.serializers.json;

import com.oberasoftware.jasdb.api.exceptions.RestException;
import com.oberasoftware.jasdb.api.session.Entity;
import com.oberasoftware.jasdb.api.session.Property;
import com.oberasoftware.jasdb.rest.model.InstanceRest;
import com.oberasoftware.jasdb.rest.model.serializers.RestResponseHandler;
import com.oberasoftware.jasdb.rest.model.streaming.StreamedEntity;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import static org.junit.Assert.assertEquals;

/**
 * User: renarj
 * Date: 3/26/12
 * Time: 2:42 PM
 */
public class JsonDataSerializerTest {
    @Test
    public void deserializeEntityTest() throws RestException {
        String entityString = "{\"__ID\":null,\"city\":\"Amsterdam\"}";
        StreamedEntity entity = new JsonRestResponseHandler().deserialize(StreamedEntity.class, new ByteArrayInputStream(entityString.getBytes()));
        Assert.assertNotNull(entity);

        Entity mappedEntity = entity.getEntity();
        Assert.assertNotNull(entity);
        Property property = mappedEntity.getProperty("city");
        Assert.assertNotNull(property);
        Assert.assertEquals("Expected a property with value 'Amsterdam'", "Amsterdam", property.getFirstValueObject().toString());
    }

    @Test
    public void deserializeEntityMultiValueTest() throws RestException {
        String entityString = "{\"__ID\":null,\"city\":[\"Amsterdam\", \"Rotterdam\"]}";
        StreamedEntity entity = new JsonRestResponseHandler().deserialize(StreamedEntity.class, new ByteArrayInputStream(entityString.getBytes()));
        Assert.assertNotNull(entity);

        Entity mappedEntity = entity.getEntity();
        Assert.assertNotNull(entity);
        Property property = mappedEntity.getProperty("city");
        Assert.assertNotNull(property);
        Assert.assertEquals("Expected 2 values", 2, property.getValues().size());
        Assert.assertEquals("Expected a first property with value 'Amsterdam'", "Amsterdam", property.getFirstValueObject().toString());
        Assert.assertEquals("Expected a second property value with 'Rotterdam'", "Rotterdam", property.getValues().get(1).toString());

    }

    @Test
    public void testSerializeInstance() throws Exception {
        InstanceRest instance = new InstanceRest("/path", "OK", "1.0.1", "hostname");

        RestResponseHandler serializer = new JsonRestResponseHandler();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        serializer.serialize(instance, bos);
        assertEquals("{\"path\":\"/path\",\"version\":\"1.0.1\",\"instanceId\":\"hostname\",\"status\":\"OK\"}", bos.toString("UTF8"));
    }

}
