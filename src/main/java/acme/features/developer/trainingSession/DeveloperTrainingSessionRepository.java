
package acme.features.developer.trainingSession;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.TrainingModule;
import acme.entities.TrainingSession;

@Repository
public interface DeveloperTrainingSessionRepository extends AbstractRepository {

	@Query("select ts from TrainingSession ts where ts.trainingModule.developer.id = :developerId")
	Collection<TrainingSession> findManyTrainingSessionsByDeveloperId(int developerId);

	@Query("select tm from TrainingModule tm where tm.id = :id")
	TrainingModule findOneTrainingModuleById(int id);

	@Query("select ts from TrainingSession ts where ts.code = :code")
	TrainingSession findOneTrainingSessionByCode(String code);

	@Query("SELECT tm FROM TrainingModule tm JOIN tm.project p WHERE tm.draftMode = true AND p.draftMode = true")
	Collection<TrainingModule> findManyTrainingModulesAvailable();

	@Query("select ts from TrainingSession ts where ts.id = :trainingSessionId")
	TrainingSession findOneTrainingSessionById(int trainingSessionId);

	@Query("select tm from TrainingModule tm")
	Collection<TrainingModule> findAllTrainingModules();

	@Query("select tm from TrainingModule tm where tm.developer.id = :id")
	Collection<TrainingModule> findAllTrainingModulesByDeveloperId(int id);

	@Query("select tm from TrainingModule tm where tm.draftMode = true")
	Collection<TrainingModule> findManyTrainingModulesAvailable2();

	@Query("select ts from TrainingSession ts where ts.trainingModule.id = :masterId")
	Collection<TrainingSession> findManyTrainingSessionsbyTrainingModuleId(int masterId);

}
