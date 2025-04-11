document.getElementById("res_clusterAdmin").addEventListener("change", function () {
    const clusterCredentials = document.getElementById("res_clusterCredentials");
    clusterCredentials.style.display = this.checked ? "block" : "none";
});

const submitButom = document.getElementById("submitRestore");
submitButom.addEventListener('click', async(event) => {
    event.preventDefault();

    let restoreData = {
        res_clusterServer: document.getElementById("res_clusterServer").value,
        testDbName: document.getElementById("testDbName").value,
        restoreDbServer: document.getElementById("restoreDbServer").value,
        restoreDbUser: document.getElementById("restoreDbUser").value,
        restoreDbPassword: document.getElementById("restoreDbPassword").value,
        res_storageType: document.getElementById("res_storageType").value,
        backupFile: document.getElementById("backupFile").value,
        res_clusterAdmin: document.getElementById("res_clusterAdmin").checked

    };

    if (restoreData.res_clusterAdmin) {
        restoreData.res_clusterUsername = document.getElementById("res_clusterUsername").value;
        restoreData.res_clusterPassword = document.getElementById("res_clusterPassword").value;
    }

    const checkDbExistsUrl = `/api/backup/listDatabases?dbServer=${encodeURIComponent(restoreData.restoreDbServer)}&dbUser=${encodeURIComponent(restoreData.restoreDbUser)}&dbPassword=${encodeURIComponent(restoreData.restoreDbPassword)}`;

    try {
        const response = await fetch(checkDbExistsUrl);
        if (!response.ok) {
            throw new Error("Failed to check databases");
        }

        const databases = await response.json();
        if (databases.includes(restoreData.testDbName)) {
            alert(`❌ Database "${restoreData.testDbName}" already exists on the server.`);
            return;
        }
    } catch (err) {
        alert(`❌ Error checking database existence: ${err.message}`);
        return;
    }
    if (!validateDbName(restoreData.testDbName)) {
        return;
    }

    fetch("/api/restore/backup", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(restoreData)
    })
        .then(response => {
            if (!response.ok) {
                throw new Error("Server returned error");
            }
            return response.text();
        })
        .then(data => alert("✅ Restore started successfully!"))
        .catch(error => alert("❌ Error starting restore: " + error));
});

const submitButton = document.getElementById("submitDelete");
submitButton.addEventListener('click', (event) => {
    event.preventDefault();

    const man_DbServer = document.getElementById("man_dbServer").value;
    const man_DbUser = document.getElementById("man_dbUser").value;
    const  man_DbPassword = document.getElementById("man_dbPassword").value;
    const  selectTestDb = document.getElementById("selectTestDb").value;
    const   confirmAction = document.getElementById("confirmAction").checked;

    if (!confirmAction) {
        alert("❗ Please confirm the action before deleting.");
        return;
    }

    fetch(`/api/restore/manage?testDb=${encodeURIComponent(selectTestDb)}&server=${encodeURIComponent(man_DbServer)}&user=${encodeURIComponent(man_DbUser)}&password=${encodeURIComponent(man_DbPassword)}`,
        {
            method: "POST",
            headers: {
                "Accept": "application/json"
            }
    })
        .then(response => {
            if (!response.ok) {
                throw new Error("Server returned error");
            }
            return response.text();
        })
        .then(data => alert("✅ Delete started successfully!"))
        .catch(error => alert("❌ Error starting delete: " + error));
});