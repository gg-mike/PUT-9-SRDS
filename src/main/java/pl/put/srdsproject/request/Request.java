package pl.put.srdsproject.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Table("Request")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Request {

    @PrimaryKey("id")
    private String id;
    private String productId;
    private Long quantity;
    private String applicationId;
}
