package moviebuddy;
// 객체를 생성하고 구성하는 역할

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Scope;

import moviebuddy.data.CsvMovieReader;
import moviebuddy.domain.MovieFinder;
import moviebuddy.domain.MovieReader;

//@Import은 다른 클래스에서 빈 구성 정보를 불러오기 위해서 사용하는 것이다.
//XML을 불러오고 싶으면, @ImportResource("xml file location") 지정된 곳의 XML파일을 읽어서 불러올 수 있다.
@Configuration
@ComponentScan
//@ComponentScan(basePackages = { "moviebuddy" }) //패키지를 지정하고 싶을때
@Import({ MovieBuddyFactory.DomainModuleConfig.class, MovieBuddyFactory.DataSourceModuleConfig.class })
public class MovieBuddyFactory {
	//아래의 2개 클래스는 빈 구성 정보로 사용할 거기 때문에 @Configuration 을 붙인다.
	@Configuration
	static class DomainModuleConfig {
		//자바 코드로 의존 관계 주입하는 방법으로 2번째 메소드 파라미터로 받는 방식
		/*
		@Bean
		//@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE) //@Scope("prototype") //동일한 뜻이다.
		public MovieFinder movieFinder(MovieReader movieReader) {
			return new MovieFinder(movieReader);
		}
		*/
	}
	
	@Configuration
	static class DataSourceModuleConfig {
		/*
		ComponentScan을 하기 위해 제거 
		@Bean
		public MovieReader movieReader() {
			return new CsvMovieReader();
		}
		*/
		
		/*
		주입 방식중 1번째 메서드 콜 방식 
		@Bean
		public MovieFinder movieFinder() {
			return new MovieFinder(new CsvMovieReader());
		}
		*/	
	}
	
}