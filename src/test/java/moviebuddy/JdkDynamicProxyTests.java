package moviebuddy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Objects;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.DefaultResourceLoader;

import moviebuddy.data.CsvMovieReader;
import moviebuddy.domain.MovieReader;

public class JdkDynamicProxyTests {
	@Test
	void useDynamicProxy() throws Exception {
		//movieReader의 실행 시간을 측정하는 프락시를 작성
		CsvMovieReader movieReader = new CsvMovieReader();
		movieReader.setResourceLoader(new DefaultResourceLoader());
		movieReader.setMetadata("movie_metadata.csv");
		movieReader.afterPropertiesSet();
		
		ClassLoader classLoader = JdkDynamicProxyTests.class.getClassLoader();
		Class<?>[] interfaces = new Class[] {MovieReader.class};
		InvocationHandler handler = new PerformanceInvocationHandler(movieReader);
		
		MovieReader proxy = (MovieReader) Proxy.newProxyInstance(classLoader, interfaces, handler);
		
		proxy.loadMovies();
		proxy.loadMovies();
	}
	//inner 클래스
	static class PerformanceInvocationHandler implements InvocationHandler {
		
		final Logger log = LoggerFactory.getLogger(getClass());
		final Object target;
		
		PerformanceInvocationHandler(Object target){
			this.target = Objects.requireNonNull(target);
		}
		
		@Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			long start = System.currentTimeMillis();//시작 시간 기록
			Object result = method.invoke(target, args);//실제로 대상 객체를 호출
			long elapsed = System.currentTimeMillis() - start;//최종으로 얼마의 시간이 걸렸는지 계산
			
			log.info("Execution {} method finished in {} ms", method.getName(), elapsed);
			
			return null;
		}
		
	}
}
