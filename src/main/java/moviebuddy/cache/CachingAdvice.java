package moviebuddy.cache;

import java.util.Objects;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.util.ClassUtils;

import moviebuddy.domain.MovieReader;

public class CachingAdvice implements MethodInterceptor{

	private final Logger log = LoggerFactory.getLogger(getClass());
	private final CacheManager cacheManager;

	public CachingAdvice(CacheManager cacheManager) {
		this.cacheManager = Objects.requireNonNull(cacheManager);
	}
	
	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		//부가기능을 적용할 대상 객체나 메서드를 판별하기
		/*기껏 분리한 것을 다시 섞이는 꼴이기 때문에 제거
		//대상 객체가 MovieReader 타입이 아니면 즉시 반환하기
		if (!ClassUtils.isAssignableValue(MovieReader.class, invocation.getThis())) {
			return invocation.proceed();
		}
		
		//메서드 이름이 loadMovies가 아니면 즉시 반환하기
		if (!"loadMovies".equals(invocation.getMethod().getName())) {
			return invocation.proceed();
		}
		*/
		
		//캐시된 데이터 존재하면, 즉시 반환 처리
		Cache cache = cacheManager.getCache(invocation.getThis().getClass().getName());
		Object cachedValue = cache.get(invocation.getMethod().getName(), Object.class);
		//캐시된 데이터가 null이 아니라면 즉시 반환
		if(Objects.nonNull(cachedValue)) {
			log.info("returns cached data. [{}]", invocation);
			return cachedValue;
		}
		
		//캐시된 데이터 없으면, 대상 객체에 명령을 위임하고, 반환된 값을 캐시에 저장 후 반환 처리
		cachedValue = invocation.proceed();
		cache.put(invocation.getMethod().getName(), cachedValue);
		log.info("caching return value. [{}]", invocation);
		
		return cachedValue;
	}
	
}
