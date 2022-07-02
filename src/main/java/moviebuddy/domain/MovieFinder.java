package moviebuddy.domain;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

//영화를 불러들이고 검색하기 위한 클래스
// MovieFinder가 MovieReader 인터페이스를 통해서 동작하도록 구현
public class MovieFinder {
	//기존 MovieFinder는 단순히 new를 사용하여 객체를 생성하는 것이지만, 독립적으로 확장 가능한 클래스가 될 수 없는 상태이다.
	//private MovieReader movieReader = new CsvMovieReader();
	
	//내부에서 직접 생성하지 않고, 생성자를 통해 외부에서 입력받도록 변경한다.
	private final MovieReader movieReader;
	
	public MovieFinder(MovieReader movieReader) {
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