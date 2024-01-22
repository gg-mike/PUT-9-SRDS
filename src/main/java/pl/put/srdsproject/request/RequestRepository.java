package pl.put.srdsproject.request;

import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestRepository extends ListCrudRepository<Request, String> {

    @Query("SELECT * FROM requests WHERE application_id = '' ALLOW FILTERING")
    List<Request> findNotClaimedRequests();

    @Query("SELECT * FROM requests WHERE application_id = ?0 ALLOW FILTERING")
    List<Request> findClaimedRequests(String applicationId);

    @Query("DELETE FROM requests WHERE id IN ?0")
    void deleteProcessedRequests(List<String> requestIds);
}
