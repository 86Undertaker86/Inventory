document.addEventListener("DOMContentLoaded", () => {

    /* === Показ / приховання форм додавання та редагування === */
    const addBtn = document.getElementById('toggleAddBtn');
    const editBtn = document.getElementById('toggleEditBtn');
    const addForm = document.getElementById('addForm');
    const editForm = document.getElementById('editForm');

    if (addBtn && addForm) {
        addBtn.addEventListener('click', () => {
            const hidden = addForm.classList.toggle('hidden');
            addBtn.textContent = hidden ? addBtn.dataset.show || 'Додати' : addBtn.dataset.hide || 'Закрити форму';
            if (!hidden && editForm && !editForm.classList.contains('hidden')) {
                editForm.classList.add('hidden');
                if (editBtn) editBtn.textContent = editBtn.dataset.show || 'Редагувати';
            }
        });
    }

    if (editBtn && editForm) {
        editBtn.addEventListener('click', () => {
            const hidden = editForm.classList.toggle('hidden');
            editBtn.textContent = hidden ? editBtn.dataset.show || 'Редагувати' : editBtn.dataset.hide || 'Закрити форму редагування';
            if (!hidden && addForm && !addForm.classList.contains('hidden')) {
                addForm.classList.add('hidden');
                if (addBtn) addBtn.textContent = addBtn.dataset.show || 'Додати';
            }
        });
    }

    /* === Повідомлення (автозникнення) === */
    document.querySelectorAll('.message').forEach(alert => {
        setTimeout(() => alert.classList.add('fade-out'), 3000);
        setTimeout(() => alert.remove(), 4000);
    });

    /* === Прибирання параметрів з URL === */
    if (window.location.search) {
        const cleanUrl = window.location.origin + window.location.pathname;
        window.history.replaceState({}, document.title, cleanUrl);
    }

    /* === 🔍 Універсальний пошук у таблицях === */
    const searchInput = document.getElementById('searchInput');
    const table = document.querySelector('table');
    if (searchInput && table) {
        const rows = table.querySelectorAll('tbody tr');
        searchInput.addEventListener('input', () => {
            const filter = searchInput.value.toLowerCase();
            rows.forEach(row => {
                const text = row.innerText.toLowerCase();
                row.style.display = text.includes(filter) ? '' : 'none';
            });
        });
    }

    /* === 🧩 Універсальне завантаження даних для редагування (Item, Location, Supplier, User) === */
    const selectEl = document.querySelector('#itemSelect, #locationSelect, #supplierSelect, #userSelect');
    const editFormEl = document.querySelector('#itemEditForm, #locationEditForm, #supplierEditForm, #userEditForm');

    if (selectEl && editFormEl) {
        selectEl.selectedIndex = 0;

        selectEl.addEventListener('change', async () => {
            const id = selectEl.value;
            if (!id) {
                editFormEl.classList.add('hidden');
                return;
            }

            const basePath = selectEl.id.replace('Select', '');
            let urlPrefix = '/manager';
            if (window.location.pathname.includes('/admin')) {
                urlPrefix = '/admin';
            }
            const url = `${urlPrefix}/${basePath}s/get/${id}`;

            try {
                const response = await fetch(url);
                if (!response.ok) throw new Error("Не вдалося отримати дані");
                const data = await response.json();

                for (const key in data) {
                    const input = editFormEl.querySelector(`[name='${key}'], #edit_${key}`);

                    if (input) {
                        if (key === 'supplier' && data.supplier) {
                            // якщо постачальник існує, вставляємо його ID
                            input.value = data.supplier.supplier_id;
                        } else {
                            input.value = data[key] ?? '';
                        }
                    }
                }

                editFormEl.action = `/manager/${basePath}s/update/${id}`;
                editFormEl.classList.remove('hidden');
            } catch (err) {
                alert("Помилка при отриманні даних!");
                console.error(err);
            }
        });
    }

    /* === 🔄 Рух товарів: перемикач типу (TRANSFER / IN / OUT) === */
    const typeSelect = document.getElementById("movementType");
    const transferFields = document.getElementById("transferFields");
    const singleLocation = document.getElementById("singleLocation");

    if (typeSelect) {
        function updateVisibility() {
            if (typeSelect.value === "TRANSFER") {
                if (transferFields) transferFields.style.display = "block";
                if (singleLocation) singleLocation.style.display = "none";
            } else {
                if (transferFields) transferFields.style.display = "none";
                if (singleLocation) singleLocation.style.display = "block";
            }
        }

        typeSelect.addEventListener("change", updateVisibility);
        updateVisibility();
    }

});