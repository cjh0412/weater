package zerobase.weather.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import zerobase.weather.domain.Diary;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DiaryRepository extends JpaRepository<Diary, Integer> {

    //date값을 기준으로 데이터 가져옴
    List<Diary> findAllByDate(LocalDate date);

    //startDate부터 endDate까지의 값
    List<Diary> findAllByDateBetween(LocalDate startDate, LocalDate endDate);

    // 동일 날짜의 일기중 첫번째 데이터를 불러옴
    Diary getFirstByDate(LocalDate date);

    @Transactional
    //해당 날짜의 전체 데이터 삭제
    void deleteAllByDate(LocalDate date);

}
