package pl.put.srdsproject.fulfilled;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;
import pl.put.srdsproject.request.Request;

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

    public Fulfilled(Request request) {
        this.id = request.getId();
        this.productId = request.getProductId();
        this.quantity = request.getQuantity();
        this.applicationId = request.getApplicationId();
    }
}
