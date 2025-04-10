package jp.quiz.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import jp.quiz.dto.QuizDTO;
import jp.quiz.entity.Quiz;
import jp.quiz.repository.QuizRepository;

/**
 * QuizServiceは、クイズに関するビジネスロジックを提供するサービスクラスです。
 * 
 * <p>
 * クイズの登録、更新、削除、取得、ランダムなクイズの取得、解答のチェックなど、クイズに関連する
 * 機能を提供します。また、解答履歴の管理やスコアの計算も行います。
 * </p>
 * 
 * @since 2025-04-10
 * @author shimizu
 */
@Service
public class QuizService {

    @Autowired
    private QuizRepository quizRepository;

    /**
     * データベースから全てのクイズをID順に昇順で取得する。
     *
     * @return クイズのリスト
     */
    public List<Quiz> getAllQuizzes() {
        return quizRepository.findAll(Sort.by(Sort.Order.asc("id")));
    }

    /**
     * ランダムに10問のクイズを取得し、DTOに変換して返す。
     *
     * @return ランダムに選ばれた10問のクイズDTOリスト
     */
    public List<QuizDTO> getQuizzesList() {
        List<Quiz> list = findRandom10Quizzes();
        QuizDTO dto = null;
        List<QuizDTO> qizzesList = new ArrayList<>();
        List<Map<Integer, String>> choises;

        // クイズをDTOに変換
        for (Quiz quiz : list) {
            dto = new QuizDTO();
            dto.setId(quiz.getId());
            dto.setQuestion(quiz.getQuestion().replace("\\n", "<br>"));
            choises = getShuffledChoices(quiz.getChoice1(), quiz.getChoice2(), quiz.getChoice3(), quiz.getChoice4());
            dto.setChoices(choises);
            dto.setAnswer(quiz.getAnswer());
            dto.setDescription(quiz.getDescription());
            qizzesList.add(dto);
        }

        return qizzesList;
    }

    /**
     * IDに基づいてクイズを取得する。
     *
     * @param id 取得するクイズのID
     * @return 指定されたIDのクイズ
     */
    public Quiz getQuizById(Long id) {
        return quizRepository.findById(id).orElse(null);
    }

    /**
     * 新しいクイズをデータベースに保存する。
     *
     * @param quiz 登録するクイズ
     */
    public void addQuiz(Quiz quiz) {
        quizRepository.save(quiz);
    }

    /**
     * 既存のクイズをデータベースで更新する。
     *
     * @param quiz 更新するクイズ
     */
    public void updateQuiz(Quiz quiz) {
        quizRepository.save(quiz); // IDがあればUPDATEされます
    }

    /**
     * 指定されたIDのクイズを削除する。
     *
     * @param id 削除するクイズのID
     */
    public void deleteQuizById(Long id) {
        quizRepository.deleteById(id); // IDに基づいて削除
    }

    /**
     * ランダムに10問のクイズを取得する。
     *
     * @return ランダムに選ばれた10問のクイズ
     */
    public List<Quiz> findRandom10Quizzes() {
        return quizRepository.findRandom10Quizzes();
    }

    /**
     * 解答履歴に基づいてユーザーのスコアを計算する。
     *
     * @param session 現在のセッション
     * @return 合計点
     */
    @SuppressWarnings("unchecked")
    public int getScore(HttpSession session) {
        List<Map<String, Object>> answerHistory = (List<Map<String, Object>>) session.getAttribute("answerHistory");
        if (answerHistory == null) {
            return -1;
        }
        return answerHistory.stream()
            .filter(m -> (boolean) m.get("correct"))
            .mapToInt(m -> 10)
            .sum();
    }

    /**
     * ユーザーの選んだ選択肢が正解かどうかをチェックし、結果を返す。
     *
     * @param quizId クイズのID
     * @param choice ユーザーが選んだ選択肢
     * @param choiceNum ユーザーが選んだ選択肢番号
     * @param session 現在のセッション
     * @return 解答結果を含むマップ
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> checkAnswer(Long quizId, String choice, String choiceNum, HttpSession session) {
        Quiz quiz = quizRepository.findById(quizId).orElseThrow(() -> new RuntimeException("Quiz not found"));
        List<QuizDTO> quizzes = (List<QuizDTO>) session.getAttribute("quizzes");
        int currentIndex = (Integer) session.getAttribute("currentIndex");
        QuizDTO quizDto = quizzes.get(currentIndex);
        List<Map<Integer, String>> choiceMap = quizDto.getChoices();

        String correctAnswer = Integer.toString(quiz.getAnswer());
        String choiceStr = getChoiceByNumber(quiz, choice);
        String correctAnswerStr = getChoiceByNumber(quiz, correctAnswer);
        String description = quiz.getDescription();
        int index = findChoiceIndex(choiceMap, quiz.getAnswer());

        Map<String, Object> response = createResponse(choiceNum, choiceStr, correctAnswerStr, choice, correctAnswer, index, description);
        updateAnswerHistory(session, currentIndex + 1, choiceNum, choiceStr, correctAnswerStr, choice.equals(correctAnswer), index);

        return response;
    }

    /**
     * 正解が何番目の選択肢かを取得する。
     *
     * @param choiceMap 選択肢のリスト
     * @param answer 正解の番号
     * @return 正解のインデックス（1始まり）
     */
    private int findChoiceIndex(List<Map<Integer, String>> choiceMap, int answer) {
        for (int i = 0; i < choiceMap.size(); i++) {
            if (choiceMap.get(i).containsKey(answer)) {
                return i + 1;
            }
        }
        return -1;
    }

    /**
     * 解答結果をマップにまとめる。
     *
     * @param choiceNum ユーザーが選んだ選択肢番号
     * @param choiceStr ユーザーが選んだ選択肢の文字列
     * @param correctAnswerStr 正解の選択肢の文字列
     * @param choice ユーザーが選んだ選択肢
     * @param correctAnswer 正解の選択肢
     * @param index 正解の選択肢番号
     * @param description クイズの解説
     * @return 解答結果のマップ
     */
    private Map<String, Object> createResponse(String choiceNum, String choiceStr, String correctAnswerStr, String choice, String correctAnswer, int index, String description) {
        Map<String, Object> response = new HashMap<>();
        response.put("yourAnswer", choiceNum + " : " + choiceStr);
        response.put("correctAnswer", index + " : " + correctAnswerStr);
        response.put("correct", choice.equals(correctAnswer));
        response.put("description", description);
        return response;
    }

    /**
     * セッションの回答履歴を更新する。
     *
     * @param session 現在のセッション
     * @param currentIndex 現在のクイズのインデックス
     * @param choiceNum ユーザーが選んだ選択肢番号
     * @param choiceStr ユーザーが選んだ選択肢の文字列
     * @param correctAnswerStr 正解の選択肢の文字列
     * @param isCorrect ユーザーの選択が正解かどうか
     * @param index 正解の選択肢番号
     */
    private void updateAnswerHistory(HttpSession session, int currentIndex, String choiceNum, String choiceStr, String correctAnswerStr, boolean isCorrect, int index) {
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> answerHistory = (List<Map<String, Object>>) session.getAttribute("answerHistory");
        if (answerHistory == null) {
            answerHistory = new ArrayList<>();
        }

        Map<String, Object> answer = new HashMap<>();
        answer.put("index", currentIndex);
        answer.put("yourAnswer", choiceNum + " : " + choiceStr);
        answer.put("correctAnswer", index + " : " + correctAnswerStr);
        answer.put("correct", isCorrect);
        answerHistory.add(answer);

        session.setAttribute("answerHistory", answerHistory);
    }

    /**
     * 選択肢をランダムに並べ替える。
     *
     * @param choice1 選択肢1
     * @param choice2 選択肢2
     * @param choice3 選択肢3
     * @param choice4 選択肢4
     * @return シャッフルされた選択肢のリスト
     */
    private List<Map<Integer, String>> getShuffledChoices(String choice1, String choice2, String choice3, String choice4) {
        List<Map<Integer, String>> shuffledChoices = new ArrayList<>();
        shuffledChoices.add(Map.of(1, choice1));
        shuffledChoices.add(Map.of(2, choice2));
        shuffledChoices.add(Map.of(3, choice3));
        shuffledChoices.add(Map.of(4, choice4));
        Collections.shuffle(shuffledChoices);
        return shuffledChoices;
    }

    /**
     * 選択肢番号から選択肢の文字列を返す。
     *
     * @param quiz クイズ
     * @param choiceNumber 選択肢番号
     * @return 選択肢の文字列
     */
    private String getChoiceByNumber(Quiz quiz, String choiceNumber) {
        switch (choiceNumber) {
            case "1": return quiz.getChoice1();
            case "2": return quiz.getChoice2();
            case "3": return quiz.getChoice3();
            case "4": return quiz.getChoice4();
            default: return null;
        }
    }
}
