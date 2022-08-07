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
import org.springframework.beans.factory.annotation.Value;

import moviebuddy.ApplicationException;
import moviebuddy.domain.MovieReader;

public abstract class AbstractFileSystemMovieReader {

	private final Logger log = LoggerFactory.getLogger(getClass());
	private String metadata;

	public String getMetadata() {
		return metadata;
	}
	
	
	@Value("${movie.metadata}") //애노테이션을 이용해 빈 프로퍼티 값 설정을 지원합니다.
	public void setMetadata(String metadata) {
		//this.metadata = Objects.requireNonNull(metadata, "metadata is required value");
		this.metadata = metadata;
	}
	
	//자바의 URL 클래스로는 서블릿 컨텍스트 경로나 클라우드 스토리지 서비스에 있느 자원과 같은 것들은 표현할 수 없다.
	public URL getMetadataUrl() {
		String location = getMetadata();
		if (location.startsWith("file:")) {
			// file URL 처리
			
		} else if (location.startsWith("http:")) {
			// http URL 처리
			
		}
		
		return  ClassLoader.getSystemResource(location);
	}

	@PostConstruct
	public void afterPropertiesSet() throws Exception {
		//ClassLoader.getSystemResource() 클래스 패스 상에 있는 자원만 읽어 들일 수 있다.
		
		//형식에 맞지 않는 데이터가 오면 에러가 발생함으로, 빠르게 확인해 보는 검증 코드 추가
		//URL metadataUrl = ClassLoader.getSystemResource(metadata);
		URL metadataUrl = getMetadataUrl();
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