package zerobase.weather.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDate;

//@Data
// entity에서 setter는 일관성 유지의 어려움 때문에 사용 지양 ,
// 1. Builder를 이용하여 일관성 유지 or 사용 의도가 명확한 메서드를 이용

@Getter
@Entity(name="date_weather")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DateWeather {
    @Id
    private LocalDate date;
    private String weather;
    private String icon;
    private double temperature;
}
