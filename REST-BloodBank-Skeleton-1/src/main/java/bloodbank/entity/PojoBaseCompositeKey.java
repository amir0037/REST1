/***************************************************************************
 * File: PojoBaseCompositeKey.java Course materials (21F) CST 8277
 * 
 * @author Teddy Yap
 * @author Shariar (Shawn) Emami
 * @date Mar 9, 2021
 * @author Mike Norman
 * @date 2020 04
 */
package bloodbank.entity;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import javax.persistence.Version;

/**
 * Abstract class that is base of (class) hierarchy for all @Entity classes
 * 
 * @param <ID> - type of composit key used
 */
@MappedSuperclass
//@Access(AccessType.FIELD)
@Access(AccessType.PROPERTY)
@EntityListeners(PojoCompositeListener.class)
public abstract class PojoBaseCompositeKey<ID extends Serializable> implements Serializable {
	private static final long serialVersionUID = 1L;

	protected int version;
	protected long epochCreated;
	protected long epochUpdated;
	protected Instant created;
	protected Instant updated;

	@Transient
	public abstract ID getId();
	public abstract void setId( ID id);

    @Version
	public int getVersion() {
		return version;
	}
	public void setVersion( int version) {
		this.version = version;
	}

	@Transient
	public Instant getCreated() {
		if ( created == null) {
			setCreatedEpochMilli( epochCreated);
		}
		return created;
	}
	public void setCreated( Instant created) {
		setCreatedEpochMilli( created.toEpochMilli());
	}

    @Column( name = "created")
	public long getCreatedEpochMilli() {
		return created.toEpochMilli();
	}
	public void setCreatedEpochMilli( long created) {
		this.epochCreated = created;
		this.created = Instant.ofEpochMilli( created);
	}

	@Transient
	public Instant getUpdated() {
		if ( updated == null) {
			setUpdatedEpochMilli( epochUpdated);
		}
		return updated;
	}
	public void setUpdated( Instant updated) {
		setUpdatedEpochMilli( updated.toEpochMilli());
	}

    @Column( name = "updated")
	public long getUpdatedEpochMilli() {
		return updated.toEpochMilli();
	}
	public void setUpdatedEpochMilli( long updated) {
		this.epochUpdated = updated;
		this.updated = Instant.ofEpochMilli( updated);
	}

	/**
	 * @see <a href= "https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/">
	 *      How to implement equals and hashCode using the JPA entity identifier (Primary Key) </a>
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		return prime * result + Objects.hash(getId());
	}

	@Override
	public boolean equals( Object obj) {
		if ( this == obj) {
			return true;
		}
		if ( obj == null) {
			return false;
		}
		if ( !( obj instanceof PojoBaseCompositeKey)) {
			return false;
		}
		@SuppressWarnings("rawtypes")
		PojoBaseCompositeKey other = (PojoBaseCompositeKey)obj;
		return Objects.equals(getId(), other.getId());
	}
}