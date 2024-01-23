package pl.put.srdsproject.inventory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

@Table("inventory")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Inventory {

    @PrimaryKeyColumn(name = "handler_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private String handlerId;
    @PrimaryKeyColumn(name = "request_id", ordinal = 1, type = PrimaryKeyType.CLUSTERED)
    private String requestId;
    @Column("unique_id")
    private String uniqueId;
    @Column("product_id")
    private String productId;

}
