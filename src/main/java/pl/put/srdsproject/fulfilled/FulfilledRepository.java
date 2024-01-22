package pl.put.srdsproject.fulfilled;

import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FulfilledRepository extends ListCrudRepository<Fulfilled, String> {
    @Query("SELECT product_id, quantity FROM fulfilled WHERE quantity > 0 ALLOW FILTERING")
    List<Fulfilled> getReport();
}