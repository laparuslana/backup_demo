// document.getElementById("res_clusterAdmin").addEventListener("change", function () {
//     const clusterCredentials = document.getElementById("res_clusterCredentials");
//     clusterCredentials.style.display = this.checked ? "block" : "none";
// });

const submitButom = document.getElementById("submitRestore");
submitButom.addEventListener('click', (event) => {
    event.preventDefault();

    let restoreData = {
        res_clusterServer: document.getElementById("res_clusterServer").value,
        testDbName: document.getElementById("testDbName").value,
        restoreDbServer: document.getElementById("restoreDbServer").value,
        restoreDbUser: document.getElementById("restoreDbUser").value,
        restoreDbPassword: document.getElementById("restoreDbPassword").value,
        // res_clusterAdmin: document.getElementById("res_clusterAdmin").checked,
        backupFile: document.getElementById("backupFile").value,

    };
    //
    // if (restoreData.res_clusterAdmin) {
    //     restoreData.res_clusterUsername = document.getElementById("res_clusterUsername").value;
    //     restoreData.res_clusterPassword = document.getElementById("res_clusterPassword").value;
    // }

    //validateDbName(restoreData.testDbName);

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