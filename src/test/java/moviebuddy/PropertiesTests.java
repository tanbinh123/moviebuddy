package moviebuddy;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

//자바가 제공하는 프로퍼티즈, 저 수준의 코드를 작성하지 않아도 설정 정보를 읽을 수 있는 기술을 스프링이 제공하고 있다.
public class PropertiesTests {
	
	@Test
	void Load_PropertiesFile() throws IOException {
		Properties config = new Properties();
		config.load(Files.newInputStream(Paths.get("./src/test/resources/config.properties")));
	
		Assertions.assertEquals(1, config.size());
		Assertions.assertEquals("arawn", config.get("name"));
	}
}
