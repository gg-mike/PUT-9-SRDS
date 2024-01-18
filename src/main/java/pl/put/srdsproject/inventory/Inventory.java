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

    private String handlerId;
    private String requestId;

}
