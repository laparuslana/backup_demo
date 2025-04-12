
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


document.addEventListener("DOMContentLoaded", () => {
    const man_dbServer = document.getElementById("man_dbServer");
    const man_dbUser = document.getElementById("man_dbUser");
    const man_dbPassword = document.getElementById("man_dbPassword");
    const selectDbName = document.getElementById("selectTestDb");

    man_dbServer.addEventListener("change", loadDatabases);
    man_dbUser.addEventListener("change", loadDatabases);
    man_dbPassword.addEventListener("change", loadDatabases);

    function loadDatabases() {
        const dbServer = man_dbServer.value;
        const dbUser = man_dbUser.value;
        const dbPassword = man_dbPassword.value;

        if (!dbServer || !dbUser || !dbPassword) return;

        fetch(`/api/backup/listDatabases?dbServer=${encodeURIComponent(dbServer)}&dbUser=${encodeURIComponent(dbUser)}&dbPassword=${encodeURIComponent(dbPassword)}`)
            .then(response => response.json())
            .then(data => {
                selectDbName.innerHTML = "";
                if (Array.isArray(data)) {
                    data.forEach(db => {
                        const option = document.createElement("option");
                        option.value = db;
                        option.textContent = db;
                        selectDbName.appendChild(option);
                    });
                } else {
                    selectDbName.innerHTML = `<option value="">${data[0]}</option>`;
                }
            })
            .catch(err => {
                console.error("Error fetching databases:", err);
                selectDbName.innerHTML = `<option value="">Error loading databases</option>`;
            });
    }
    loadDatabases();
})


document.addEventListener("DOMContentLoaded", () => {
    const man_bafPath = document.getElementById("man_bafPath");
    const man_clusterAdmin = document.getElementById("man_clusterAdmin");
    const man_clusterUsername = document.getElementById("man_clusterUsername");
    const man_clusterPassword = document.getElementById("man_clusterPassword");
    const sourceDbName = document.getElementById("sourceDbName");

    man_bafPath.addEventListener("change", loadInfobases)
    man_clusterAdmin.addEventListener("change", loadInfobases)
    man_clusterUsername.addEventListener("change", loadInfobases);
    man_clusterPassword.addEventListener("change", loadInfobases);

    function loadInfobases() {
        const bafPath = man_bafPath.value;
        const clusterAd = man_clusterAdmin.checked;
        const clusterUser = man_clusterUsername.value;
        const clusterPass = man_clusterPassword.value;

        fetch(`/api/restore/listInfobases?bafPath=${encodeURIComponent(bafPath)}&clusterAd=${encodeURIComponent(clusterAd)}&clusterUser=${encodeURIComponent(clusterUser)}&clusterPass=${encodeURIComponent(clusterPass)}`)
            .then(response => response.json())
            .then(data => {
                sourceDbName.innerHTML = "";
                if (Array.isArray(data)) {
                    data.forEach(db => {
                        const option = document.createElement("option");
                        option.value = db;
                        option.textContent = db;
                        sourceDbName.appendChild(option);
                    });
                } else {
                    sourceDbName.innerHTML = `<option value="">${data[0]}</option>`;
                }
            })
            .catch(err => {
                console.error("Error fetching databases:", err);
                sourceDbName.innerHTML = `<option value="">Error loading databases</option>`;
            });
    }
    loadInfobases();
})