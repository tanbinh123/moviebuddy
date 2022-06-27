package moviebuddy.domain;

import java.util.List;

/**
 * @author springrunner.kr@gmail.com
 * 영화감독으로 검색을 하더나 또는 개봉 연도로 검색을 해서 검색한 결과가 일치하는지 검증하는 코드
 */
public class MovieFinerTest {

	public static void main(String[] args) {
		MovieFinder movFinder = new CsvMovieFinder();
		
		List<Movie> result = movFinder.directedBy("Michael Bay");
		assertEquals(3, result.size());

        result = movFinder.releasedYearBy(2015);
        assertEquals(225, result.size());
	}
	
	static void assertEquals(long expected, long actual) {
		if (expected != actual) {
			throw new RuntimeException(String.format("actual(%d) is different from the expected(%d)", actual, expected));			
		}
	}
	
}
