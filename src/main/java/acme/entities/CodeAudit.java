
package acme.entities;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import acme.client.data.AbstractEntity;
import acme.enumerated.Mark;
import acme.enumerated.Type;
import acme.roles.Auditor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter

public class CodeAudit extends AbstractEntity {

	// Serialisation identifier -----------------------------------------------

	private static final long	serialVersionUID	= 1L;

	//Atributes ---------------------------------------------------------------

	@NotBlank
	@Column(unique = true)
	@Pattern(regexp = "^[A-Z]{1,3}-\\d{3}$", message = "{validation.codeAudit.code}")
	private String				code;

	@NotNull
	@Past
	@Temporal(TemporalType.TIMESTAMP)
	private Date				execution;

	@NotNull
	private Type				type;

	@NotBlank
	@Length(max = 100)
	private String				correctiveActions;

	@URL
	@Length(max = 255)
	private String				optionalLink;

	private boolean				draftMode;

	// Derived attributes -----------------------------------------------------


	@Transient
	public Mark getMark(final Collection<AuditRecord> records) {
		Map<Mark, Integer> frecMap = new HashMap<>();
		for (AuditRecord r : records) {
			Mark mark = r.getMark();
			frecMap.put(mark, frecMap.getOrDefault(mark, 0) + 1);
		}
		Mark mode = null;
		int max = 0;
		for (Map.Entry<Mark, Integer> entry : frecMap.entrySet())
			if (entry.getValue() > max) {
				max = entry.getValue();
				mode = entry.getKey();
			}

		return mode;
	}

	// Relationships ----------------------------------------------------------


	@NotNull
	@Valid
	@ManyToOne(optional = false)
	private Project	project;

	@NotNull
	@Valid
	@ManyToOne(optional = false)
	private Auditor	auditor;

}
