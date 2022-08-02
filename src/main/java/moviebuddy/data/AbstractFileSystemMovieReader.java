package moviebuddy.data;

import java.io.FileNotFoundException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import moviebuddy.ApplicationException;
import moviebuddy.domain.MovieReader;

public abstract class AbstractFileSystemMovieReader {

	private final Logger log = LoggerFactory.getLogger(getClass());
	private String metadata;

	public String getMetadata() {
		return metadata;
	}

	public void setMetadata(String metadata) {
		//this.metadata = Objects.requireNonNull(metadata, "metadata is required value");
		this.metadata = metadata;
	}

	@PostConstruct
	public void afterPropertiesSet() throws Exception {
		//형식에 맞지 않는 데이터가 오면 에러가 발생함으로, 빠르게 확인해 보는 검증 코드 추가
		URL metadataUrl = ClassLoader.getSystemResource(metadata);
		if(Objects.isNull(metadataUrl)) {
			throw new FileNotFoundException(metadata);
		}
		
		//읽을 수 있는 파일인지 확인
		if (Files.isReadable(Path.of(metadataUrl.toURI())) == false ){
			throw new ApplicationException(String.format("cannot read to metadata. [%s]", metadata));
		}
		
	}

	@PreDestroy
	public void destroy() throws Exception {
		//스프링 컨테이너를 통해서 테스트 하는 것이 아니기에 로그가 직접적으로 수행되는 것을 볼 수 없다.
		log.info("Destoyed bean");
	}

}