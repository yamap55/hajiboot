package com.example;

import java.net.URI;
import java.net.URISyntaxException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import net.sf.log4jdbc.Log4jdbcProxyDataSource;

@Configuration
public class AppConfig {
	@Autowired
	DataSourceProperties dataSourceProperties;
	DataSource dataSource;

	@Bean(destroyMethod = "close")
	DataSource realDataSource() throws URISyntaxException {
		String url;
		String username;
		String password;

		String databaseUrl = System.getenv("DATABASE_URL");

		if (databaseUrl != null) {
			URI dbUri = new URI(databaseUrl);
			url = "jdbc:postgresql://" + dbUri.getHost() + dbUri.getPath() + ":" + dbUri.getPort() + dbUri.getPath();
			username = dbUri.getUserInfo().split(":")[0];
			password = dbUri.getUserInfo().split(":")[1];
		} else {
			url = this.dataSourceProperties.getUrl();
			username = this.dataSourceProperties.getUsername();
			password = this.dataSourceProperties.getPassword();
		}

		DataSourceBuilder factory = DataSourceBuilder.create(this.dataSourceProperties.getClassLoader()).url(url)
				.username(username).password(password);
		this.dataSource = factory.build();
		return this.dataSource;
	}

	@Bean
	DataSource dataSource() {
		return new Log4jdbcProxyDataSource(this.dataSource);
	}
}
