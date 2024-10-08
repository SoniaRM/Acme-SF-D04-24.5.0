
package acme.entities;

import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import acme.client.data.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class SystemConfiguration extends AbstractEntity {

	// Serialisation identifier ----------------------------------------------

	protected static final long	serialVersionUID	= 1L;

	// Attributes ------------------------------------------------------------

	@Pattern(regexp = "^[A-Z]{3}$")
	@NotBlank
	protected String			systemCurrency;

	@Pattern(regexp = "^[A-Z]{3}(,[A-Z]{3}){0,2}$")
	@NotBlank
	protected String			acceptedCurrencies;

}
