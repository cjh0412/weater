package zerobase.weather;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import zerobase.weather.domain.Memo;
import zerobase.weather.repository.JpaMemoRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
public class JpaMemoRepositoryTest {

    @Autowired
    JpaMemoRepository jpaMemoRepository;

    @Test
    void insertMemoTest(){
        //given
        Memo newMemo = new Memo(10, "this is jpa memo");

        //when
        jpaMemoRepository.save(newMemo);

        //then
        List<Memo> memoList = jpaMemoRepository.findAll();
        assertTrue(memoList.size() > 0);
    }

    @Test
    void findByIdTest(){
        //given
        Memo newMemo = new Memo(11, "this is jpa memo");

        //when
        Memo memo = jpaMemoRepository.save(newMemo);

        //then
        //Optional<Memo> result = jpaMemoRepository.findById(11); // 데이터 베이스 기준으로 id값 생성, 직접 넣은 아이디값x
        Optional<Memo> result = jpaMemoRepository.findById(memo.getId()); // db에 저장된 id로 조회
        assertEquals(result.get().getText(), "this is jpa memo");
    }
}
