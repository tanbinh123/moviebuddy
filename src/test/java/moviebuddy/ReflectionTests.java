package moviebuddy;

import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;

public class ReflectionTests {
	@Test
	void objectCreateAndMethodCall() throws Exception {
		//리플렉션을 쓰지 않고 흔히 자바에서 쓰는 방법
		Duck duck = new Duck();
		duck.quack();
		
		//리플렉션 api를 활용
		//어떤 클래스인지 알 수 없으니 제네릭 전체 클래스 타입을 받을 수 있도록 하고, 이 클래스를 찾지 못할 수 있기에 ClassNotFoundException 추가 
		Class<?> duckClass = Class.forName("moviebuddy.ReflectionTests$Duck");
		Object duckObject = duckClass.getDeclaredConstructor().newInstance();//해당 클래스로 객체를, 인스턴스를 생성
		Method quackMethod = duckObject.getClass().getDeclaredMethod("quack", new Class<?>[0]);
		quackMethod.invoke(duckObject);
	}
	
	static class Duck {
		
		void quack() {
			System.out.println("꽥꽥!");
		}
	}
}
