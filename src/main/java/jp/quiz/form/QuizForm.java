package jp.quiz.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * クイズの情報を保持するクラス。
 * <p>
 * このクラスは、クイズの問題、選択肢、正解、解説などの情報を保持し、データベースとのやり取りで使用されます。
 * Lombokアノテーションを使用して、ボイラープレートコード（ゲッター、セッター、コンストラクタなど）を自動生成しています。
 * </p>
 * 
 * @since 2025-04-10
 * @author shimizu
 */
@Data // ゲッター、セッター、toString、equals、hashCodeを生成
@NoArgsConstructor // 引数なしのコンストラクタを生成
@AllArgsConstructor // 全引数コンストラクタを生成
public class QuizForm {

    /**
     * クイズのID。データベースの主キーとして使用されます。
     */
    private Long id;

    /**
     * クイズの質問内容。
     */
    private String question;
    
    /**
     * クイズの選択肢1。
     */
    private String choice1;
    
    /**
     * クイズの選択肢2。
     */
    private String choice2;
    
    /**
     * クイズの選択肢3。
     */
    private String choice3;
    
    /**
     * クイズの選択肢4。
     */
    private String choice4;
    
    /**
     * クイズの正解の選択肢番号。
     */
    private int answer;
    
    /**
     * クイズの解説。
     */
    private String description;
    
}
