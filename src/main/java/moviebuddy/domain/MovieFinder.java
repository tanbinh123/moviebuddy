package moviebuddy.domain;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

//영화를 불러들이고 검색하기 위한 클래스
// MovieFinder가 MovieReader 인터페이스를 통해서 동작하도록 구현
@Service
public class MovieFinder {
	//기존 MovieFinder는 단순히 new를 사용하여 객체를 생성하는 것이지만, 독립적으로 확장 가능한 클래스가 될 수 없는 상태이다.
	//private MovieReader movieReader = new CsvMovieReader();
	
	//내부에서 직접 생성하지 않고, 생성자를 통해 외부에서 입력받도록 변경한다.
	private final MovieReader movieReader;
	
	@Autowired //의존 관계 주입을 자동으로 설정, 하나뿐인 생성자를 가진 빈을 등록할때는 @Autowired를 생략해도 된다.
	//public MovieFinder(MovieReader movieReader) {
	//public MovieFinder(@Qualifier("csvMovieReader")MovieReader movieReader) {
	//XML을 읽어 사용하도록 버전의 배포할때 배포폰을 따로따로 만들어야 한다. 
	public MovieFinder(@Qualifier("jaxbMovieReader")MovieReader movieReader) {
		this.movieReader = Objects.requireNonNull(movieReader);
	}

    /**
     * 저장된 영화 목록에서 감독으로 영화를 검색한다.
     * 
     * @param directedBy 감독
     * @return 검색된 영화 목록
     */
    public List<Movie> directedBy(String directedBy) {
        return movieReader.loadMovies().stream()
                           .filter(it -> it.getDirector().toLowerCase().contains(directedBy.toLowerCase()))
                           .collect(Collectors.toList());
    }

    /**
     * 저장된 영화 목록에서 개봉년도로 영화를 검색한다.
     * 
     * @param releasedYearBy
     * @return 검색된 영화 목록
     */
    public List<Movie> releasedYearBy(int releasedYearBy) {
        return movieReader.loadMovies().stream()
                           .filter(it -> Objects.equals(it.getReleaseYear(), releasedYearBy))
                           .collect(Collectors.toList());
    }

}