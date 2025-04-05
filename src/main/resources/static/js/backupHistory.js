function loadBackupHistory() {
    fetch("http://localhost:8080/req/backup-history")
        .then(response => response.json())
        .then(data => {
            const table = document.getElementById("backupTable");

            while (table.rows.length > 1) {
                table.deleteRow(1);
            }

            data.forEach(backup => {
                let row = table.insertRow(-1);
                let cell1 = row.insertCell(0);
                let cell2 = row.insertCell(1);
                let cell3 = row.insertCell(2);
                let cell4 = row.insertCell(3);
                let cell5 = row.insertCell(4);

                cell1.textContent = backup.status;
                cell2.textContent = new Date(backup.backup_time).toLocaleString();
                cell3.textContent = backup.database_name;
                cell4.textContent = backup.backup_location;
                cell5.textContent = backup.retention_period;
            });
        })
        .catch(error => console.error("Error fetching backup history:", error));
}

document.addEventListener("DOMContentLoaded", function () {
loadBackupHistory();
setInterval(loadBackupHistory, 10000);
});