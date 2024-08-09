package zerobase.weather.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import zerobase.weather.domain.Memo;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor // 의존성 생성
public class JdbcMemoRepository {
    private final JdbcTemplate jdbcTemplate;

    public Memo save(Memo memo) {
      String sql = "insert into memo values (?, ?)";
      jdbcTemplate.update(sql, memo.getId(), memo.getText());
      return memo;
    }

    public List<Memo> findAll() {
        String sql = "select * from memo";
        return jdbcTemplate.query(sql, memoRowMapper());

    }

    public Optional<Memo> findById(String id) {
        String sql = "select * from memo where id = ?";
        // 해당하는 id값으 첫번째(중복체크)
        //Optional을 이용하여 null값 체크
        return jdbcTemplate.query(sql, memoRowMapper(), id).stream().findFirst();
    }

    private RowMapper<Memo> memoRowMapper (){
        //resultSet 형태
        return (rs, rowNum) -> new Memo(
                rs.getInt("id"),
                rs.getString("text")
        );
    }


}
