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

    @PrimaryKeyColumn(name = "unique_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private String uniqueId;
    @PrimaryKeyColumn(name = "product_id", ordinal = 1, type = PrimaryKeyType.CLUSTERED)
    private String productId;
    @Column("handler_id")
    private String handlerId;
    @Column("request_id")
    private String requestId;

}
