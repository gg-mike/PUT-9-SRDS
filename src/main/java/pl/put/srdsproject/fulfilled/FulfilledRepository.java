package pl.put.srdsproject.fulfilled;

import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FulfilledRepository extends ListCrudRepository<Fulfilled, String> {
    @Query("SELECT product_id, SUM(quantity) FROM Fulfilled WHERE quantity > 0 GROUP BY product_id")
    List<Fulfilled> getReport();
}