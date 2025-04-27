document.getElementById("res_clusterAdmin").addEventListener("change", function () {
    const clusterCredentials = document.getElementById("res_clusterCredentials");
    clusterCredentials.style.display = this.checked ? "block" : "none";
});

const submitButom = document.getElementById("submitRestore");
submitButom.addEventListener('click', async(event) => {
    event.preventDefault();

    let restoreData = {
        testDbName: document.getElementById("testDbName").value,
        restoreDbServer: document.getElementById("restoreDbServer").value,
        restoreDbUser: document.getElementById("restoreDbUser").value,
        restoreDbPassword: document.getElementById("restoreDbPassword").value,
        res_storageType: document.getElementById("res_storageType").value,
        backupFile: document.getElementById("backupFile").value,
        res_clusterAdmin: document.getElementById("res_clusterAdmin").checked,
        res_nameSelect: document.getElementById("res_nameSelect").value
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
            //startRestoreProgressAutoUpdate();
            return response.text();
        })
        .then(data => alert("✅ Restore started successfully!"))
        .catch(error => alert("❌ Error starting restore: " + error));
});

let progressInterval;
let simulatedProgress = 0;
let realProgress = 0;
function startRestoreProgressAutoUpdate() {
    simulatedProgress = 0;
    realProgress = 0;
    if (progressInterval) clearInterval(progressInterval);

    progressInterval = setInterval(() => {
        fetchAndShowRestoreProgress();
    }, 3000);
 }


function fetchAndShowRestoreProgress() {
    fetch('/api/restoreProgress')
        .then(response => {
            if (response.status === 204) {
                document.getElementById('progressRestoreContainer').innerHTML = 'No active progress';
                clearInterval(progressInterval);
                return;
            }
            return response.json();
        })
        .then(progress => {
            if (!progress) return;

            const progressHtml = `
        <div class="progress-bar">
            <div class="progress-fill" style="width: ${progress.percent}%; transition: width 0.5s;"></div>
        </div>
        <p>${progress.percent}% ${progress.status}</p>
        <small>Backup file: ${progress.backupFile} | Soure Database: ${progress.sourceDatabase}</small>
        <small>Started: ${progress.startTime} | Updated: ${progress.updateTime}</small>
       
    `;
            document.getElementById('progressRestoreContainer').innerHTML = progressHtml;

            realProgress = progress.percent;

            if (realProgress >= 100) {
                clearInterval(progressInterval);
            }
        })
        .catch(error => {
            document.getElementById('progressRestoreContainer').innerHTML = 'No active progress';
            clearInterval(progressInterval);
        });
}


document.getElementById("res_clusterAdmin").addEventListener("change", function () {
    const clusterCredentials = document.getElementById("res_clusterCredentials");
    clusterCredentials.style.display = this.checked ? "block" : "none";
});

const submitRestore = document.getElementById("submitFileRestore");
submitRestore.addEventListener('click', async(event) => {
    event.preventDefault();

    const restorePath = document.getElementById("restorePath").value;
    const restoreFile = document.getElementById("restoreFile").value;
    const nameSelect = document.getElementById("restore_nameSelect").value;

    fetch(`/api/restore/file?restorePath=${encodeURIComponent(restorePath)}&restoreFile=${encodeURIComponent(restoreFile)}&name=${encodeURIComponent(nameSelect)}`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        }
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
    const man_DbPassword = document.getElementById("man_dbPassword").value;
    const selectTestDb = document.getElementById("selectTestDb").value;
    const confirmAction = document.getElementById("confirmAction").checked;

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
            alert("✅ Delete started successfully!")
            return response.json();
        })
        .then(data => {
            alert(data.message);
        })
        .catch(error => alert("❌ Error starting delete: " + error));
});

document.getElementById("man_clusterAdmin").addEventListener("change", function () {
    const clusterCredentials = document.getElementById("man_clusterCredentials");
    clusterCredentials.style.display = this.checked ? "block" : "none";
});

const submitButon = document.getElementById("submitSwitch");
submitButon.addEventListener('click', (event) => {
    event.preventDefault();

    const man_clusterAdmin = document.getElementById("man_clusterAdmin").checked;
    const sourceDbName = document.getElementById("sourceDbName").value;
    const confirmAction = document.getElementById("confirmAction").checked;

    let man_clusterUsername= "";
    let man_clusterPassword = "";

    if (man_clusterAdmin) {
        man_clusterUsername = document.getElementById("man_clusterUsername").value;
        man_clusterPassword = document.getElementById("man_clusterPassword").value;
    }

    if (!confirmAction) {
        alert("❗ Please confirm the action before deleting.");
        return;
    }

    fetch(`/api/restore/switch?clusterAd=${encodeURIComponent(man_clusterAdmin)}&clusterUser=${encodeURIComponent(man_clusterUsername)}&clusterPass=${encodeURIComponent(man_clusterPassword)}&infobase=${encodeURIComponent(sourceDbName)}`,
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
            alert("✅ Switch started successfully!")
            return response.json();
        })
        .then(data => {
            alert(data.message);
})
        .catch(error => alert("❌ Error starting delete: " + error));
});



