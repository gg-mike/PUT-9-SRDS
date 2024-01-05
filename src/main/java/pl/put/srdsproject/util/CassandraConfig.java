package pl.put.srdsproject.util;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.CqlSessionFactoryBean;
import org.springframework.data.cassandra.core.cql.keyspace.CreateKeyspaceSpecification;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class CassandraConfig {

    private final CassandraConfigProperties properties;


    @Bean
    public CqlSessionFactoryBean sessionFactory() {
        var session = new CqlSessionFactoryBean();
        session.setContactPoints(properties.contactPoints());
        session.setPort(properties.port());
        session.setKeyspaceCreations(
                List.of(
                        CreateKeyspaceSpecification.createKeyspace(properties.keyspaceName()).ifNotExists().withSimpleReplication(properties.replicationFactor())
                )
        );
        session.setKeyspaceName(properties.keyspaceName());
        session.setLocalDatacenter(properties.localDatacenter());
        return session;
    }


}
