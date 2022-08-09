package moviebuddy;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

public class CaffeineTests {
	
	@Test
	void useCache() throws InterruptedException{
		//캐시가 저장된 이후 200ms가 지나면 만료 / 이 캐시는 100개 까지의 캐시 객체를 캐싱할 수 있다.
		Cache<String, Object> cache = Caffeine.newBuilder()
				.expireAfterWrite(200, TimeUnit.MILLISECONDS)
				.maximumSize(100)
				.build();
		
		String key = "springrunner";
		Object value = new Object();
		
		//jupiter.api인 Assertions를 이용해서 Null 체크
		Assertions.assertNull(cache.getIfPresent(key));
		
		cache.put(key, value);
		Assertions.assertEquals(value, cache.getIfPresent(key));
		
		TimeUnit.MILLISECONDS.sleep(100);
		Assertions.assertEquals(value, cache.getIfPresent(key));
		
		TimeUnit.MILLISECONDS.sleep(100);
		Assertions.assertNull(cache.getIfPresent(key));
	}
}
