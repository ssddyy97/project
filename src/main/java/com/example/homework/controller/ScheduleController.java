package com.example.homework.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/schedule")
public class ScheduleController {

    @GetMapping
    public String getSchedule(Model model) {
        Map<String, List<String>> podcastSchedule = new LinkedHashMap<>();

        podcastSchedule.put("[2개월 전]", Arrays.asList(
                "소담소담 시즌1 코멘터리 (45:24)",
                "그동안 감사했습니다 (43:43)",
                "또X4 도쿄 맛집 리스트! 근데 마멜의 내 토나내(+사진들) 결론임. (25:16)",
                "'힘오 사패멘스' (39:36)",
                "소담소담 범죄 게임 [퀴지 마인드] (15:19)",
                "고도로 발전한 경경이는 멋쟁이와 구분할 수 없다 (51:54)",
                "또또또 돌아온 도쿄 여행~ 이번엔 추천! 여행지! [소담소담] (20:28)",
                "중요할건 적어도 후회하는 마음 [소담소담 후커페스트] (39:36)",
                "[소담소담] 엑디 & 마멜의 플레이리스트 (22:18)"
        ));

        podcastSchedule.put("[3개월 전]", Arrays.asList(
                "[소담소담 후커페스트] 학창한 친구끼리? (41:08)",
                "[소담소담] 일본 현지인 도쿄 여행/맛집 (2부 신주쿠편) (18:02)",
                "[소담소담 후커페스트] 같이 있어도 외로운 우리 (46:33)",
                "[소담소담] 범죄스게임 #1: 모드 대화를 들려줄까? VS 모든 대화를 들으려고… (16:59)",
                "[소담소담 후커페스트] 소담소담이 친해진 그날 (46:33)",
                "[소담소담 엑디] 5분 심리학 - 브루잉 효과 (12:46)"
        ));

        podcastSchedule.put("[4개월 전]", Arrays.asList(
                "[소담소담 후커페스트] 공부하기 싫죠? (저희도여) (46:16)",
                "[소담소담] 일본 현지인 도쿄 여행/맛집 리스트 (1화) (21:54)",
                "[소담소담 후커페스트] 생각이 많은 당신! 예제 (44:34)",
                "[소담소담] 여행하다 추억이다 (8:08)",
                "[소담소담 후커페스트] 예쁘게 말하는 법 ✨ (58:40)",
                "[소담소담] 아 제육 뭐로 해야 되냐 (8:55)",
                "[소담소담 후커페스트] 자존감이 떨어진 당신에게 (44:27)",
                "[소담소담] 오프 더 레코드 (46:33)",
                "[소담소담 후커페스트] 하고 싶은 말 (1:02:49)"
        ));

        model.addAttribute("podcastSchedule", podcastSchedule);
        return "schedule";
    }
}
