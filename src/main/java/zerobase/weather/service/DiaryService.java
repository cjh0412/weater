package zerobase.weather.service;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zerobase.weather.WeatherApplication;
import zerobase.weather.domain.DateWeather;
import zerobase.weather.domain.Diary;
import zerobase.weather.error.InvalidDate;
import zerobase.weather.repository.DateWeatherRepository;
import zerobase.weather.repository.DiaryRepository;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class DiaryService {

    private final DiaryRepository diaryRepository;
    private final DateWeatherRepository dateWeatherRepository;
    private static final Logger logger = LoggerFactory.getLogger(WeatherApplication.class);

    public DiaryService(DiaryRepository diaryRepository, DateWeatherRepository dateWeatherRepository) {
        this.diaryRepository = diaryRepository;
        this.dateWeatherRepository = dateWeatherRepository;
    }


    @Transactional
    @Scheduled(cron = "0 0 1 * * *" ) // (초 분 시간 일 월 요일)
    // 매일 새벽 1시에 실행
    public void saveWeatherDate(){
        dateWeatherRepository.save(getWeatherFromApi());
    };

    // 지정된 변수에있는 값을 가져옴
    // application.properties 설정
    @Value("${openweather.key}") 
    private String apiKey;


    public void createDiary(LocalDate date, String text) {
        logger.info("started to create diary");
        //openWeatherMap에서 날씨 데이터 가져오기 (api에서 파싱하기)
//        String weatherData = getWeatherString();
//        //날씨 json 파싱하기
//        Map<String, Object> parseWeather = parseWeather(weatherData);

        //날씨 데이터 가져오기(DB or API)
        DateWeather dateWeather = getDateWeather(date);

        //파싱된 데이터 + 일기 값 db 저장하기
//
//        nowDiary.setDateWeather(dateWeather);
//        nowDiary.setText(text);

//        nowDiary.setWeather(parseWeather.get("main").toString());
//        nowDiary.setIcon(parseWeather.get("icon").toString());
//        nowDiary.setTemperature((Double) parseWeather.get("temp"));
//        nowDiary.setDate(date);

        Diary nowDiary = new Diary();
        nowDiary.setDateWeather(dateWeather, text);
        diaryRepository.save(nowDiary);

        System.out.println(diaryRepository.save(nowDiary));

        logger.info("finished to create diary");
    }

    private String getWeatherString(){
        //openweaterapi url 정보
        String apiUrl = "https://api.openweathermap.org/data/2.5/weather?q=seoul&appid=" + apiKey;

        try {
            URL url = new URL(apiUrl);
            // HTTP 형식으로 호출
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            //전송방식 정의
            con.setRequestMethod("GET");

            int responseCode = con.getResponseCode();
            // 입력 스트림 문자를 읽는 함수(입력값을 buffer에 저장후 한꺼번에 전송)
            BufferedReader br;
            if (responseCode == 200) { // 정상 호출인경우
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            }else {
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                
            }
            String inputLine;
            //문자열을 추가하거나 변경할때 사용
            StringBuffer content = new StringBuffer();
            while ((inputLine = br.readLine()) != null) {
                content.append(inputLine); // 결과값 추가
            }

            br.close();

            return content.toString();
        }catch (Exception e){
            return "fail";
        }

    }

    private Map<String, Object> parseWeather(String jsonString){
        JSONParser jsonParser = new JSONParser(); // json값 파싱
        JSONObject jsonObject;

        try {
            // 파싱한 데이터를 jsonObject 객체로 변환
            jsonObject = (JSONObject) jsonParser.parse(jsonString);
        }catch (ParseException e){
            throw new RuntimeException(e);
        }

        Map<String, Object> resultMap = new HashMap<>();
        // jsonObject가 가지고 있는 특정 멤버(main)을 저장
        JSONObject mainData = (JSONObject) jsonObject.get("main");
        // hashMap에 추가
        resultMap.put("temp", mainData.get("temp"));

        // 파싱한 데이터가 중괄호'{}' 형식인 경우 jsonObject, 대괄호'[]'인경우 jsonArray 로변환
        //변환한 데이터를 JSONObject로 변환
        JSONArray weatherArray = (JSONArray) jsonObject.get("weather");
        JSONObject weatherData = (JSONObject) weatherArray.get(0); // 리스트안 객체가 1개 뿐이므로 0번째 값 가져옴
        resultMap.put("main", weatherData.get("main"));
        resultMap.put("icon", weatherData.get("icon"));

        return resultMap;
    }

    private DateWeather getDateWeather(LocalDate date){
        List<DateWeather> dateWeatherListFromDb = dateWeatherRepository.findAllByDate(date);
        if(dateWeatherListFromDb.size() == 0){ // db에 존재하지 않을 경우
            //api에서 파싱
            //과거의 데이터를 가져오려고 할 경우, 현재 날씨를 가져오도록 하거나, 날씨없이 일기를 쓰도록 설정
            return getWeatherFromApi();
        }else {
            return dateWeatherListFromDb.get(0);
        }
    }

    private DateWeather getWeatherFromApi(){
        //openWeatherMap에서 날씨 데이터 가져오기
        String weatherData = getWeatherString();

        //날씨 json 파싱하기
        Map<String, Object> parseWeather = parseWeather(weatherData);
// setter 제거후 builder 사용
//        DateWeather dateWeather = new DateWeather();
//        dateWeather.setDate(LocalDate.now());
//        dateWeather.setWeather(parseWeather.get("main").toString());
//        dateWeather.setIcon(parseWeather.get("icon").toString());
//        dateWeather.setTemperature((Double) parseWeather.get("temp"));

        DateWeather dateWeather = DateWeather
                .builder()
                .date(LocalDate.now())
                .weather(parseWeather.get("main").toString())
                .icon(parseWeather.get("icon").toString())
                .temperature((Double) parseWeather.get("temp"))
                .build();

        return dateWeather;
    }


    //읽기만 가능
    @Transactional(readOnly = true)
    public List<Diary> readDiary(LocalDate date) {
        logger.debug("reading diary");
//        if(date.isAfter(LocalDate.ofYearDay(3050,1))){
//            throw new InvalidDate();
//        }

        return diaryRepository.findAllByDate(date);
    }

    //읽기만 가능
    @Transactional(readOnly = true)
    public List<Diary> readDiaries(LocalDate startDate, LocalDate endDate) {
        return diaryRepository.findAllByDateBetween(startDate, endDate);
    }

    public void updateDiary(LocalDate date, String text) {
        // setter 제거
        //동일 날짜의 일기가 존재할 경우 첫번째 일기 데이터 가져옴
        Diary nowDiary = diaryRepository.getFirstByDate(date);
        //일기값만 수정
//        nowDiary.setText(text);
        Diary saveDiary = Diary.builder()
                .id(nowDiary.getId())
                .text(text)
                .date(nowDiary.getDate())
                .weather(nowDiary.getWeather())
                .temperature(nowDiary.getTemperature())
                .icon(nowDiary.getIcon())
                .build();

        diaryRepository.save(saveDiary);
    }

    public void deleteDiary(LocalDate date) {
        diaryRepository.deleteAllByDate(date);
    }
}
