/**
 * File: SecurityRoleSerializer.java
 * Course materials (22W) CST 8277
 *
 * @author Teddy Yap
 * @author Shariar (Shawn) Emami
 * @author Mike Norman
 *  * <p>
 *  * Updated by:  Group 14
 *  * 041000651, Chang Liu
 *  * 040991163, Wontaek Oh
 *  * 040990427, Mona Mahmoodianfard
 *  * 040997802, Gabriel Matte
 *  * ...
 *  */
package bloodbank.rest.serializer;

import java.io.IOException;
import java.io.Serializable;

import javax.ejb.EJB;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import bloodbank.ejb.PersonService;
import bloodbank.entity.BloodBank;

public class BloodBankSerializer extends StdSerializer<BloodBank> implements Serializable {
    private static final long serialVersionUID = 1L;


    private static final Logger LOG = LogManager.getLogger();

    public BloodBankSerializer() {
        this(null);
    }

    public BloodBankSerializer(Class<BloodBank> t) {
        super(t);
    }

    /**
     * This is to prevent back and forth serialization between Many to Many relations.<br>
     * This is done by setting the relation to null.
     */
    @Override
    public void serialize(BloodBank original, JsonGenerator generator, SerializerProvider provider)
            throws IOException {
        generator.writeStartObject();
        generator.writeNumberField("id", original.getId());
        generator.writeStringField("name", original.getName());
        generator.writeBooleanField("privately_owned", original.isPublic());
        generator.writeObjectField("created", original.getCreated());
        generator.writeObjectField("updated", original.getUpdated());
        generator.writeNumberField("version", original.getVersion());
        generator.writeEndObject();
    }
}