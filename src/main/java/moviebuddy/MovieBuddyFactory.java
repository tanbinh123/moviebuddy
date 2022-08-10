package moviebuddy;
// 객체를 생성하고 구성하는 역할

import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.aopalliance.aop.Advice;
import org.springframework.aop.Advisor;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import moviebuddy.cache.CachingAdvice;
import moviebuddy.data.AbstractMetadataResourceMovieReader;
import moviebuddy.data.CachingMovieReader;
import moviebuddy.data.CsvMovieReader;
import moviebuddy.data.XmlMovieReader;
import moviebuddy.domain.Movie;
import moviebuddy.domain.MovieFinder;
import moviebuddy.domain.MovieReader;

//@Import은 다른 클래스에서 빈 구성 정보를 불러오기 위해서 사용하는 것이다.
//XML을 불러오고 싶으면, @ImportResource("xml file location") 지정된 곳의 XML파일을 읽어서 불러올 수 있다.
@Configuration
@PropertySource("/application.properties") //application.properties를 클래스로 부터 찾아라
@ComponentScan
//@ComponentScan(basePackages = { "moviebuddy" }) //패키지를 지정하고 싶을때
@Import({ MovieBuddyFactory.DomainModuleConfig.class, MovieBuddyFactory.DataSourceModuleConfig.class })
public class MovieBuddyFactory {
	//새로운 빈 등록 / Jaxb2Marshaller가 마샬과 언마샬 인터페이스를 모두 구현하고 있기 때문에 언마샬도 가능해서 사용한다. 
	@Bean
	public Jaxb2Marshaller jaxb2Marshaller() {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setPackagesToScan("moviebuddy");
		
		return marshaller;
	}
	
	//CaffeineCacheManager 직접 넣는 방식이 아니라 빈으로 등록하기
	@Bean
	public CacheManager CaffeineCacheManager() {
		CaffeineCacheManager casCacheManager = new CaffeineCacheManager();
		casCacheManager.setCaffeine(Caffeine.newBuilder().expireAfterWrite(3, TimeUnit.SECONDS));
		
		return casCacheManager;
	}
	
	//자동 프락시 생성기 
	@Bean
	public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
		return new DefaultAdvisorAutoProxyCreator();
	}
	
	@Bean
	public Advisor cachingAdvisor(CacheManager cacheManager) {
		Advice advice = new CachingAdvice(cacheManager);
		
		//Advisor = PointCut(대상 선정 알고리즘)과 Advice(부가기능)
		return new DefaultPointcutAdvisor(advice);
	}
	
	
	//아래의 2개 클래스는 빈 구성 정보로 사용할 거기 때문에 @Configuration 을 붙인다.
	@Configuration
	static class DomainModuleConfig {
		//자바 코드로 의존 관계 주입하는 방법으로 2번째 메소드 파라미터로 받는 방식
		/*
		@Bean
		//@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE) //@Scope("prototype") //동일한 뜻이다.
		public MovieFinder movieFinder(MovieReader movieReader) {
			return new MovieFinder(movieReader);
		}
		*/
	}
	
	@Configuration
	static class DataSourceModuleConfig {
		/*MovieBuddyFactory에서 수동으로 빈을 등록할 필요가 없어서 제거
		//스프링의 컨테이너의 도움을 받아서 의존 관계 주입을 받는다.
		//private final Environment environment;
		
		//@Autowired 가 생략되어 있다고 보면 된다.
		//public DataSourceModuleConfig(Environment environment) {
		//	this.environment = environment;
		//}
		
		//ComponentScan을 하기 위해 제거 
		//@Bean
		//public MovieReader movieReader() {
		//	return new CsvMovieReader();
		//}
		
		//주입 방식중 1번째 메서드 콜 방식 
		//@Bean
		//public MovieFinder movieFinder() {
		//	return new MovieFinder(new CsvMovieReader());
		//}
		
		//환경에 따라서 빈이 등록이 되거나 되지 않을 수 있기 때문에 Profile 애노테이션을 통해서 CsvMovieReader을 등록
		@Profile(MovieBuddyProfile.CSV_MODE)
		@Bean
		public CsvMovieReader csvMovieReader() {
			CsvMovieReader movieReader = new CsvMovieReader();
			
			//애플리케이션 외부에서 작성된 설정정보를 읽어, 메타데이터 위치 설정하기
			//시스템의 OS나 JVM 아규먼트와 같은 설정 정보를 읽어 들이기 위해서 자바에서는 시스템 클래스에
			//getProperty라는 메소드를 활용할 수 있습니다.
			
			//movieReader.setMetadata("movie_metadata.csv");
			//movieReader.setMetadata(System.getProperty("movie.metadata"));
			//스프링 컨테이너인 Value 애노테이션으로 AbstractFileSystemMovieReader 주입받고 있기 때문에 주석처리 
			//movieReader.setMetadata(environment.getProperty("movie.metadata"));
			return movieReader;
		}
		
		@Profile(MovieBuddyProfile.XML_MODE)
		@Bean
		public XmlMovieReader xmlMovieReader(Unmarshaller unmarshaller) {
			XmlMovieReader movieReader = new XmlMovieReader(unmarshaller);
			//movieReader.setMetadata("movie_metadata.xml");
			//movieReader.setMetadata(System.getProperty("movie.metadata"));
			//스프링 컨테이너인 Value 애노테이션으로 AbstractFileSystemMovieReader 주입받고 있기 때문에 주석처리 
			//movieReader.setMetadata(environment.getProperty("movie.metadata"));
			
			return movieReader;
		}
		*/
		
		/*
		@Bean
		public CsvMovieReader csvMovieReader() {
			CacheManager cacheManager = new CaffeineCacheManager();
			
			//일정시간 유지하고 빠져나가도록, 3초 유지
			Cache<String, List<Movie>> cache = Caffeine.newBuilder()
					.expireAfterWrite(3, TimeUnit.SECONDS)
					.build();
			
			//return new CsvMovieReader(cache);
			return new CsvMovieReader(cacheManager);
		}
		*/
		
		/*이렇게 직접 등록하지 않아도 이제 캐시라는 부가 기능을 어드바이저에 담아서 DefaultAdvisorAutoProxyCreator 프락시를 자동으로 구성해서 스프링 컨테이너에 등록해 줄 것이기 때문에
		//현재 프로파일에 의해서 CSV 모드 또는 XML 모드가 지정이 되면 그것에 걸맍게 생성이 된다.
		//@Primary : 두개 이상의 동일한 타입의 빈이 존재할때 이 빈을 선호 하겠다라는 뜻
		@Primary
		@Bean
		//public MovieReader cachingMovieReader(CacheManager cacheManager, MovieReader target) {
		public ProxyFactoryBean cachingMovieReaderFactory(ApplicationContext applicationContext) {
			MovieReader target = applicationContext.getBean(MovieReader.class);
			CacheManager cacheManager = applicationContext.getBean(CacheManager.class);
			
			//생성된 MovieReader 객체는 의존 관계 주입을 받아서 대상(target) 객체로써 사용 될 것이다.
			//return new CachingMovieReader(cacheManager, target);
			ProxyFactoryBean proxyFactoryBean = new ProxyFactoryBean();
			proxyFactoryBean.setTarget(target);
			//클래스 프락시 활성화(true)/비활성화(false, 기본값)
			//proxyFactoryBean.setProxyTargetClass(true);
			proxyFactoryBean.addAdvice(new CachingAdvice(cacheManager));
			
			return proxyFactoryBean;
		}
		*/
	}
}