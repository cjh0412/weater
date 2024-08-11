package zerobase.weather.service;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import zerobase.weather.domain.Diary;
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

    // 지정된 변수에있는 값을 가져옴
    // application.properties 설정
    @Value("${openweather.key}") 
    private String apiKey;

    public DiaryService(DiaryRepository diaryRepository) {
        this.diaryRepository = diaryRepository;
    }

    public void createDiary(LocalDate date, String text) {
        //openWeatherMap에서 날씨 데이터 가져오기
        String weatherData = getWeatherString();
        
        //날씨 json 파싱하기
        Map<String, Object> parseWeather = parseWeather(weatherData);

        //파싱된 데이터 + 일기 값 db 저장하기
        Diary nowDiary = new Diary();
        nowDiary.setWeather(parseWeather.get("main").toString());
        nowDiary.setIcon(parseWeather.get("icon").toString());
        nowDiary.setTemperature((Double) parseWeather.get("temp"));
        nowDiary.setText(text);
        nowDiary.setDate(date);

        diaryRepository.save(nowDiary);
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
    //읽기만 가능
    @Transactional(readOnly = true)
    public List<Diary> readDiary(LocalDate date) {
        return diaryRepository.findAllByDate(date);
    }

    //읽기만 가능
    @Transactional(readOnly = true)
    public List<Diary> readDiaries(LocalDate startDate, LocalDate endDate) {
        return diaryRepository.findAllByDateBetween(startDate, endDate);
    }

    public void updateDiary(LocalDate date, String text) {
        Diary nowDiary = diaryRepository.getFirstByDate(date);
        //일기값만 수정
        nowDiary.setText(text);
        diaryRepository.save(nowDiary);
    }

    public void deleteDiary(LocalDate date) {
        diaryRepository.deleteAllByDate(date);
    }
}
