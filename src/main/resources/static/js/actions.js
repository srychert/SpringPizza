const sortSelect = document.getElementById("sort");

sortSelect.addEventListener("change", (e) => {
	window.location.href = `/pizza/list?sort=${e.target.value}`;
});

const order = document.getElementById("order");

order.addEventListener("click", (e) => {
	console.log(order.dataset.username, order.dataset.userid);

	const d = document.createElement("div");
	d.className = "confirm-order";

	const label = document.createElement("label");
	label.innerText = "Your total";
	d.appendChild(label);
	const i = document.createElement("input");
	i.type = "number";
	i.step = "0.01";
	i.value = 0;
	i.readOnly = true;
	d.appendChild(i);

	const buttonContainer = document.createElement("div");
	buttonContainer.style = "display: flex;gap: 4px;";

	const cancelButton = document.createElement("button");
	cancelButton.className = "btn-secondary";
	cancelButton.style = "flex: 1;";
	cancelButton.innerText = "Cancel";
	buttonContainer.appendChild(cancelButton);

	const confirmButton = document.createElement("button");
	confirmButton.className = "btn-primary";
	confirmButton.style = "flex: 1;";
	confirmButton.innerText = "Confrim";
	buttonContainer.appendChild(confirmButton);

	d.appendChild(buttonContainer);
	const errorSpan = document.createElement("span");
	errorSpan.className = "error-li";
	d.appendChild(errorSpan);

	document.body.appendChild(d);

	// pizzas
	const checkboxElements = Array.from(document.querySelectorAll(`[id^="checkbox"]`));

	checkboxElements.forEach((box) => {
		console.log(box.checked, box.dataset.pizzaid, box.dataset.pizzaprice);
	});

	const total = checkboxElements.reduce((sum, box) => {
		if (box.checked) {
			return sum + parseFloat(box.dataset.pizzaprice) * 100;
		}
		return sum;
	}, 0);
	i.value = total / 100;

	const pizzas = checkboxElements.map((box) => (box.checked ? { id: box.dataset.pizzaid } : null)).filter((box) => box !== null);

	confirmButton.addEventListener("click", (_confirmEvent) => {
		console.log("confirm");

		if (pizzas.length === 0) {
			errorSpan.innerText = "You need to add at least one pizza to order";
			return;
		}

		axios
			.post("/api/order", {
				client: {
					id: order.dataset.userid,
				},
				pizzas,
			})
			.then((r) => {
				console.log(r);
				cancelButton.click();
				window.location.href = `/order/${r.data.id}`;
			})
			.catch((err) => console.log(err));
	});

	cancelButton.addEventListener("click", (confirmEvent) => {
		console.log("cancel");
		d.remove();
	});
});
