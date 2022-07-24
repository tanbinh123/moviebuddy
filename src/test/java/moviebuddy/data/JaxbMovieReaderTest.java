package moviebuddy.data;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import moviebuddy.MovieBuddyFactory;
import moviebuddy.MovieBuddyProfile;
import moviebuddy.data.JaxbMovieReader;
import moviebuddy.domain.Movie;

@ActiveProfiles(MovieBuddyProfile.XML_MODE)
@SpringJUnitConfig(MovieBuddyFactory.class)
public class JaxbMovieReaderTest {
	//동일한 타입의 빈을 적용하는 것이 아닌 구체적인 타입으로 와이어링을 하였기 때문에
	//빈이 3개가 있지만 오류가 안나는 것이다.
	@Autowired JaxbMovieReader movieReader;
	
//	JUnit 기반 테스트 코드로 변경
	@Test
	void NotEmpty_LoadedMovies() {
		//JaxbMovieReader movieReader = new JaxbMovieReader();
		
		List<Movie> movies = movieReader.loadMovies();
		//movies.size() =>> XML 문서에 등록된 영화 수와 동일한가?
		Assertions.assertEquals(1375, movies.size());
		
	}
	
}