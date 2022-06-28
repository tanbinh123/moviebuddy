package moviebuddy.domain;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

//영화를 불러들이고 검색하기 위한 클래스
// MovieFinder가 MovieReader 인터페이스를 통해서 동작하도록 구현
public class MovieFinder {
	
	private MovieReader movieReader = new CsvMovieReader();

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