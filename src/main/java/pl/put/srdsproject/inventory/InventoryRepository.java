package pl.put.srdsproject.inventory;

import org.springframework.data.repository.ListCrudRepository;

public interface InventoryRepository extends ListCrudRepository<Inventory, InventoryKey> {

}
