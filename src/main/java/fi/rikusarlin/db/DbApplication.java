package fi.rikusarlin.db;

import org.springframework.context.annotation.Bean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.data.relational.core.mapping.RelationalPersistentProperty;
import org.springframework.data.relational.core.mapping.NamingStrategy;
import org.springframework.data.util.ParsingUtils;
import org.springframework.util.Assert;

@SpringBootApplication
public class DbApplication extends SpringBootServletInitializer implements NamingStrategy{

	public static void main(String[] args) {
		SpringApplication.run(DbApplication.class, args);
	}


    	// Need to override this to avoid default snake_case (db is camel case)
	@Bean
    	public NamingStrategy namingStrategy() {
        	return new NamingStrategy() {
            		@Override
            			public String getColumnName(RelationalPersistentProperty property) {
                		Assert.notNull(property, "Property must not be null.");
                		return ParsingUtils.reconcatenateCamelCase(property.getName(), "");
            		}
        	};
    	}
}
