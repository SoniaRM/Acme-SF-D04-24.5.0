
package acme.features.authenticated.notice;

import java.util.Collection;
import java.util.Date;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.Notice;

@Repository
public interface AuthenticatedNoticeRepository extends AbstractRepository {

	@Query("select n from Notice n where n.instantiationMoment >= :date")
	Collection<Notice> findNoticesInLastMonth(Date date);

	@Query("select n from Notice n where n.id = :id")
	Notice findOneNoticeById(int id);

}
