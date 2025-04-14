
-- data.sql

INSERT INTO public.quizzes (question, choice1, choice2, choice3, choice4, answer, description, created_at)
VALUES 
('Javaのデータ型で浮動小数点数を扱うものはどれか？', 'int', 'float', 'boolean', 'char', 2, '浮動小数点数を扱う型はfloatです。', now()),

('Javaで配列の要素数を取得するにはどのメソッドを使うか？', 'length()', 'size()', 'getSize()', 'getLength()', 1, '配列の要素数はlength()で取得できます。', now()),

('次のコードの結果は？\n int a = 5;\n int b = ++a + a++;', '10', '11', '12', '13', 2, '「++a」は前置インクリメント、a++は後置インクリメントです。', now()),

('次のコードの出力結果は？\n System.out.println("Java".substring(1, 3));', 'av', 'Ja', 'va', 'Java', 1, 'substring(1, 3)でインデックス1から3までの部分文字列が取得されます。', now()),

('次のコードのエラーは何か？\n String s = null;\n System.out.println(s.length());', 'NullPointerException', 'ArrayIndexOutOfBoundsException', 'ClassCastException', 'IOException', 1, 'nullのオブジェクトに対してメソッドを呼び出すとNullPointerExceptionが発生します。', now()),

('Javaのインターフェースにメソッドを定義する場合、メソッドに何を付ける必要があるか？', 'public static', 'abstract', 'final', 'private', 2, 'インターフェース内のメソッドは自動的にabstractです。', now()),

('Javaでスレッドを実行するために使用するメソッドは？', 'start()', 'run()', 'execute()', 'init()', 1, 'スレッドを実行するにはstart()メソッドを使います。', now()),

('Javaで文字列を比較する際に使うメソッドは？', '==', 'equals()', 'compare()', 'contains()', 2, '文字列を内容で比較するにはequals()メソッドを使用します。', now()),

('次のコードの結果は？\n String s1 = "Java";\n String s2 = "Java";\n System.out.println(s1 == s2);', 'true', 'false', 'null', 'エラーが発生する', 1, '文字列は文字列プールで共有されるため、同じ文字列は同じオブジェクトを参照します。', now()),

('次のコードで「例外が発生しない」という条件はどれか？\n try {\n   int[] arr = new int[2];\n   arr[3] = 10;\n } catch (ArrayIndexOutOfBoundsException e) {\n   System.out.println("例外");\n }', '配列のインデックスを正しく指定した場合', 'catch節が省略された場合', 'arrに値を設定しない場合', '例外処理を行わない場合', 1, '配列インデックスが範囲外だとArrayIndexOutOfBoundsExceptionが発生します。', now())

ON CONFLICT (id) DO NOTHING;
