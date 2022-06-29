package moviebuddy.domain;

import java.util.List;

public class JaxbMovieReaderTest {
	
	public static void main(String[] args) {
		JaxbMovieReader movieReader = new JaxbMovieReader();
		
		List<Movie> movies = movieReader.loadMovies();
		//movies.size() =>> XML 문서에 등록된 영화 수와 동일한가?
		MovieFinerTest.assertEquals(1375, movies.size());
		
	}
	
}