package pl.put.srdsproject.util;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("cassandra")
public record CassandraConfigProperties(
        String contactPoints,
        Integer port,
        String schemaAction,
        String localDatacenter,
        String keyspaceName,
        Integer replicationFactor
) {
}
