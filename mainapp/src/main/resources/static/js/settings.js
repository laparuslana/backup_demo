function saveStorageForm() {
    event.preventDefault();
    const form = document.getElementById("storageSettingsForm");


        const formData = new FormData(form);
        const name = formData.get('name');
        const type = formData.get('type');

        const jsonParams = {};
        for (let [key, value] of formData.entries()) {
            if (key !== 'name' && key !== 'type') {
                jsonParams[key] = value;
            }
        }

        const payload = {
            name, type, jsonParameters: jsonParams
        };

        fetch('/api/settings/storage-settings-save', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(payload)
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error("Server returned error");
                }
            return response.text();
})
            .then(data => alert("✅ Baf settings saved!"))
    .catch(error => alert("❌ Error saving settings: " + error));
}


function saveBafSettings() {
    event.preventDefault();

    let bafSettings = {
        bafType: document.querySelector('input[name="bafType"]:checked')?.value,
        bafPath: document.getElementById("bafPath").value
    };

    fetch("/api/settings/baf-settings-save", {
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

function loadActiveSettings() {
    return fetch('/api/settings/get-baf-settings')
        .then(res => {
            if (!res.ok) throw new Error('Failed to fetch BAF settings');
            return res.json();
        })
        .then(data => {
            currentBafPath=data.bafType || '';
            document.getElementById("bafTypeValue").textContent = data.bafType || '';
            document.getElementById("bafPathValue").textContent = data.bafPath || '';
        })
        .catch(err => console.error('Error loading BAF settings:', err));
}
