# MeetingService

らくらくビデオチャットサービス

- wsappにデプロイしたサービスは [こちら](https://es4.eedept.kobe-u.ac.jp/video_chat_service/)。

### 本番環境(仮想tomcat)

実行サーバーは仮想で、データベースは本番環境。

1. application.propertiesで`spring.profiles.active=prod`に設定。
1. コマンドラインで`./gradlew bootRun`を実行する。
1. http://localhost:8080/ で動かす。

### デプロイしたサービス

https://es4.eedept.kobe-u.ac.jp/video_chat_service/

