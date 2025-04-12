
const submitButttom = document.getElementById("submit");
submitButttom.addEventListener('click', (event) => {
    event.preventDefault();

    let backupData = {
        databaseName: document.getElementById("databaseName").value,
        dbServer: document.getElementById("dbServer").value,
        dbUser: document.getElementById("dbUser").value,
        dbPassword: document.getElementById("dbPassword").value,
        storageType: document.getElementById("storageType").value
    };


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