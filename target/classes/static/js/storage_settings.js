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

        fetch('/api/storage-settings/save', {
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
