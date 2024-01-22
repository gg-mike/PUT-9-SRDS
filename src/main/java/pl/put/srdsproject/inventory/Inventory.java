package pl.put.srdsproject.inventory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Table("inventory")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Inventory {

    @PrimaryKey("id")
    private InventoryKey id;
    @Column("handler_id")
    private String handlerId;
    @Column("request_id")
    private String requestId;

}
