
package acme.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import acme.client.data.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Banner extends AbstractEntity {

	// Serialisation identifier -----------------------------------------------

	private static final long	serialVersionUID	= 1L;

	//Atributes ---------------------------------------------------------------

	@NotNull
	@PastOrPresent
	@Temporal(TemporalType.TIMESTAMP)
	private Date				instationUpdateMoment;

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	private Date				periodStart;

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	private Date				periodFinish;

	@URL
	@NotBlank
	@Length(max = 255)
	private String				linkPicture;

	@NotBlank
	@Length(max = 75)
	private String				slogan;

	@URL
	@NotBlank
	@Length(max = 255)
	private String				linkDocument;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------
}
