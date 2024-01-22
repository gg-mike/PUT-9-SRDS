package pl.put.srdsproject.fulfilled;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FulfilledRepository extends ListCrudRepository<Fulfilled, String> {
}