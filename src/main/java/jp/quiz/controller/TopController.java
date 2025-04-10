package jp.quiz.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * トップページを担当するコントローラークラスです。
 * <p>
 * このクラスは、アプリケーションのトップページを表示するためのリクエストを処理します。
 * デフォルトのルートパスにアクセスすると、トップページ（index.html）に遷移します。
 * </p>
 * 
 * @since 2025-04-10
 * @author shimizu
 */
@Controller
@RequestMapping("/")
public class TopController {

    /**
     * トップページ（index.html）を表示します。
     * <p>
     * このメソッドは、ブラウザがルートURLにアクセスした際に実行され、トップページを表示するために
     * "index" ビューを返します。
     * </p>
     * 
     * @return "index" ビューの名前（resources/templates/index.htmlが対応）
     */
    public String index() {
        return "index"; // resources/templates/index.html
    }

}
