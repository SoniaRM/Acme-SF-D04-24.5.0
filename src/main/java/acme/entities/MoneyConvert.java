
package acme.entities;

import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import acme.client.data.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class MoneyConvert extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@NotBlank
	private String				currency;

	@NotNull
	private Double				converter;
}
