package zerobase.weather.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import zerobase.weather.service.DiaryService;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
public class DiaryController {

    private final DiaryService diaryService;

//@RequiredArgsConstructor 대체
//    public DiaryController(DiaryService diaryService) {
//        this.diaryService = diaryService;
//    }
    
    @PostMapping("/create/diary")
    //RequestParam : 요청 데이터
    void createDiary(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                     @RequestBody String text) {

        diaryService.createDiary(date, text);

    }
}
