package pl.put.srdsproject.inventory;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Table("Inventory")
@Data
@NoArgsConstructor
public class Inventory {

    @PrimaryKey("id")
    private InventoryKey id;

    private Long quantity;


    public Inventory(InventoryKey id, Long quantity) {
        this.id = id;
        this.quantity = quantity;
    }
}
