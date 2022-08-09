 package moviebuddy.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

//import com.github.benmanes.caffeine.cache.Cache;

import moviebuddy.ApplicationException;
import moviebuddy.MovieBuddyProfile;
import moviebuddy.domain.Movie;
import moviebuddy.domain.MovieReader;
import moviebuddy.util.FileSystemUtils;

//@Component
//@Repository("movieReader")
@Profile(MovieBuddyProfile.CSV_MODE)
@Repository
public class CsvMovieReader extends AbstractMetadataResourceMovieReader implements MovieReader {
	//캐시 객체
	//private final Cache<String, List<Movie>> cache;
	private final CacheManager caheManager;
	//CsvMovieReader의 생성자를 통해서 캐시를 위존 관계 주입을 받도록 한다.
	/*
	public CsvMovieReader(Cache<String, List<Movie>> cache) {
		//반드시 들어와야 하기 때문에 null체크
		this.cache = Objects.requireNonNull(cache);
	}
	*/
	public CsvMovieReader(CacheManager caheManager) {
		//반드시 들어와야 하기 때문에 null체크
		this.caheManager = Objects.requireNonNull(caheManager);
	}
	
	/**
     * 영화 메타데이터를 읽어 저장된 영화 목록을 불러온다.
     * 
     * @return 불러온 영화 목록
     */
	@Override
    public List<Movie> loadMovies() {
		//캐시에 저장된 데이터가 있다면, 즉시 반환한다.
		Cache cache = caheManager.getCache(getClass().getName());
		List<Movie> movies = cache.get("csv.movies", List.class);
		if(Objects.nonNull(movies) && movies.size() > 0 ) {
			return movies;
		}
		
        try {
            //final URI resourceUri = ClassLoader.getSystemResource("movie_metadata.csv").toURI();
        	//메타 데이터 위치를 읽을 수 있도록 설정.
        	//스프링의 리소스 인터페이스를 사용하도록 변경
        	//final URI resourceUri = ClassLoader.getSystemResource(getMetadata()).toURI();
        	final InputStream content = getmetadataResource().getInputStream();
            //final Path data = Path.of(FileSystemUtils.checkFileSystem(resourceUri));
            final Function<String, Movie> mapCsv = csv -> {
                try {
                    // split with comma
                    String[] values = csv.split(",");

                    String title = values[0];
                    List<String> genres = Arrays.asList(values[1].split("\\|"));
                    String language = values[2].trim();
                    String country = values[3].trim();
                    int releaseYear = Integer.valueOf(values[4].trim());
                    String director = values[5].trim();
                    List<String> actors = Arrays.asList(values[6].split("\\|"));
                    URL imdbLink = new URL(values[7].trim());
                    String watchedDate = values[8];

                    return Movie.of(title, genres, language, country, releaseYear, director, actors, imdbLink, watchedDate);
                } catch (IOException error) {
                    throw new ApplicationException("mapping csv to object failed.", error);
                }
            };
            
            //즉시 반환이 아닌 movie에 데이터를 담는다.
            //return new BufferedReader(new InputStreamReader(content, StandardCharsets.UTF_8))
            movies = new BufferedReader(new InputStreamReader(content, StandardCharsets.UTF_8))
                    .lines()
            //return Files.readAllLines(data, StandardCharsets.UTF_8)
                        //.stream()
                        .skip(1)
                        .map(mapCsv)
                        .collect(Collectors.toList());
        //} catch (IOException | URISyntaxException error) {
        } catch (IOException error) {
            throw new ApplicationException("failed to load movies data.", error);
        }
        
        //획득한 데이터를 캐시에 저장하고, 반환한다.
        cache.put("csv.movies", movies);
        return movies;
    }
}
