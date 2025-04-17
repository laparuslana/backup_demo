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
        type: "database",
        databaseName2: document.getElementById("databaseName2").value,
        dbServer2: document.getElementById("dbServer2").value,
        dbUser2: document.getElementById("dbUser2").value,
        dbPassword2: document.getElementById("dbPassword2").value,
        frequency: document.getElementById("frequency").value,
        day: document.getElementById("day").value,
        time: document.getElementById("time").value,
        daysKeep2: document.getElementById("daysKeep2").value,
        storageType2: document.getElementById("storageType2").value
    };


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
        .then(data => alert("✅ Backup parameters saved!"))
        .catch(error => alert("❌ Error saving backup parameters: " + error));
});

document.getElementById("fileFrequency").addEventListener("change", function () {
    let daySelection = document.getElementById("fileDaySelection");
    let dayDropdown = document.getElementById("fileDay");

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

const submitFile = document.getElementById("filesubmit");
submitFile.addEventListener('click', (event) => {
    event.preventDefault();

    let fileData = {
        type: "file",
        folderPath: document.getElementById("folderPath").value,
        frequency: document.getElementById("fileFrequency").value,
        day: document.getElementById("fileDay").value,
        time: document.getElementById("fileTime").value,
        daysKeep2: document.getElementById("daysKeep").value,
        storageType2: "ftp"
        };

    fetch("/api/backup/save", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(fileData)
    })
        .then(response => {
            if (!response.ok) {
                throw new Error("Server returned error");
            }
            return response.text();
        })
        .then(data => alert("✅ File backup parameters saved!"))
        .catch(error => alert("❌ Error saving file backup parameters: " + error));
});