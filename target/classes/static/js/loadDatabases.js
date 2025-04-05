
document.addEventListener("DOMContentLoaded", () => {
    const dbServerInput = document.getElementById("dbServer");
    const dbUserInput = document.getElementById("dbUser");
    const dbPasswordInput = document.getElementById("dbPassword");
    const databaseNameSelect = document.getElementById("databaseName");

    dbServerInput.addEventListener("change", loadDatabases);
    dbUserInput.addEventListener("change", loadDatabases);
    dbPasswordInput.addEventListener("change", loadDatabases);

    function loadDatabases() {
        const dbServer = dbServerInput.value;
        const dbUser = dbUserInput.value;
        const dbPassword = dbPasswordInput.value;

        if (!dbServer || !dbUser || !dbPassword) return;

        fetch(`/api/backup/listDatabases?dbServer=${encodeURIComponent(dbServer)}&dbUser=${encodeURIComponent(dbUser)}&dbPassword=${encodeURIComponent(dbPassword)}`)
            .then(response => response.json())
            .then(data => {
                databaseNameSelect.innerHTML = "";
                if (Array.isArray(data)) {
                    data.forEach(db => {
                        const option = document.createElement("option");
                        option.value = db;
                        option.textContent = db;
                        databaseNameSelect.appendChild(option);
                    });
                } else {
                    databaseNameSelect.innerHTML = `<option value="">${data[0]}</option>`;
                }
            })
            .catch(err => {
                console.error("Error fetching databases:", err);
                databaseNameSelect.innerHTML = `<option value="">Error loading databases</option>`;
            });
    }
    loadDatabases();
})

document.addEventListener("DOMContentLoaded", () => {
    const dbServerInput2 = document.getElementById("dbServer2");
    const dbUserInput2 = document.getElementById("dbUser2");
    const dbPasswordInput2 = document.getElementById("dbPassword2");
    const databaseNameSelect2 = document.getElementById("databaseName2");

    dbServerInput2.addEventListener("change", loadDatabases);
    dbUserInput2.addEventListener("change", loadDatabases);
    dbPasswordInput2.addEventListener("change", loadDatabases);

    function loadDatabases() {
        const dbServer2 = dbServerInput2.value;
        const dbUser2 = dbUserInput2.value;
        const dbPassword2 = dbPasswordInput2.value;

        if (!dbServer2 || !dbUser2 || !dbPassword2) return;

        fetch(`/api/backup/listDatabases?dbServer=${encodeURIComponent(dbServer2)}&dbUser=${encodeURIComponent(dbUser2)}&dbPassword=${encodeURIComponent(dbPassword2)}`)
            .then(response => response.json())
            .then(data => {
                databaseNameSelect2.innerHTML = "";
                if (Array.isArray(data)) {
                    data.forEach(db => {
                        const option = document.createElement("option");
                        option.value = db;
                        option.textContent = db;
                        databaseNameSelect2.appendChild(option);
                    });
                } else {
                    databaseNameSelect2.innerHTML = `<option value="">${data[0]}</option>`;
                }
            })
            .catch(err => {
                console.error("Error fetching databases:", err);
                databaseNameSelect2.innerHTML = `<option value="">Error loading databases</option>`;
            });
    }
    loadDatabases();
})