package moviebuddy.domain;

import java.util.List;

import javax.cache.annotation.CacheResult;

public interface MovieReader {
//	영화 목록을 반환하는 loadMovies라는 메소드를 가진다.
	@CacheResult(cacheName = "movies")//각각의 클래스에 구현하지 않아도 해결되었다.
	List<Movie> loadMovies();
}
