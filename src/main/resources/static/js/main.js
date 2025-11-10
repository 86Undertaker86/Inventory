document.addEventListener("DOMContentLoaded", () => {

    /* === Toggle display of Add and Edit forms === */
    const addBtn = document.getElementById('toggleAddBtn');
    const editBtn = document.getElementById('toggleEditBtn');
    const addForm = document.getElementById('addForm');
    const editForm = document.getElementById('editForm');

    // Handle show/hide for "Add" form
    if (addBtn && addForm) {
        addBtn.addEventListener('click', () => {
            const hidden = addForm.classList.toggle('hidden');
            addBtn.textContent = hidden ? addBtn.dataset.show || 'Add' : addBtn.dataset.hide || 'Close form';

            // If Edit form is open — close it
            if (!hidden && editForm && !editForm.classList.contains('hidden')) {
                editForm.classList.add('hidden');
                if (editBtn) editBtn.textContent = editBtn.dataset.show || 'Edit';
            }
        });
    }

    // Handle show/hide for "Edit" form
    if (editBtn && editForm) {
        editBtn.addEventListener('click', () => {
            const hidden = editForm.classList.toggle('hidden');
            editBtn.textContent = hidden ? editBtn.dataset.show || 'Edit' : editBtn.dataset.hide || 'Close edit form';

            // If Add form is open — close it
            if (!hidden && addForm && !addForm.classList.contains('hidden')) {
                addForm.classList.add('hidden');
                if (addBtn) addBtn.textContent = addBtn.dataset.show || 'Add';
            }
        });
    }

    /* === Flash message auto-removal === */
    document.querySelectorAll('.message').forEach(alert => {
        setTimeout(() => alert.classList.add('fade-out'), 3000);
        setTimeout(() => alert.remove(), 4000);
    });

    /* === Clean URL parameters after load === */
    if (window.location.search) {
        const cleanUrl = window.location.origin + window.location.pathname;
        window.history.replaceState({}, document.title, cleanUrl);
    }

    /* === Universal table search === */
    const searchInput = document.getElementById('searchInput');
    const table = document.querySelector('table');
    if (searchInput && table) {
        const rows = table.querySelectorAll('tbody tr');
        // Filters table rows by search text in real time
        searchInput.addEventListener('input', () => {
            const filter = searchInput.value.toLowerCase();
            rows.forEach(row => {
                const text = row.innerText.toLowerCase();
                row.style.display = text.includes(filter) ? '' : 'none';
            });
        });
    }

    /* === Dynamic data loading for edit forms (Item, Location, Supplier, User) === */
    const selectEl = document.querySelector('#itemSelect, #locationSelect, #supplierSelect, #userSelect');
    const editFormEl = document.querySelector('#itemEditForm, #locationEditForm, #supplierEditForm, #userEditForm');

    if (selectEl && editFormEl) {
        selectEl.selectedIndex = 0;

        // When user selects an item — load its data into the edit form
        selectEl.addEventListener('change', async () => {
            const id = selectEl.value;
            if (!id) {
                editFormEl.classList.add('hidden');
                return;
            }

            // Determine correct base path for the current page (admin/manager)
            const basePath = selectEl.id.replace('Select', '');
            let urlPrefix = '/manager';
            if (window.location.pathname.includes('/admin')) {
                urlPrefix = '/admin';
            }
            const url = `${urlPrefix}/${basePath}s/get/${id}`;

            try {
                const response = await fetch(url);
                if (!response.ok) throw new Error("Failed to fetch data");
                const data = await response.json();

                // Fill in all form inputs with received data
                for (const key in data) {
                    const input = editFormEl.querySelector(`[name='${key}'], #edit_${key}`);

                    if (input) {
                        // Special case: if the field is a supplier object, set its ID
                        if (key === 'supplier' && data.supplier) {
                            input.value = data.supplier.supplier_id;
                        } else {
                            input.value = data[key] ?? '';
                        }
                    }
                }

                // Set the correct form action URL for update
                editFormEl.action = `${urlPrefix}/${basePath}s/update/${id}`;
                editFormEl.classList.remove('hidden');
            } catch (err) {
                alert("Error while fetching data!");
                console.error(err);
            }
        });
    }

    /* === Movement type toggle (TRANSFER / IN / OUT) === */
    const typeSelect = document.getElementById("movementType");
    const transferFields = document.getElementById("transferFields");
    const singleLocation = document.getElementById("singleLocation");

    // Switch visibility depending on selected movement type
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

    /* === Validation: ensure min_level ≤ max_level === */
    document.querySelectorAll('form').forEach(form => {
        const minInput = form.querySelector('input[name="min_level"]');
        const maxInput = form.querySelector('input[name="max_level"]');

        if (!minInput || !maxInput) return;

        // Set default minimum values for inputs if not defined
        if (!minInput.hasAttribute('min')) minInput.setAttribute('min', '0');
        if (!maxInput.hasAttribute('min')) maxInput.setAttribute('min', '0');

        // Function checks that max ≥ min and sets custom validation message
        function validateLevels(showNow = false) {
            const min = Number(minInput.value);
            const max = Number(maxInput.value);

            if (minInput.value === '' || maxInput.value === '') {
                maxInput.setCustomValidity('');
                return;
            }

            if (max < min) {
                maxInput.setCustomValidity("Max level cannot be less than min level!");
            } else {
                maxInput.setCustomValidity('');
            }

            if (showNow) maxInput.reportValidity();
        }

        // Attach validation on input change and before form submission
        minInput.addEventListener('input', () => validateLevels(false));
        maxInput.addEventListener('input', () => validateLevels(false));

        form.addEventListener('submit', (e) => {
            validateLevels(true);
            if (maxInput.validationMessage) {
                e.preventDefault();
                maxInput.reportValidity();
            }
        });
    });
});