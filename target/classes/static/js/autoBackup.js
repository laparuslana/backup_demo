document.getElementById("clusterAdmin2").addEventListener("change", function () {
    const clusterCredentials = document.getElementById("clusterCredentials2");
    clusterCredentials.style.display = this.checked ? "block" : "none";
});

document.getElementById("frequency").addEventListener("change", function () {
    let daySelection = document.getElementById("daySelection");
    let dayDropdown = document.getElementById("day");

    dayDropdown.innerHTML = "";

    if (this.value === "weekly") {
        daySelection.style.display = "block";
        ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"].forEach(day => {
            let option = document.createElement("option");
            option.value = day;
            option.textContent = day;
            dayDropdown.appendChild(option);
        });
    } else if (this.value === "monthly") {
        daySelection.style.display = "block";
        for (let i = 1; i <= 31; i++) {
            let option = document.createElement("option");
            option.value = i;
            option.textContent = i;
            dayDropdown.appendChild(option);
        }
    } else {
        daySelection.style.display = "none";
    }
});

const submitButtom = document.getElementById("autosubmit");
submitButtom.addEventListener('click', (event) => {
    event.preventDefault();

    let backupData2 = {
        clusterServer2: document.getElementById("clusterServer2").value,
        databaseName2: document.getElementById("databaseName2").value,
        dbServer2: document.getElementById("dbServer2").value,
        dbUser2: document.getElementById("dbUser2").value,
        dbPassword2: document.getElementById("dbPassword2").value,
        frequency: document.getElementById("frequency").value,
        day: document.getElementById("day").value,
        time: document.getElementById("time").value,
        daysKeep2: document.getElementById("daysKeep2").value,
        clusterAdmin2: document.getElementById("clusterAdmin2").checked,
        storageType2: document.getElementById("storageType2").value
    };

    if (backupData2.clusterAdmin2) {
        backupData2.clusterUsername2 = document.getElementById("clusterUsername2").value;
        backupData2.clusterPassword2 = document.getElementById("clusterPassword2").value;
    }

    if (backupData2.storageType2 === "LOCAL") {
        backupData2.backupLocation2 = document.getElementById("backupLocation2").value;
    }
    if (backupData2.storageType2 === "FTP") {
        backupData2.storageParams2 = {
            ftpServer2: document.getElementById("ftpServer2").value,
            ftpUser2: document.getElementById("ftpUser2").value,
            ftpPassword2: document.getElementById("ftpPassword2").value,
            ftpDirectory2: document.getElementById("ftpDirectory2").value
        };
    }

    fetch("/api/backup/save", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(backupData2)
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