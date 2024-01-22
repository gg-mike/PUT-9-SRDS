package pl.put.srdsproject.inventory;

import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface InventoryRepository extends ListCrudRepository<Inventory, InventoryKey> {

    @Query("SELECT * FROM inventory WHERE product_id = ?0 AND handler_id = '' AND request_id = '' LIMIT ?1 ALLOW FILTERING")
    List<Inventory> findAvailableProductsByProductId(String productId, Integer limit);

    @Query("SELECT * FROM inventory WHERE handler_id = ?0 AND request_id = ?1 ALLOW FILTERING")
    List<Inventory> findProductsByHandlerIdAndRequestId(String handlerId, String requestId);

    @Query("SELECT product_id FROM Inventory")
    List<Inventory> getReport();
}
