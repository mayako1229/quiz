# Java 17 がインストールされたイメージを使用
FROM eclipse-temurin:17-jre

# 作業ディレクトリを作成
WORKDIR /app

# jar ファイルをコンテナにコピー
COPY target/quiz-0.0.1-SNAPSHOT.jar app.jar

# ポート番号（例：8080）を公開
EXPOSE 8080

# アプリの起動
ENTRYPOINT ["java", "-jar", "app.jar"]
