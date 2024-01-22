package pl.put.srdsproject.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Table("requests")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Request {

    @PrimaryKey("id")
    private String id;
    @Column("product_id")
    private String productId;
    @Column("quantity")
    private Long quantity;
    @Column("application_id")
    private String applicationId;
}
