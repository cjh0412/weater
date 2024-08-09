package zerobase.weather.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity //클래스 명과 테이블 명이 다른 경우 설정(name="memo")
//@Table(name="테이블명") 2개 이상의 entity를 1개의 테이블에 연결할때 사용
public class Memo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // id값 자동 생성(IDENTITY: 데이터 베이스 기준)
    private int id;
    private String text;

}
