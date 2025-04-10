const radios = document.querySelectorAll('input[name="choice"]');
const submitButton = document.querySelector('#answer-btn button');
const hiddenInput = document.getElementById('choiceNumHidden');
const modal = document.getElementById('resultModal');
const modalContent = document.getElementById('modalContent');
const closeBtn = document.querySelector('.close-btn');
let offsetX = 0;
let offsetY = 0;
let isDragging = false;

radios.forEach(radio => {
	radio.addEventListener('change', (e) => {
		// どれか1つでも選択されていればボタンを有効に
		const isChecked = Array.from(radios).some(r => r.checked);
		console.log(e.target.checked);
		submitButton.disabled = !isChecked;
		//　選択肢の番号を隠しフィールドに格納
		if (e.target.checked) {
			const choiceNum = e.target.getAttribute('data-choice-num');
			hiddenInput.value = choiceNum;
		}
	});
});

// 戻るボタンを押したらトップページへ強制遷移
window.addEventListener('popstate', function (event) {
	// タイムスタンプをクエリにつけて、キャッシュを無視させる
	const now = new Date().getTime();
	window.location.href = '/quizzes?ts=' + now;
});

document.getElementById('quizForm').addEventListener('submit', function (event) {
	event.preventDefault();
	const formData = new FormData(this);

	fetch('/quizzes/check-answer', {
		method: 'POST',
		body: formData
	})
		.then(response => response.json())
		.then(data => {
			// モーダル要素
			const modal = document.getElementById('resultModal');
			document.getElementById('modalResultText').textContent = data.correct ? '結果：○' : '結果：×';
			document.getElementById('modalResultText').style.color = data.correct ? '#4CAF50' : '#e53935';

			document.getElementById('yourAnswer').textContent = data.yourAnswer;
			document.getElementById('correctAnswer').textContent = data.correctAnswer;
			document.getElementById('description').textContent = data.description;

			// モーダル表示
			modal.style.display = 'block';

			// 回答ボタン非表示、次へボタン表示
			document.getElementById('answer-btn').style.display = 'none';
			document.getElementById('next').style.display = 'block';

			//ラジオボタン無効化
			radios.forEach(radio => {
				radio.disabled = true;
			});
		})
		.catch(error => console.error('Error:', error));
});

// モーダル外をクリックで閉じる
window.addEventListener('click', function (event) {
	const modal = document.getElementById('resultModal');
	if (event.target === modal) {
		modal.style.display = 'none';
	}
});

// 閉じるボタンでも閉じる
document.querySelector('.close-btn').addEventListener('click', function () {
	document.getElementById('resultModal').style.display = 'none';
});


// ドラッグ開始
modalContent.addEventListener('mousedown', (e) => {
	isDragging = true;
	offsetX = e.clientX - modalContent.offsetLeft;
	offsetY = e.clientY - modalContent.offsetTop;
	document.body.style.cursor = 'grabbing'; // ドラッグ中のカーソル
});

// ドラッグ中
document.addEventListener('mousemove', (e) => {
	if (isDragging) {
		const x = e.clientX - offsetX;
		const y = e.clientY - offsetY;
		modalContent.style.left = `${x}px`;
		modalContent.style.top = `${y}px`;
	}
});

// ドラッグ終了
document.addEventListener('mouseup', () => {
	isDragging = false;
	document.body.style.cursor = 'default'; // 通常のカーソル
});

