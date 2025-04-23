function validateEmail(email) {
    return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);
}
function validatePassword(password) {
    return password.length >= 6;
}
function validateDbName(dbName) {
    if (!dbName.startsWith('test_')) {
        alert("❗ Test DB Name should start with 'test_'");
    return false;
    }
    return true;
}

function saveBafSettings() {
event.preventDefault();

    let bafSettings = {
        bafType: document.querySelector('input[name="bafType"]:checked')?.value,
        bafPath: document.getElementById("bafPath").value
    };

    fetch("/api/storage-settings/baf", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(bafSettings)
    })
        .then(response => {
            if (!response.ok) {
                throw new Error("Server returned error");
            }
            return response.text();
        })
        .then(data => alert("✅ Storage settings saved!"))
        .catch(error => alert("❌ Error saving settings: " + error));

}

function getActivityStats() {
    fetch('/api/activity-stats')
        .then(res => res.json())
        .then(data => {
            const labels = data.map(d => d.date);
            const backupCounts = data.map(d => d.backupCount);
            const restoreCounts = data.map(d => d.restoreCount);

            new Chart(document.getElementById('activityChart'), {
                type: 'line',
                data: {
                    labels,
                    datasets: [
                        {
                            label: 'Backups',
                            data: backupCounts,
                            borderColor: 'blue',
                            fill: false
                        },
                        {
                            label: 'Restores',
                            data: restoreCounts,
                            borderColor: 'green',
                            fill: false
                        }
                    ]
                }
            });
        });
}


let currentBafPath= '';

function loadBafSettings() {
    return fetch('/api/storage-settings/get-baf-settings')
        .then(res => {
            if (!res.ok) throw new Error('Failed to fetch BAF settings');
            return res.json();
        })
        .then(data => {
            if (data.bafType) {
                currentBafPath=data.bafType;
                const radio = document.querySelector(`input[name="bafType"][value="${data.bafType}"]`);
                if (radio) radio.checked = true;
            }

            currentBafPath=data.bafPath || '';
            const pathInput = document.querySelector('#bafPath');
            if (pathInput) pathInput.value = currentBafPath;
        })
        .catch(err => console.error('Error loading BAF settings:', err));
}


function loadActiveSettings() {
    return fetch('/api/storage-settings/get-baf-settings')
        .then(res => {
            if (!res.ok) throw new Error('Failed to fetch BAF settings');
            return res.json();
        })
        .then(data => {
            document.getElementById("bafTypeValue").textContent = data.bafType || '';
            document.getElementById("bafPathValue").textContent = data.bafPath || '';
        })
        .catch(err => console.error('Error loading BAF settings:', err));
}
