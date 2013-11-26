package com.querydsl.example.sql.guice;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map.Entry;
import java.util.Properties;

import javax.inject.Named;
import javax.inject.Singleton;
import javax.sql.DataSource;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.matcher.Matchers;
import com.google.inject.name.Names;
import com.mchange.v2.c3p0.DataSources;
import com.mysema.query.sql.H2Templates;
import com.mysema.query.sql.SQLTemplates;

public class ServiceModule extends AbstractModule {
    @Override
    protected void configure() {
        Properties properties = new Properties();
        try {
            properties.load(getClass().getResourceAsStream("/jdbc.properties"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for (Entry<Object, Object> entry : properties.entrySet()) {
            bind(String.class).annotatedWith(Names.named((String)entry.getKey()))
                    .toInstance((String) entry.getValue());
        }

        TransactionInterceptor intercepter = new TransactionInterceptor();
        requestInjection(intercepter);
        bindInterceptor(Matchers.any(), Matchers.annotatedWith(Transactional.class), intercepter);
    }

    @Provides
    @Singleton
    public SQLTemplates templates() {
        return new H2Templates();
    }

    @Provides
    @Singleton
    public DataSource dataSource(@Named("jdbc.user") String user,
                                 @Named("jdbc.password") String password,
                                 @Named("jdbc.url") String url,
                                 @Named("jdbc.driver") String driver) {
        try {
            Class.forName(driver);
            return DataSources.pooledDataSource(DataSources.unpooledDataSource(
                    url, user, password));

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
