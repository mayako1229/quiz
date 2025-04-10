
// モーダル関連のDOMを取得
const passwordModal = document.getElementById("passwordModal");
const closeModalBtn = document.getElementById("closeModal");
const quizListButton = document.getElementById("quizListButton");
const confirmButton = document.getElementById("confirmButton");
const cancelButton = document.getElementById("cancelButton");

// ボタンのクリックイベントでモーダルを開く
quizListButton.addEventListener("click", ()=> {
	passwordModal.style.display = "block";
});

// モーダルを閉じる
closeModalBtn.addEventListener("click", closeModal);

// キャンセルボタンのクリックでモーダルを閉じる
cancelButton.addEventListener("click", closeModal);

// パスワード確認ボタンのクリックでパスワードチェック
confirmButton.addEventListener("click", ()=>{
	// パスワードをチェックして、正しければ遷移する{
	const password = document.getElementById("passwordInput").value;
	if (password === "admin") {
		closeModal();
		window.location.href = "/quizzes/list";
	} else {
		alert("パスワードが違います！");
	}
});

// モーダル外をクリックしたら閉じる
window.addEventListener("click", (e)=> {
	if (e.target === passwordModal) {
		closeModal();
	}
});

// モーダルを閉じる関数
function closeModal() {
	passwordModal.style.display = "none";
	document.getElementById("passwordInput").value = ""; // パスワード入力フィールドをリセット
}
