package jp.quiz.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import jp.quiz.entity.Quiz;

/**
 * クイズデータの操作を行うリポジトリインターフェースです。
 * <p>
 * このインターフェースは、`JpaRepository` を継承し、クイズに関するデータの保存、更新、削除、取得を行うための
 * メソッドを提供します。`Quiz` エンティティに対応するデータベースのテーブル操作を行います。
 * </p>
 * 
 * @since 2025-04-10
 * @author shimizu
 */
public interface QuizRepository extends JpaRepository<Quiz, Long> {

    /**
     * ランダムにクイズを10件取得します。
     * <p>
     * このメソッドは、クイズテーブルからランダムに10件のクイズを取得するためのクエリを実行します。
     * </p>
     * 
     * @return ランダムに選ばれたクイズのリスト
     */
    @Query(value = "SELECT * FROM quizzes ORDER BY RANDOM() LIMIT 5", nativeQuery = true)
    List<Quiz> findRandom10Quizzes();
}
