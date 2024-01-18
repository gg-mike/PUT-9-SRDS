package pl.put.srdsproject.fulfillment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Table("Fulfillment")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Fulfilled {
    @PrimaryKey("id")
    private String id;
    private String productId;
    private Long quantity;
    private String applicationId;
}
