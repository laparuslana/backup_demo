
const submitButttom = document.getElementById("submit");
submitButttom.addEventListener('click', (event) => {
    event.preventDefault();

    let backupData = {
        databaseName: document.getElementById("databaseName").value,
        dbServer: document.getElementById("dbServer").value,
        dbUser: document.getElementById("dbUser").value,
        dbPassword: document.getElementById("dbPassword").value,
        storageType: document.getElementById("storageType").value,
        nameSelect: document.getElementById("nameSelect").value
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
            startProgressAutoUpdate();
            return response.text();
        })
        .then(data => alert("✅ Backup started successfully!"))
        .catch(error => alert("❌ Error starting backup: " + error));
});


let progressInterval;
let simulatedProgress = 0;
let realProgress = 0;

function startProgressAutoUpdate() {
    simulatedProgress = 0;
    realProgress = 0;
    if (progressInterval) clearInterval(progressInterval);

    progressInterval = setInterval(() => {
        fetchAndShowProgress();
    }, 3000);
}

function fetchAndShowProgress() {
    fetch('/api/progress')
        .then(response => {
            if (response.status === 204) {
                const container = document.getElementById('progressContainer').innerHTML = 'No active progress';
                container.style.display = 'none';
                clearInterval(progressInterval);
                return;
            }
            return response.json();
        })
        .then(progress => {
            if (!progress) return;

            const container = document.getElementById('progressContainer').innerHTML = 'No active progress';
            container.style.display = 'block';

            const progressHtml = `
        <div class="progress-bar">
            <div class="progress-fill" style="width: ${progress.percent}%; transition: width 0.5s;"></div>
        </div>
        <p>${progress.percent}% ${progress.status}</p>
        <small>Database: ${progress.databaseName} | Backup Location: ${progress.backupLocation}</small>
        <small>Started: ${progress.startTime} | Updated: ${progress.updateTime}</small>

    `;
            document.getElementById('progressContainer').innerHTML = progressHtml;

            realProgress = progress.percent;

            if (realProgress >= 100) {
                clearInterval(progressInterval);
            }
        })
        .catch(error => {
            document.getElementById('progressContainer').innerHTML = 'No active progress';
            clearInterval(progressInterval);
        });
}
