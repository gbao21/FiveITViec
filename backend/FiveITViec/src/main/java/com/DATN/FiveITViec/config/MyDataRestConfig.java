package com.DATN.FiveITViec.config;

import com.DATN.FiveITViec.dto.UserProfileDTO;
import com.DATN.FiveITViec.model.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class MyDataRestConfig implements RepositoryRestConfigurer {

	private String theAllowedOrigins = "https://www.fiveitviec.online/";
	private String theAllowedOrigin1 = "http://localhost:3000/";

	@Override
	public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {

		config.exposeIdsFor(Job.class);
		config.exposeIdsFor(JobCategory.class);
		config.exposeIdsFor(User.class);
		config.exposeIdsFor(Roles.class);
		config.exposeIdsFor(Contact.class);
		config.exposeIdsFor(Blog.class);
		config.exposeIdsFor(ProfileEmployer.class);
		config.exposeIdsFor(ProfileCandidate.class);
		config.exposeIdsFor(Specialization.class);
		config.exposeIdsFor(ReviewAndRating.class);
		config.exposeIdsFor(BlogComment.class);
		config.exposeIdsFor(Applicant.class);




		/* Configure CORS Mapping */
		cors.addMapping(config.getBasePath() + "/**").allowedOrigins(theAllowedOrigins);
		cors.addMapping(config.getBasePath() + "/**").allowedOrigins(theAllowedOrigin1);
	}

	private void disableHttpMethods(Class theClass, RepositoryRestConfiguration config,
			HttpMethod[] theUnsupportedActions) {
		config.getExposureConfiguration().forDomainType(theClass)
				.withItemExposure((metadata, httpMethods) -> httpMethods.disable(theUnsupportedActions))
				.withCollectionExposure((metadata, httpMethods) -> httpMethods.disable(theUnsupportedActions));
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**") // Allow CORS for all endpoints
						.allowedOrigins(theAllowedOrigins, theAllowedOrigin1) // Replace with your front-end origin
						.allowedMethods("GET", "POST", "PUT", "DELETE")
						.allowedHeaders("*");
			}
		};
	}



}
