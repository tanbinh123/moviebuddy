package moviebuddy.domain;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import moviebuddy.MovieBuddyFactory;
import moviebuddy.MovieBuddyProfile;

/**
 * @author springrunner.kr@gmail.com
 * 영화감독으로 검색을 하더나 또는 개봉 연도로 검색을 해서 검색한 결과가 일치하는지 검증하는 코드
 */

//테스트 환경에서 적절한 프로파일이 활성화될 수 있도록 설정
@ActiveProfiles(MovieBuddyProfile.CSV_MODE)
@SpringJUnitConfig(MovieBuddyFactory.class)
/*
//의존성 스프링 테스트 모듈 빌드시 사용하는 애노테이션이며 JUnit을 테스트 실행 전략을 확장할때 사용한다.
//JUnit 지원 클래스로, 테스트를 실행할때 테스트에 필요한 구성하고 관리해 주는 역할.
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = MovieBuddyFactory.class) //해당 애노테이션에 지정한 빈 구성 정보를 바탕으로 스프링 컨테이너를 만듭니다.ㅍ
*/
public class MovieFinerTest {
	
	/*
	//Junit을 사용한 Test 클래스 개선
	final MovieBuddyFactory movieBuddyFactory = new MovieBuddyFactory();
	final MovieFinder movieFinder = movieBuddyFactory.movieFinder();
	*/
	
	/*
	testImplementation을 빌드 했을때
	스프링 켄텍스트, 곧 스프링 컨테이너가 구성될 거기 때문에 지워준다.
	final ApplicationContext applivationContext = 
			new AnnotationConfigApplicationContext(MovieBuddyFactory.class);
	final MovieFinder movieFinder = applivationContext.getBean(MovieFinder.class);
	*/
	
	/*
	//생성자를 통해서 의존관계를 주입한다.
	final MovieFinder movieFinder;
	@Autowired
	MovieFinerTest(MovieFinder movieFinder) {
		this.movieFinder = movieFinder;
	}
	*/
	
	/*
	//자바의 Setter 메서드로 의존 관계 주입 받기
	MovieFinder movieFinder;
	
	@Autowired
	void SetMovieFiner(MovieFinder movieFinder) {
		this.movieFinder = movieFinder;
	}
	*/
	
	//가장 간단한 필드 레벨에서 @Autowired 적용 
	//아래의 방법으로 하면 테스트하기가 쉽지 않아서 운영서버 쪽에서는 사용하지 않는다.
	@Autowired MovieFinder movieFinder;
	
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
