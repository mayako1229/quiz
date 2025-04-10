package jp.quiz.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * QuizDTOは、クイズ情報を表現するデータ転送オブジェクト（DTO）です。
 * 
 * <p>
 * クイズのID、質問内容、選択肢、正解の番号、解説などを保持します。クライアントとサーバー間で
 * クイズデータをやり取りする際に使用されます。
 * </p>
 * 
 * @since 2025-04-10
 * @author shimizu
 */
@Data // getter/setter, toString, equals, hashCode を自動生成
@NoArgsConstructor // 引数なしコンストラクタ
@AllArgsConstructor // 全フィールドを引数に持つコンストラクタ
public class QuizDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * クイズのID。
     */
    private Long id;
    
    /**
     * クイズの質問内容。
     */
    private String question;
    
    /**
     * クイズの選択肢リスト。
     * 各選択肢はマップで、キーが選択肢番号、値が選択肢の文字列。
     */
    private List<Map<Integer, String>> choices;
    
    /**
     * 正解の選択肢番号。
     */
    private int answer;
    
    /**
     * クイズの解説。
     */
    private String description;
}
