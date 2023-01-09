const sortSelect = document.getElementById("sort");

sortSelect.addEventListener("change", (e) => {
	window.location.href = `/pizza/list?sort=${e.target.value}`;
});
