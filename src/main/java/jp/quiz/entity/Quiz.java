package jp.quiz.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * クイズの情報を管理するエンティティクラスです。
 * <p>
 * このクラスはデータベースの「quizzes」テーブルと対応しており、クイズの問題、選択肢、答え、
 * 作成日時、更新日時を保持します。HibernateによるORM（Object-Relational Mapping）を利用して、
 * クイズデータの永続化を行います。
 * </p>
 * 
 * @since 2025-04-10
 * @author shimizu
 */
@Entity
@Table(name = "quizzes")
@Data // ゲッター、セッター、toString、equals、hashCodeを生成
@NoArgsConstructor // 引数なしのコンストラクタを生成
@AllArgsConstructor // 全引数コンストラクタを生成
public class Quiz {

    /**
     * クイズのID。データベースの主キーとして使用されます。
     * 
     * @see javax.persistence.Id
     */
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "quiz_id_seq")
    @SequenceGenerator(name = "quiz_id_seq", sequenceName = "quiz_id_seq", allocationSize = 1)
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
    
    /**
     * クイズの作成日時。レコードが作成されると自動的に設定されます。
     * Hibernateの{@link CreationTimestamp}アノテーションを使用しています。
     */
    @CreationTimestamp
    @Column(name = "created_at", updatable = false, insertable = true)
    private LocalDateTime createdAt;

    /**
     * クイズの更新日時。レコードが更新されると自動的に設定されます。
     * Hibernateの{@link UpdateTimestamp}アノテーションを使用しています。
     */
    @UpdateTimestamp
    @Column(name = "updated_at", insertable = false, updatable = true)
    private LocalDateTime updatedAt;
}
