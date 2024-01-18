package pl.put.srdsproject.inventory;

import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface InventoryRepository extends ListCrudRepository<Inventory, InventoryKey> {

    @Query("SELECT * FROM Inventory WHERE id.productId = ?0 AND handlerId is null AND requestId is null LIMIT ?1")
    List<Inventory> findAvailableProductsByProductId(String productId, Long limit);

    @Query("SELECT * FROM Inventory WHERE id.handlerId = ?0 AND id.requestId = ?1")
    List<Inventory> findProductsByHandlerIdAndRequestId(String handlerId, String requestId);

//    @Query("SELECT product_id, COUNT(*) FROM Inventory GROUP BY product_id")
//    List<Inventory> getReport();
}
