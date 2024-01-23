package pl.put.srdsproject.inventory;

import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface InventoryRepository extends ListCrudRepository<Inventory, InventoryKey> {

    @Query("SELECT * FROM inventory WHERE handler_id = 'none' AND request_id = 'none' AND product_id = ?0 LIMIT ?1")
    List<Inventory> findAvailableProductsByProductId(String productId, Integer limit);

    @Query("SELECT * FROM inventory WHERE handler_id = ?0 AND request_id = ?1")
    List<Inventory> findProductsByHandlerIdAndRequestId(String handlerId, String requestId);
}
