
package acme.features.sponsor.sponsorship;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.Invoice;
import acme.entities.Project;
import acme.entities.Sponsorship;
import acme.roles.Sponsor;

@Repository
public interface SponsorSponsorshipRepository extends AbstractRepository {

	@Query("select s from Sponsorship s")
	Collection<Sponsorship> findAllSponsorships();

	@Query("select s from Sponsorship s where s.id = :id")
	Sponsorship findOneSponsorshipById(int id);

	@Query("select s from Sponsorship s where s.sponsor.id = :sponsorId")
	Collection<Sponsorship> findManySponsorshipsBySponsorId(int sponsorId);

	@Query("select s from Sponsor s where s.id = :id")
	Sponsor findOneSponsorById(int id);

	@Query("select p from Project p where p.id = :projectId")
	Project findOneProjectById(int projectId);

	@Query("select p from Project p where p.draftMode = false")
	Collection<Project> findManyPublishedProjects();

	@Query("select s from Sponsorship s where s.code = :code")
	Sponsorship findOneSponsorshipByCode(String code);

	@Query("select i from Invoice i where i.sponsorship.id = :sponsorshipId")
	Collection<Invoice> findManyInvoicesBySponsorshipId(int sponsorshipId);

	@Query("select i from Invoice i where (i.sponsorship.id = :sponsorshipId and i.draftMode = true)")
	Collection<Invoice> findManyUnpublishedInvoicesBySponsorshipId(int sponsorshipId);
}
