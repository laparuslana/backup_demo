function loadDatabases() {

    const dbServerInput = document.getElementById("dbServer");
    const dbUserInput = document.getElementById("dbUser");
    const dbPasswordInput = document.getElementById("dbPassword");
    const databaseNameSelect = document.getElementById("databaseName");

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
                    databaseNameSelect.innerHTML = `<option value="">NOTHING </option>`;
                }
            })
            .catch(err => {
                console.error("Error fetching databases:", err);
                databaseNameSelect.innerHTML = `<option value="">Error loading databases</option>`;
            });
    }

    function loadAutoDatabases() {
        const dbServerInput2 = document.getElementById("dbServer2");
        const dbUserInput2 = document.getElementById("dbUser2");
        const dbPasswordInput2 = document.getElementById("dbPassword2");
        const databaseNameSelect2 = document.getElementById("databaseName2");

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

    function loadManDatabases() {

        const man_dbServer = document.getElementById("man_dbServer");
        const man_dbUser = document.getElementById("man_dbUser");
        const man_dbPassword = document.getElementById("man_dbPassword");
        const selectDbName = document.getElementById("selectTestDb");

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


    function loadInfobases() {
        const man_clusterAdmin = document.getElementById("man_clusterAdmin");
        const man_clusterUsername = document.getElementById("man_clusterUsername");
        const man_clusterPassword = document.getElementById("man_clusterPassword");
        const sourceDbName = document.getElementById("sourceDbName");

        const clusterAd = man_clusterAdmin.checked;
        const clusterUser = man_clusterUsername.value;
        const clusterPass = man_clusterPassword.value;

        fetch(`/api/restore/listInfobases?clusterAd=${encodeURIComponent(clusterAd)}&clusterUser=${encodeURIComponent(clusterUser)}&clusterPass=${encodeURIComponent(clusterPass)}`)
            .then(response => response.json())
            .then(data => {
                sourceDbName.innerHTML = "";
                if (Array.isArray(data)) {
                    data.forEach(db => {
                        const [uuid, name] = db.split(";");
                        const option = document.createElement("option");
                        option.value = uuid;
                        option.textContent = name;
                        sourceDbName.appendChild(option);
                    });
                } else {
                    sourceDbName.innerHTML = `<option value="">NOTHING</option>`;
                }
            })
            .catch(err => {
                console.error("Error fetching databases:", err);
                sourceDbName.innerHTML = `<option value="">Error loading databases</option>`;
            });
    }

    function listFiles(){
        const res_storageType = document.getElementById("res_storageType");
        const res_nameSelect = document.getElementById("res_nameSelect");

        const type = res_storageType.value;
        const nameSelect = res_nameSelect.value;

        fetch(`/api/restore/list?type=${encodeURIComponent(type)}&nameSelect=${encodeURIComponent(nameSelect)}`)
    .then(response => response.json())
    .then(files => {
        const select = document.getElementById('backupFile');
        select.innerHTML = "";
        files.forEach(file => {
            const option = document.createElement('option');
            option.value = file;
            option.textContent = file;
            select.appendChild(option);
        });
    })
    .catch(err => console.error('Error fetching backups:', err));
}


function selectArchives() {
    //const restore_storageType = document.getElementById("restore_storageType");
    const restore_nameSelect = document.getElementById("restore_nameSelect");

    //const type = restore_storageType.value;
    const nameSelect = restore_nameSelect.value;

    fetch(`/api/restore/archive?nameSelect=${encodeURIComponent(nameSelect)}`)
        .then(response => response.json())
        .then(files => {
            const select = document.getElementById('restoreFile');
            select.innerHTML = "";
            files.forEach(file => {
                const option = document.createElement('option');
                option.value = file;
                option.textContent = file;
                select.appendChild(option);
            });
        })
        .catch(err => console.error('Error fetching backups:', err));
}