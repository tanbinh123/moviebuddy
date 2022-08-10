 package moviebuddy.data;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.util.AopTestUtils;

import moviebuddy.MovieBuddyFactory;
import moviebuddy.MovieBuddyProfile;
import moviebuddy.domain.Movie;
import moviebuddy.domain.MovieReader;

@ActiveProfiles(MovieBuddyProfile.XML_MODE)
@SpringJUnitConfig(MovieBuddyFactory.class)
@TestPropertySource(properties = "movie.metadata=movie_metadata.xml") //테스트를 위한 프로퍼티 적용
public class XmlMovieReaderTest {
	//동일한 타입의 빈을 적용하는 것이 아닌 구체적인 타입으로 와이어링을 하였기 때문에
	//빈이 3개가 있지만 오류가 안나는 것이다.
	//@Autowired XmlMovieReader movieReader;
	//프락시 자료형이 달라 실패하는 Test룰 의존 관계 주입으로 해결
	@Autowired MovieReader movieReader;
	
//	JUnit 기반 테스트 코드로 변경
	@Test
	void NotEmpty_LoadedMovies() {
		//JaxbMovieReader movieReader = new JaxbMovieReader();
		
		List<Movie> movies = movieReader.loadMovies();
		//movies.size() =>> XML 문서에 등록된 영화 수와 동일한가?
		Assertions.assertEquals(1375, movies.size());
		
	}
	
	//대상 객체가 XmlMovieReader인가 확인 매서드
	@Test
	void Check_MovieReaderType() {
		Assertions.assertTrue(AopUtils.isAopProxy(movieReader));
		
		MovieReader target = AopTestUtils.getTargetObject(movieReader);
		Assertions.assertTrue(XmlMovieReader.class.isAssignableFrom(target.getClass()));
	}
} 