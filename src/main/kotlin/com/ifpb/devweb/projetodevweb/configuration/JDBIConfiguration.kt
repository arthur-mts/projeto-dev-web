package com.ifpb.devweb.projetodevweb.configuration

import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.KotlinPlugin
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy
import javax.sql.DataSource

@Configuration
class JDBIConfiguration {
    @Bean
    fun jdbi(dataSource: DataSource): Jdbi {
        val transactionAwareDatasourceProxy = TransactionAwareDataSourceProxy(dataSource)
        return with(Jdbi.create(transactionAwareDatasourceProxy)) {
            registerArgument(UUIDArgumentFactory())
            installPlugin(KotlinPlugin())
            this
        }
    }
}