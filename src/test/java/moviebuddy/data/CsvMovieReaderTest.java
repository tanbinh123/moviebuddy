package moviebuddy.data;

import java.io.FileNotFoundException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.cache.support.NoOpCacheManager;
import org.springframework.core.io.DefaultResourceLoader;

public class CsvMovieReaderTest {
	//정상적인 메타 데이터를 설정했을때
	@Test
	void Valid_Metadata() throws Exception {
		//AbstractFileSystemMovieReader movieReader = new CsvMovieReader();
		CsvMovieReader movieReader = new CsvMovieReader(new NoOpCacheManager());//NoOpCacheManager() 아무것도 없는 캐시 매니저의 빈 깡통 객체
		movieReader.setMetadata("movie_metadata.csv");
		//ResourceLoader가 없어서 테스트 에러가 남으로 추가 시켜준다.
		movieReader.setResourceLoader(new DefaultResourceLoader());
		
		movieReader.afterPropertiesSet();
	}
	
	//유효하지 않는 데이터를 설정했을때
	@Test
	void Invalid_Metadata() {
		//AbstractFileSystemMovieReader movieReader = new CsvMovieReader();
		CsvMovieReader movieReader = new CsvMovieReader(new NoOpCacheManager());
		movieReader.setResourceLoader(new DefaultResourceLoader());
		
		Assertions.assertThrows(FileNotFoundException.class, () -> {
			movieReader.setMetadata("invalid");
			movieReader.afterPropertiesSet();
		});
	}
	
}
