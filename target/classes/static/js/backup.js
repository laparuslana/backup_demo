document.getElementById("clusterAdmin").addEventListener("change", function () {
    const clusterCredentials = document.getElementById("clusterCredentials");
    clusterCredentials.style.display = this.checked ? "block" : "none";
});

const submitButttom = document.getElementById("submit");
submitButttom.addEventListener('click', (event) => {
    event.preventDefault();

    let backupData = {
        clusterServer: document.getElementById("clusterServer").value,
        databaseName: document.getElementById("databaseName").value,
        dbServer: document.getElementById("dbServer").value,
        dbUser: document.getElementById("dbUser").value,
        dbPassword: document.getElementById("dbPassword").value,
        clusterAdmin: document.getElementById("clusterAdmin").checked,
        storageType: document.getElementById("storageType").value
    };

    if (backupData.clusterAdmin) {
        backupData.clusterUsername = document.getElementById("clusterUsername").value;
        backupData.clusterPassword = document.getElementById("clusterPassword").value;
    }
    //
    // if (backupData.storageType === "local") {
    //     backupData.backupLocation = document.getElementById("backupLocation").value;
    // }
    // if (backupData.storageType === "ftp") {
    //     backupData.storageParams = {
    //         ftpServer: document.getElementById("ftpServer").value,
    //         ftpUser: document.getElementById("ftpUser").value,
    //         ftpPassword: document.getElementById("ftpPassword").value,
    //         ftpDirectory: document.getElementById("ftpDirectory").value
    //     };
    // }


    fetch("/api/backup/execute", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(backupData)
    })
        .then(response => {
            if (!response.ok) {
                throw new Error("Server returned error");
            }
            return response.text();
        })
        .then(data => alert("✅ Backup started successfully!"))
        .catch(error => alert("❌ Error starting backup: " + error));
});