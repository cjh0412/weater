package zerobase.weather.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
//@Data
// entity에서 setter는 일관성 유지의 어려움 때문에 사용 지양 ,
// 1. Builder를 이용하여 일관성 유지 or 사용 의도가 명확한 메서드를 이용
public class Diary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String weather;
    private String icon;
    private double temperature;
    private String text;
    private LocalDate date;

//    사용 의도가 명확한 메서드를 이용 , 입력값 text, 파싱 데이터 저장
    public void setDateWeather(DateWeather dateWeather, String text) {
        this.date = dateWeather.getDate();
        this.weather = dateWeather.getWeather();
        this.icon = dateWeather.getIcon();
        this.temperature = dateWeather.getTemperature();
        this.text = text;
    }




}
