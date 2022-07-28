package moviebuddy.data;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
//import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.springframework.context.annotation.Profile;
import org.springframework.oxm.Unmarshaller;
import org.springframework.stereotype.Repository;

import moviebuddy.ApplicationException;
import moviebuddy.MovieBuddyProfile;
import moviebuddy.domain.Movie;
import moviebuddy.domain.MovieReader;

@Profile(MovieBuddyProfile.XML_MODE)
@Repository
public class XmlMovieReader implements MovieReader{
	
	//외부로부터 언마샬로 객체를 받아서 처리를 위해 변경을한다.
	/*기존의 xml Unmarshaller를 사용하고 있기 때문에 pull name으로 적용되므로, 위의 import javax.xml.bind.Unmarshaller; 를 지워주고 다시 한다.  
	private final org.springframework.oxm.Unmarshaller
	*/
	private final Unmarshaller unmarshaller;
	//unmarshaller의존성 주입을 받으려면 final을 통해 선언했기 때문에 생성자를 통해 객체를 받아야 합니다.
	public XmlMovieReader(Unmarshaller unmarshaller) {
		this.unmarshaller = Objects.requireNonNull(unmarshaller);
	}
	
	@Override
	public List<Movie> loadMovies() {
		
		try {
			/*JAXB를 직접적으로 쓰지 않을 것이다.
			final JAXBContext jaxb = JAXBContext.newInstance(MovieMetadata.class);
			final Unmarshaller unmarshaller = jaxb.createUnmarshaller();
			*/
			
			final InputStream content = ClassLoader.getSystemResourceAsStream("Movie_metadata.xml");
			final Source source = new StreamSource(content); 
			final MovieMetadata metadata = (MovieMetadata) unmarshaller.unmarshal(source);
			
			return metadata.toMovies();
		//} catch (JAXBException error) {
		} catch (IOException error) {
			throw new ApplicationException("failed to load movies data", error);
		}
		
	}
	
	// XML 문서 구조에 맞게끔 자바 객체가 매핑될 수 있도록 작성을 해줘야 합니다.
	@XmlRootElement(name = "moviemetadata")
	public static class MovieMetadata{
		
		private List<MovieData> movies;

		public List<MovieData> getMovies() {
			return movies;
		}

		public void setMovies(List<MovieData> movies) {
			this.movies = movies;
		}
		
		public List<Movie> toMovies() {
			return movies.stream().map(MovieData::toMovie).collect(Collectors.toList());
		}
	}
	
	public static class MovieData{
		
		private String title;
		private List<String> genres;
		private String language;
		private String country;
		private int releaseYear;
		private String director;
		private List<String> actors;
		private URL imdbLink;
		private String watchedDate;
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public List<String> getGenres() {
			return genres;
		}
		public void setGenres(List<String> genres) {
			this.genres = genres;
		}
		public String getLanguage() {
			return language;
		}
		public void setLanguage(String language) {
			this.language = language;
		}
		public String getCountry() {
			return country;
		}
		public void setCountry(String country) {
			this.country = country;
		}
		public int getReleaseYear() {
			return releaseYear;
		}
		public void setReleaseYear(int releaseYear) {
			this.releaseYear = releaseYear;
		}
		public String getDirector() {
			return director;
		}
		public void setDirector(String director) {
			this.director = director;
		}
		public List<String> getActors() {
			return actors;
		}
		public void setActors(List<String> actors) {
			this.actors = actors;
		}
		public URL getImdbLink() {
			return imdbLink;
		}
		public void setImdbLink(URL imdbLink) {
			this.imdbLink = imdbLink;
		}
		public String getWatchedDate() {
			return watchedDate;
		}
		public void setWatchedDate(String watchedDate) {
			this.watchedDate = watchedDate;
		}
		
		// Movie 객체를 구성하는 코드를 작성
		public Movie toMovie() {
			return Movie.of(title, genres, language, country, releaseYear, director, actors, imdbLink, watchedDate);
		}
	}
	
}
