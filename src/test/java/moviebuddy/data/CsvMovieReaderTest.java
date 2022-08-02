package moviebuddy.data;

import java.io.FileNotFoundException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CsvMovieReaderTest {
	//정상적인 메타 데이터를 설정했을때
	@Test
	void Valid_Metadata() throws Exception {
		AbstractFileSystemMovieReader movieReader = new CsvMovieReader();
		movieReader.setMetadata("movie_metadata.csv");
		
		movieReader.afterPropertiesSet();
	}
	
	//유효하지 않는 데이터를 설정했을때
	@Test
	void Invalid_Metadata() {
		AbstractFileSystemMovieReader movieReader = new CsvMovieReader();
		
		Assertions.assertThrows(FileNotFoundException.class, () -> {
			movieReader.setMetadata("invalid");
			movieReader.afterPropertiesSet();
		});
	}
	
}
