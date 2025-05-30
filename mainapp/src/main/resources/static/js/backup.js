
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
                throw new Error("Сервер повернув помилку");
            }
            alert("✅ Резервне копіювання розпочато успішно!");
            startProgressAutoUpdate();
            return response.text();
        })
        .catch(error => alert("❌ Помилка запуску резервного копіювання: " + error));
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
    }, 4000);
}

function fetchAndShowProgress() {
    fetch('/api/progress')
        .then(response => {
            if (response.status === 204) {
                document.getElementById('progressContainer').innerHTML = 'No active progress';
                clearInterval(progressInterval);
                return;
            }
            return response.json();
        })
        .then(progress => {
            if (!progress) return;

            const progressHtml = `
        <div class="progress-bar">
            <div class="progress-fill" style="width: ${progress.percent}%; transition: width 1s;"></div>
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
