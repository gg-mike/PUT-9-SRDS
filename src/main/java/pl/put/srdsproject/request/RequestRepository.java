package pl.put.srdsproject.request;

import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestRepository extends ListCrudRepository<Request, String> {

    @Query("SELECT * FROM requests WHERE application_id = 'none' LIMIT ?0")
    List<Request> findNotClaimedRequests(int maxFetchRequests);

    @Query("SELECT * FROM requests WHERE application_id = ?0")
    List<Request> findClaimedRequests(String applicationId);
}
