function loadBackupHistory() {
    fetch("http://localhost:8080/req/history/backup")
        .then(response => response.json())
        .then(data => {

            const table = document.getElementById("backupTable");

            const tbody = table.querySelector("tbody");
            tbody.innerHTML = "";

            data.forEach(backup => {
                const row = document.createElement("tr");
                row.innerHTML = `
                    <td>${backup.status}</td>
                    <td>${new Date(backup.backup_time).toLocaleString()}</td>
                    <td>${backup.database_name}</td>
                    <td>${backup.backup_location}</td>
                    <td>${backup.retention_period}</td>
                    <td>${backup.username}</td>
                `;
                tbody.appendChild(row);
            });

            if ($.fn.DataTable.isDataTable('#backupTable')) {
                $('#backupTable').DataTable().clear.destroy();
            }

            $('#backupTable').DataTable();
        })
        .catch(error => console.error("Error fetching backup history:", error));
}

function loadRestorePreviewHistory() {
    fetch("http://localhost:8080/req/history/restore")
        .then(response => response.json())
        .then(data => {

            const table = document.getElementById("restorePreviewTable");

            const tbody = table.querySelector("tbody");
            tbody.innerHTML = "";

            data.slice(0, 3).forEach(restore => {
                const row = document.createElement("tr");
                row.innerHTML = `
                    <td>${restore.status}</td>
                    <td>${new Date(restore.restore_time).toLocaleString()}</td>
                    <td>${restore.backup_file}</td>
                `;
                tbody.appendChild(row);
            });

            if ($.fn.DataTable.isDataTable('#restorePreviewTable')) {
                $('#restorePreviewTable').DataTable().clear.destroy();
            }

            $('#restorePreviewTable').DataTable();
        })
        .catch(error => console.error("Error fetching restore history:", error));
}

function loadBackupPreviewHistory() {
    fetch("http://localhost:8080/req/history/backup")
        .then(response => response.json())
        .then(data => {

            const table = document.getElementById("backupPreviewTable");

            const tbody = table.querySelector("tbody");
            tbody.innerHTML = "";

            data.slice(0, 3).forEach(backup => {
                const row = document.createElement("tr");
                row.innerHTML = `
                    <td>${backup.status}</td>
                    <td>${new Date(backup.backup_time).toLocaleString()}</td>
                    <td>${backup.database_name}</td>
                `;
                tbody.appendChild(row);
            });

            if ($.fn.DataTable.isDataTable('#backupPreviewTable')) {
                $('#backupPreviewTable').DataTable().clear.destroy();
            }

            $('#backupPreviewTable').DataTable();
        })
        .catch(error => console.error("Error fetching backup history:", error));
}

function loadRestoreHistory() {
    fetch("http://localhost:8080/req/history/restore")
        .then(response => response.json())
        .then(data => {

            const table = document.getElementById("restoreTable");

            const tbody = table.querySelector("tbody");
            tbody.innerHTML = "";

            data.forEach(restore => {
                const row = document.createElement("tr");
                row.innerHTML = `
                    <td>${restore.status}</td>
                    <td>${new Date(restore.restore_time).toLocaleString()}</td>
                    <td>${restore.backup_file}</td>
                    <td>${restore.source_database}</td>
                    <td>${restore.username}</td>
                `;
                tbody.appendChild(row);
            });

                if ($.fn.DataTable.isDataTable('#restoreTable')) {
                    $('#restoreTable').DataTable().clear.destroy();
                }

                $('#restoreTable').DataTable();
        })
        .catch(error => console.error("Error fetching restore history:", error));
}
