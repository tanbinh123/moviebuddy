package moviebuddy;
// 객체를 생성하고 구성하는 역할

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import moviebuddy.domain.CsvMovieReader;
import moviebuddy.domain.MovieFinder;
import moviebuddy.domain.MovieReader;

@Configuration	
public class MovieBuddyFactory {
	
	@Bean
	public MovieReader movieReader() {
		return new CsvMovieReader();
	}
	
	/*
	주입 방식중 1번째 메서드 콜 방식 
	@Bean
	public MovieFinder movieFinder() {
		return new MovieFinder(new CsvMovieReader());
	}
	*/	


	//자바 코드로 의존 관계 주입하는 방법으로 2번째 메소드 파라미터로 받는 방식
	@Bean
	//@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE) //@Scope("prototype") //동일한 뜻이다.
	public MovieFinder movieFinder(MovieReader movieReader) {
		return new MovieFinder(movieReader());
	}
	 
}