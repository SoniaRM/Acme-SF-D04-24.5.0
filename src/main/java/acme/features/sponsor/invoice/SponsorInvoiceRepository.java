
package acme.features.sponsor.invoice;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.data.accounts.UserAccount;
import acme.client.repositories.AbstractRepository;
import acme.entities.Invoice;
import acme.entities.Sponsorship;
import acme.entities.SystemConfiguration;
import acme.roles.Sponsor;

@Repository
public interface SponsorInvoiceRepository extends AbstractRepository {

	@Query("select ua from UserAccount ua where ua.id = :id")
	UserAccount findOneUserAccountById(int id);

	@Query("select i from Invoice i where i.id = :id")
	Invoice findOneInvoiceById(int id);

	@Query("select s from Sponsor s where s.id = :id")
	Sponsor findOneSponsorById(int id);

	@Query("select ss from Sponsorship ss where ss.sponsor.id = :id")
	Collection<Sponsorship> findManySponsorshipsBySponsor(int id);

	@Query("select i from Invoice i where i.sponsor.id = :id")
	Collection<Invoice> findManyInvoicesBySponsor(int id);

	@Query("select i from Invoice i where i.code = :code")
	Invoice findOneInvoiceByCode(String code);

	@Query("select s from Sponsorship s where s.id = :id")
	Sponsorship findOneSponsorshipById(int id);

	@Query("select i.sponsorship from Invoice i where i.id = :id")
	Sponsorship findOneSponsorshipByIncoiceId(int id);

	@Query("select i from Invoice i where i.sponsorship.id = :masterId")
	Collection<Invoice> findManyInvoicesByMasterId(int masterId);

	@Query("select count(i) = 0 from Invoice i where i.code = :code and i.id != :id")
	Boolean existsOtherByCodeAndId(String code, int id);

	@Query("select sys.acceptedCurrencies from SystemConfiguration sys")
	String findAcceptedCurrencies();

	// ----------------------------

	@Query("select i from Invoice i where i.sponsorship.id = :id")
	Collection<Invoice> findManyInvoicesBySponsorshipId(int id);

	@Query("select mc.converter from MoneyConvert mc where mc.currency = :currency")
	Double findMoneyConvertByMoneyCurrency(String currency);

	@Query("select sys from SystemConfiguration sys")
	SystemConfiguration findSystemConfiguration();
}
