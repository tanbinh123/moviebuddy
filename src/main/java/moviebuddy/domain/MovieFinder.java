package moviebuddy.domain;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

//영화를 불러들이고 검색하기 위한 클래스
public abstract class MovieFinder {

    /**
     * 저장된 영화 목록에서 감독으로 영화를 검색한다.
     * 
     * @param directedBy 감독
     * @return 검색된 영화 목록
     */
    public List<Movie> directedBy(String directedBy) {
        return loadMovies().stream()
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
        return loadMovies().stream()
                           .filter(it -> Objects.equals(it.getReleaseYear(), releasedYearBy))
                           .collect(Collectors.toList());
    }

    /**
     * 영화 메타데이터를 읽어 저장된 영화 목록을 불러온다.
     * 
     * @return 불러온 영화 목록
     */
    public abstract List<Movie> loadMovies();

}