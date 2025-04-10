package jp.quiz.controller;

import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import jp.quiz.dto.QuizDTO;
import jp.quiz.entity.Quiz;
import jp.quiz.service.QuizService;

/**
 * QuizControllerは、クイズの管理に関連する操作を処理するコントローラーです。
 * 
 * <p>
 * このコントローラーは、クイズの表示、作成、更新、削除、次のクイズの表示などの操作を担当します。
 * また、クイズの情報はセッションに保持され、ユーザーが進めていくクイズを管理します。
 * </p>
 * 
 * @since 2025-04-10
 * @author shimizu
 */
@Controller
@SessionAttributes({"quizzes", "currentIndex"})
@RequestMapping("/quizzes")
public class QuizController {

    @Autowired
    private QuizService quizService;

    /**
     * クイズ一覧画面を表示する。
     * 初回アクセス時に最初のクイズを表示するため、クイズリストと現在のインデックスをModelにセットする。
     *
     * @param model Modelオブジェクトにクイズリストと現在のインデックスを追加
     * @return "quiz_detail" クイズ詳細画面のビュー名
     */
    @GetMapping
    public String getAllQuizzes(Model model) {
        // クイズリストを取得
        List<QuizDTO> quizzes = quizService.getQuizzesList();

        // 最初のクイズをmodelに保持
        model.addAttribute("quizzes", quizzes);
        model.addAttribute("currentIndex", 0);
        model.addAttribute("quiz", quizzes.get(0));

        // quiz_detailに遷移
        return "quiz_detail";
    }

    /**
     * 新しいクイズを追加するためのフォームを表示する。
     *
     * @param model Modelオブジェクト
     * @return "add" クイズ追加画面のビュー名
     */
    @GetMapping("/add")
    public String createQuiz(Model model) {
        return "add";
    }
    
    /**
     * クイズの一覧を表示する。
     *
     * @param model Modelオブジェクトにクイズのリストをセット
     * @return "quiz_list" クイズ一覧画面のビュー名
     */
    @GetMapping("/list")
    public String showQuiz(Model model) {
        model.addAttribute("quizList", quizService.getAllQuizzes());
        return "quiz_list";
    }

    /**
     * 新しいクイズをデータベースに登録する。
     *
     * @param quiz 追加するクイズ情報
     * @param model モデルに新しいクイズを追加するための準備
     * @return "redirect:/quizzes/list" クイズ一覧画面へのリダイレクト
     */
    @PostMapping("/register")
    public String addQuiz(@ModelAttribute Quiz quiz, Model model) {
        quizService.addQuiz(quiz);
        return "redirect:/quizzes/list";
    }
    
    /**
     * 指定したIDのクイズを編集画面に表示する。
     *
     * @param id 編集するクイズのID
     * @param model モデルに編集するクイズをセット
     * @return "quiz_edit" クイズ編集画面のビュー名
     */
    @GetMapping("/edit/{id}")
    public String editQuiz(@PathVariable Long id, Model model) {
        Quiz quiz = quizService.getQuizById(id);
        model.addAttribute("quiz", quiz);
        return "quiz_edit";
    }
    
    /**
     * クイズの情報を更新する。
     *
     * @param quiz 更新するクイズ情報
     * @return "redirect:/quizzes/list" クイズ一覧画面へのリダイレクト
     */
    @PostMapping("/update")
    public String updateQuiz(@ModelAttribute Quiz quiz) {
        quizService.updateQuiz(quiz);
        return "redirect:/quizzes/list";
    }
    
    /**
     * 指定したIDのクイズを削除する。
     *
     * @param id 削除するクイズのID
     * @param model モデルに必要な情報をセット
     * @return "redirect:/quizzes/list" クイズ一覧画面へのリダイレクト
     */
    @PostMapping("/delete")
    public String deleteQuiz(@RequestParam String id, Model model) {
        quizService.deleteQuizById(Long.parseLong(id));
        return "redirect:/quizzes/list";
    }
    
    /**
     * 次のクイズを表示する。
     * 現在のクイズインデックスを更新し、次のクイズを表示する。
     * 最後のクイズに到達した場合は、クイズ終了画面を表示する。
     *
     * @param model モデルにクイズとインデックス情報をセット
     * @param session 現在のセッション情報
     * @return 次のクイズを表示するビュー名または終了画面のビュー名
     */
    @SuppressWarnings("unchecked")
    @GetMapping("/next")
    public String nextQuiz(Model model, HttpSession session) {
        List<Quiz> quizzes = (List<Quiz>) model.getAttribute("quizzes");
        Integer currentIndex = (Integer) model.getAttribute("currentIndex");

        // 次のクイズがあればその情報を更新
        if (currentIndex < quizzes.size() - 1) {
            currentIndex++;
            model.addAttribute("currentIndex", currentIndex);
            model.addAttribute("quiz", quizzes.get(currentIndex));
            return "quiz_detail";
        } else {
            // クイズ終了時の処理
            List<Map<String, Object>> answerHistory = (List<Map<String, Object>>) session.getAttribute("answerHistory");

            if (quizzes.size() != answerHistory.size()) {
                throw new RuntimeException("クイズ履歴と解答数が一致しません");
            }

            // 終了画面の準備
            model.addAttribute("answerHistory", answerHistory);
            model.addAttribute("score", quizService.getScore(session));

            // セッションを破棄
            session.invalidate();
            return "quiz_end";
        }
    }

}
