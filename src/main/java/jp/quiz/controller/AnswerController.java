package jp.quiz.controller;

import java.util.Map;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jp.quiz.service.QuizService;
/**
 * AnswerControllerは、クイズのAPI処理をするコントローラーです。
 * 
 * @since 2025-04-10
 * @author shimizu
 */
@RestController
@RequestMapping("/quizzes") 
public class AnswerController {
    @Autowired
    private QuizService quizService;
    
    /**
     * クイズの答えをチェックします。
     * 
     * @param id クイズのID
     * @param choice ユーザーが選んだ選択肢
     * @param choiceNum 選択肢の番号（1〜4）
     * @param session 現在のセッション情報
     * @return ユーザーの選択肢が正しいかどうかを示す結果のマップ
     */
    @PostMapping("/check-answer")
    public Map<String, Object> checkAnswer(@RequestParam String id, @RequestParam String choice, @RequestParam String choiceNum, HttpSession session) {
        //System.out.println("id : " + id);
        Long quizId = Long.parseLong(id);
        return quizService.checkAnswer(quizId, choice, choiceNum, session);
    }
}
