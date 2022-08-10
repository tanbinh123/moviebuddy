package moviebuddy.data;

import java.util.List;
import java.util.Objects;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import moviebuddy.domain.Movie;
import moviebuddy.domain.MovieReader;

public class CachingMovieReader implements MovieReader {
	
	static final String CACHE_NAME = CachingMovieReader.class.getName();
	static final String Cache_KEY = "movies";

	private final CacheManager cacheManager;
	private final MovieReader target; //MovieReader 타입의 대상 객체를 받아옵니다.
	
	public CachingMovieReader(CacheManager cacheManager, MovieReader target) {
		this.cacheManager = Objects.requireNonNull(cacheManager);
		this.target = Objects.requireNonNull(target);
	}
	
	@Override
	public List<Movie> loadMovies() {
		// 캐시된 데이터가 있으면 즉시 반환 처리
		Cache cache = cacheManager.getCache(CACHE_NAME);// 클래스의 이름으로 캐시를 꺼내도록 한다.
		List<Movie> movies = cache.get(Cache_KEY, List.class);
		if(Objects.nonNull(movies)) {	
			return movies;
		}
		
		// 캐시된 데이터가 없으면, 대상 객체에게 명령을 위임하고, 반환 받은 값을 캐시에 저장 후 반환 처리
		movies = target.loadMovies();
		cache.put(Cache_KEY, movies);//캐시에 데이터 넣기
		
		return movies;
	}
	
	public List<Movie> getCachedData(){
		// 캐시된 데이터가 있으면 즉시 반환 처리
		Cache cache = cacheManager.getCache(CACHE_NAME);// 클래스의 이름으로 캐시를 꺼내도록 한다.
		return cache.get(Cache_KEY, List.class);
	}
	
}
