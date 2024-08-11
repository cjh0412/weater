package zerobase.weather;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import zerobase.weather.domain.Memo;
import zerobase.weather.repository.JdbcMemoRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
// 테스트 정보가 변경되지 않도록 막는 어노테이션
// 해당 어노테이션이 활성화 되어 있는 경우
// 테스트를 실행하더라도 기존 데이터에 영향 x
// 모든 데이터 rollback 처리
@Transactional
public class JdbcMemoRepositoryTest {

    //테스트 코드의 경우 @RequiredArgsConstructor 사용 불가능 (스프링컨테이너를 사용x, Junit 프레임워크를 사용)
    @Autowired
    JdbcMemoRepository jdbcMemoRepository;

    @Test
    void insertMemoTest(){

        //given
        Memo newMemo = new Memo(2, "insert memo test");

        //when
        jdbcMemoRepository.save(newMemo);

        //then
        Optional<Memo> result = jdbcMemoRepository.findById(String.valueOf(2));
        assertEquals(result.get().getText(), "insert memo test");

    }

    @Test
    void findAllMemosTest(){
        List<Memo> memoList = jdbcMemoRepository.findAll();
        System.out.println(memoList);
        assertNotNull(memoList);
    }

}
