package zerobase.weather;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class WeatherApplicationTests {

	@Test
	void equalTest(){
		assertEquals(1,1); // a,b가 일치하는 객체임을 확인
	}

	@Test
	void nullTest(){
		// a가 null인지 확인
//		assertNull(1);
		assertNull(null);
	}

	@Test
	void TrueTest(){
		// a가 참인지 확인
		assertTrue(1==1);
	}
}
