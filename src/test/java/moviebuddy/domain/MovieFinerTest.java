package moviebuddy.domain;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import moviebuddy.MovieBuddyFactory;

/**
 * @author springrunner.kr@gmail.com
 * 영화감독으로 검색을 하더나 또는 개봉 연도로 검색을 해서 검색한 결과가 일치하는지 검증하는 코드
 */
public class MovieFinerTest {
	
	/*
	//Junit을 사용한 Test 클래스 개선
	final MovieBuddyFactory movieBuddyFactory = new MovieBuddyFactory();
	final MovieFinder movieFinder = movieBuddyFactory.movieFinder();
	*/
	final ApplicationContext applivationContext = 
			new AnnotationConfigApplicationContext(MovieBuddyFactory.class);
	final MovieFinder movieFinder = applivationContext.getBean(MovieFinder.class);
	
	@Test
	void NotEmpty_directedBy() {
		List<Movie> movies = movieFinder.directedBy("Michael Bay");
		Assertions.assertEquals(3, movies.size());
	}
	
	@Test
	void NotEmpty_ReleasedYearBy() {
		List<Movie> movies = movieFinder.releasedYearBy(2015);
		Assertions.assertEquals(225, movies.size());
	}
	
	/*
	public static void main(String[] args) {
		//MovieFinder movFinder = new MovieFinder();
		//외부에서 받도록 변경
		//MovieFinder movFinder = new MovieFinder(new CsvMovieReader());
		//중복되어 생성되는 객체를 Factory를 통한 역할과 책임 분리
		//MovieBuddyFactory movieBuddyFactory = new MovieBuddyFactory();
		//MovieFinder movieFinder = movieBuddyFactory.movieFinder();
		
		List<Movie> result = movieFinder.directedBy("Michael Bay");
		assertEquals(3, result.size());

        result = movieFinder.releasedYearBy(2015);
        assertEquals(225, result.size());
	}
	
	static void assertEquals(long expected, long actual) {
		if (expected != actual) {
			throw new RuntimeException(String.format("actual(%d) is different from the expected(%d)", actual, expected));			
		}
	}
	*/
	
}
