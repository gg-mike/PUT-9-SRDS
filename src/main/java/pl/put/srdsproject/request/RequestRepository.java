package pl.put.srdsproject.request;

import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestRepository extends ListCrudRepository<Request, String> {

    @Query("SELECT * FROM Request WHERE applicationId = null")
    List<Request> findNotClaimedRequests();

    @Query("SELECT * FROM Request WHERE applicationId = ?0")
    List<Request> findClaimedRequests(String applicationId);

    @Query("DELETE FROM Request WHERE id IN ?0")
    void deleteProcessedRequests(List<String> requestIds);

    @Query("SELECT product_id, SUM(quantity) FROM Request WHERE quantity > 0 GROUP BY product_id")
    List<Request> getReport();
}
