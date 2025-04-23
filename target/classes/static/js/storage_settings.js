// document.addEventListener("DOMContentLoaded", async () => {
//     const form = document.getElementById("storageSettingsForm");
//
//     try {
//         const res = await fetch("/api/storage-settings/load");
//         if (res.ok) {
//             const data = await res.json();
//             if (data.local) {
//                 document.getElementById("localPath").value = data.local.backupLocation || "";
//             }
//             if (data.ftp) {
//                 document.getElementById("ftpHost").value = data.ftp.ftpServer || "";
//                 document.getElementById("ftpUser").value = data.ftp.ftpUser || "";
//                 document.getElementById("ftpPassword").value = data.ftp.ftpPassword || "";
//                 document.getElementById("ftpPath").value = data.ftp.ftpDirectory || "";
//             }
//         }
//     } catch (error) {
//         alert("⚠️ Could not load existing storage settings.");
//     }
//
//     form.addEventListener("submit", async (e) => {
//         e.preventDefault();
//
//         const payload = {
//             local: {
//                 backupLocation: document.getElementById("localPath").value
//             },
//             ftp: {
//                 ftpServer: document.getElementById("ftpHost").value,
//                 ftpUser: document.getElementById("ftpUser").value,
//                 ftpPassword: document.getElementById("ftpPassword").value,
//                 ftpDirectory: document.getElementById("ftpPath").value
//             }
//         };
//
//         try {
//             const res = await fetch("/api/storage-settings/save", {
//                 method: "POST",
//                 headers: { "Content-Type": "application/json" },
//                 body: JSON.stringify(payload)
//             });
//
//             if (!res.ok) throw new Error("Failed to save settings.");
//             alert("✅ Settings saved successfully.");
//         } catch (error) {
//             alert("❌ Error saving settings: " + error.message);
//         }
//     });
// });

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

        fetch('/api/storage-settings/savee', {
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
