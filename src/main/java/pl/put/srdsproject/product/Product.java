package pl.put.srdsproject.product;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Table("products")
@NoArgsConstructor
@Data
public class Product {
    @Id
    @PrimaryKey("id")
    private String id;

    @Column("name")
    private String name;

    @Column("description")
    private String description;
}
