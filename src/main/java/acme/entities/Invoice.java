
package acme.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.URL;

import acme.client.data.AbstractEntity;
import acme.client.data.datatypes.Money;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Invoice extends AbstractEntity {

	// Serialisation identifier -----------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@NotBlank
	@Column(unique = true)
	@Pattern(regexp = "^IN-\\d{4}-\\d{4}$", message = "{validation.invoice.code}")
	private String				code;

	@PastOrPresent
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	private Date				registrationTime;

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	private Date				dueDate;

	@Valid
	@NotNull
	private Money				quantity;

	@Range(min = 0, max = 100)
	@Digits(integer = 3, fraction = 2)
	private double				tax;

	@URL
	@Length(max = 255)
	private String				link;

	private boolean				draftMode;

	// Derived attributes -----------------------------------------------------


	@Transient
	public double totalAmount() {
		double result;
		double amount;

		if (this.quantity == null)
			amount = 0.0;
		else
			amount = this.quantity.getAmount();

		result = amount + amount * (this.tax / 100.0);

		return result;
	}

	// Relationships ----------------------------------------------------------


	@NotNull
	@Valid
	@ManyToOne(optional = false)
	private Sponsorship sponsorship;

}
