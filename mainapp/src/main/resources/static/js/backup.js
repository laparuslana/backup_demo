
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
                return response.json();
            })
        .then(data => {
            alert("✅ " + data.message);
            startProgressAutoUpdate();

        })
        .catch(error => alert("❌ Помилка запуску резервного копіювання: " + error));
});
let progressInterval;
let smoothProgressInterval;
let currentDisplayProgress = 0;
let targetProgress = 0;
let isProgressActive = false;

function startProgressAutoUpdate() {
    currentDisplayProgress = 0;
    targetProgress = 0;
    isProgressActive = true;

    if (progressInterval) clearInterval(progressInterval);
    if (smoothProgressInterval) clearInterval(smoothProgressInterval);

    progressInterval = setInterval(() => {
        fetchAndShowProgress();
    }, 2000);

    smoothProgressInterval = setInterval(() => {
        updateSmoothProgress();
    }, 100);

    fetchAndShowProgress();
}

function updateSmoothProgress() {
    if (!isProgressActive) return;

    if (currentDisplayProgress < targetProgress) {
        currentDisplayProgress = Math.min(targetProgress, currentDisplayProgress + 0.5);
        updateProgressDisplay();
    } else if (currentDisplayProgress > targetProgress) {
        currentDisplayProgress = Math.max(targetProgress, currentDisplayProgress - 0.5);
        updateProgressDisplay();
    }
}

function updateProgressDisplay() {
    const progressBar = document.querySelector('.progress-fill');
    const progressText = document.querySelector('.progress-text');

    if (progressBar) {
        progressBar.style.width = `${currentDisplayProgress}%`;
    }
    if (progressText) {
        progressText.textContent = `${Math.round(currentDisplayProgress)}%`;
    }
}

function fetchAndShowProgress() {
    fetch('/api/progress')
        .then(response => {
            if (response.status === 204) {
                stopProgressTracking('Немає активного прогресу');
                return null;
            }
            return response.json();
        })
        .then(progress => {
            if (!progress) return;

            targetProgress = progress.percent;

            const progressHtml = `
                <div class="progress-container">
                    <div class="progress-bar">
                        <div class="progress-fill" style="width: ${currentDisplayProgress}%; transition: none;"></div>
                    </div>
                    <div class="progress-info">
                        <span class="progress-text">${Math.round(currentDisplayProgress)}%</span>
                        <span class="progress-status">${progress.status}</span>
                    </div>
                    <div class="progress-details">
                        <small>База даних: ${progress.databaseName}</small>
                        <small>Розташування: ${progress.backupLocation}</small>
                    </div>
                    <div class="progress-timestamps">
                        <small>Початок: ${progress.startTime}</small>
                        <small>Оновлено: ${progress.updateTime}</small>
                    </div>
                </div>`;

            document.getElementById('progressContainer').innerHTML = progressHtml;

            if (progress.percent >= 100) {
                setTimeout(() => {
                    stopProgressTracking('Резервне копіювання завершено!');
                }, 2000);
            }
        })
        .catch(error => {
            console.error('Progress fetch error:', error);
            stopProgressTracking('Помилка отримання прогресу');
        });
}

function stopProgressTracking(message) {
    isProgressActive = false;

    if (progressInterval) {
        clearInterval(progressInterval);
        progressInterval = null;
    }

    if (smoothProgressInterval) {
        clearInterval(smoothProgressInterval);
        smoothProgressInterval = null;
    }

    if (message) {
        document.getElementById('progressContainer').innerHTML = `
            <div class="progress-message">
                <p>${message}</p>
            </div>`;
    }
}

// let progressInterval;
// let simulatedProgress = 0;
// let realProgress = 0;
//
// function startProgressAutoUpdate() {
//     simulatedProgress = 0;
//     realProgress = 0;
//     if (progressInterval) clearInterval(progressInterval);
//
//     progressInterval = setInterval(() => {
//         fetchAndShowProgress();
//     }, 5000);
// }
//
// function fetchAndShowProgress() {
//     fetch('/api/progress')
//         .then(response => {
//             if (response.status === 204) {
//                 document.getElementById('progressContainer').innerHTML = 'Немає активного прогресу';
//                 clearInterval(progressInterval);
//                 return;
//             }
//             return response.json();
//         })
//         .then(progress => {
//             if (!progress) return;
//
//             const progressHtml = `
//         <div class="progress-bar">
//             <div class="progress-fill" style="width: ${progress.percent}%; transition: width 1s;"></div>
//         </div>
//         <p>${progress.percent}% ${progress.status}</p>
//         <small>База даних: ${progress.databaseName} | Розташування: ${progress.backupLocation}</small>
//         <small>Початок: ${progress.startTime} | Оновлено: ${progress.updateTime}</small>
//     `;
//             document.getElementById('progressContainer').innerHTML = progressHtml;
//
//             realProgress = progress.percent;
//
//             if (realProgress >= 100) {
//                 clearInterval(progressInterval);
//             }
//         })
//         .catch(error => {
//             document.getElementById('progressContainer').innerHTML = 'Немає активного прогресу';
//             clearInterval(progressInterval);
//         });
// }
