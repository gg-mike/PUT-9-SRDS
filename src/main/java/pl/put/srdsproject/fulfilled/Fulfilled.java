package pl.put.srdsproject.fulfilled;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;
import pl.put.srdsproject.request.Request;

@Table("fulfilled")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Fulfilled {
    @PrimaryKey("id")
    private String id;
    @Column("product_id")
    private String productId;
    @Column("quantity")
    private Long quantity;
    @Column("application_id")
    private String applicationId;

    public Fulfilled(Request request) {
        this.id = request.getId();
        this.productId = request.getProductId();
        this.quantity = request.getQuantity();
        this.applicationId = request.getApplicationId();
    }
}
