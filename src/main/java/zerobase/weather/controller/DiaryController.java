package zerobase.weather.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import zerobase.weather.domain.Diary;
import zerobase.weather.service.DiaryService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class DiaryController {

    private final DiaryService diaryService;

//@RequiredArgsConstructor 대체
//    public DiaryController(DiaryService diaryService) {
//        this.diaryService = diaryService;
//    }

    @Operation(summary = "swagger 요약설명", description ="상세 설명")
    @PostMapping("/create/diary")
    //RequestParam : 요청 데이터
    void createDiary(@RequestParam(name="date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                     @RequestBody String text) {

        diaryService.createDiary(date, text);

    }

    @Operation(summary = "선택한 날짜의 모든 일기 데이터")
    @GetMapping("/read/diary")
    List<Diary> readDiary(@RequestParam(name="date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return diaryService.readDiary(date);
    }

    @Operation(summary = "선택한 기간 중의 모든 일기 데이터")
    @GetMapping("/read/diaries")
    List<Diary> readDiaries(@RequestParam(name="startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                            @Parameter(name="startDate", description = "조회할 기간의 첫날") LocalDate startDate
                          , @RequestParam(name="endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                            @Parameter(name="endDate", description = "조회할 기간의 마지막날") LocalDate endDate) {
        //startDate 부터 endDate 까지 조회
        return diaryService.readDiaries(startDate, endDate);
    }

    @PutMapping("/update/diary") // 부분수정시 사용
    void updateDairy(@RequestParam(name="date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                      @RequestBody String text){

        diaryService.updateDiary(date, text);

}
    @DeleteMapping("/delete/diary")
    void deleteDiary(@RequestParam(name="date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date){
        diaryService.deleteDiary(date);
    }




}
