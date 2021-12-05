/**
 * File: RestConfig.java Course materials (21F) CST 8277
 *
 * @author Teddy Yap
 * @author Shariar (Shawn) Emami
 * @date Mar 31, 2021
 * @author Mike Norman
 * @date 2020 10
 */
package bloodbank.rest.serializer;

import java.io.IOException;
import java.io.Serializable;

import javax.ejb.EJB;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Hibernate;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import bloodbank.ejb.BloodBankService;
import bloodbank.entity.BloodBank;

public class BloodBankSerializer extends StdSerializer<BloodBank> implements Serializable {
	private static final long serialVersionUID = 1L;
	
	
	private static final Logger LOG = LogManager.getLogger();
	
    @EJB
    protected BloodBankService service;

	public BloodBankSerializer() {
		this(null);
	}

	public BloodBankSerializer(Class< BloodBank> t) {
		super(t);
	}

	/**
	 * This is to prevent back and forth serialization between Many to Many relations.<br>
	 * This is done by setting the relation to null.
	 */
	@Override
	public void serialize(BloodBank original, JsonGenerator generator, SerializerProvider provider)
			throws IOException {
		LOG.trace("serializeing={}",original);
		generator.writeStartObject();
		generator.writeNumberField("id", original.getId());
		generator.writeStringField("name", original.getName());
		if(original.getDonations()!=null && Hibernate.isInitialized(original.getDonations())) {
			generator.writeNumberField( "donation_count", original.getDonations().size());
		}
		generator.writeBooleanField("is_public", original.isPublic());
		generator.writeObjectField("created", original.getCreated());
		generator.writeObjectField("updated", original.getUpdated());
		generator.writeNumberField("version", original.getVersion());
		generator.writeEndObject();
	}
}