package zerobase.weather.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;

@Service
public class DiaryService {

    @Value("${openweather.key}") // 지정된 변수에있는 값을 가져옴
    private String apiKey;

    public void createDiary(LocalDate date, String text) {
        //openWeatherMap에서 날씨 데이터 가져오기
        System.out.println(getWeatherString());
        
        //날씨 json 파싱하기
        
        //파싱된 데이터 + 일기 값 db 저장하기

    }

    private String getWeatherString(){
        String apiUrl = "ttps://api.openweathermap.org/data/2.5/weather?q=seoul&appid=" + apiKey;

        try {
            URL url = new URL(apiUrl);
            // HTTP 형식으로 호출
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            int responseCode = con.getResponseCode();
            BufferedReader br;
            if (responseCode == 200) { // 정상 호출인경우
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            }else {
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                
            }
            String inputLine;
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
}
