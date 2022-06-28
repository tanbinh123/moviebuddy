package moviebuddy.domain;

import java.util.List;

public interface MovieReader {
//	영화 목록을 반환하는 loadMovies라는 메소드를 가진다.
	List<Movie> loadMovies();
}
