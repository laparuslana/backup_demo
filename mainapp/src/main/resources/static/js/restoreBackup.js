document.getElementById("res_clusterAdmin").addEventListener("change", function () {
    const clusterCredentials = document.getElementById("res_clusterCredentials");
    clusterCredentials.style.display = this.checked ? "block" : "none";
});

const submitButom = document.getElementById("submitRestore");
submitButom.addEventListener('click', async(event) => {
    event.preventDefault();

    let restoreData = {
        testDbName: document.getElementById("testDbName").value,
        restoreDbServer: document.getElementById("restoreDbServer").value,
        restoreDbUser: document.getElementById("restoreDbUser").value,
        restoreDbPassword: document.getElementById("restoreDbPassword").value,
        res_storageType: document.getElementById("res_storageType").value,
        backupFile: document.getElementById("backupFile").value,
        res_clusterAdmin: document.getElementById("res_clusterAdmin").checked,
        res_nameSelect: document.getElementById("res_nameSelect").value
    };

    if (restoreData.res_clusterAdmin) {
        restoreData.res_clusterUsername = document.getElementById("res_clusterUsername").value;
        restoreData.res_clusterPassword = document.getElementById("res_clusterPassword").value;
    }

    const checkDbExistsUrl = `/api/backup/listDatabases?dbServer=${encodeURIComponent(restoreData.restoreDbServer)}&dbUser=${encodeURIComponent(restoreData.restoreDbUser)}&dbPassword=${encodeURIComponent(restoreData.restoreDbPassword)}`;

    try {
        const response = await fetch(checkDbExistsUrl);
        if (!response.ok) {
            throw new Error("Не вдалося перевірити бази даних");
        }

        const databases = await response.json();
        if (databases.includes(restoreData.testDbName)) {
            alert(`❌ Database "${restoreData.testDbName}" вже існує на сервері.`);
            return;
        }
    } catch (err) {
        alert(`❌ Помилка перевірки існування бази даних:: ${err.message}`);
        return;
    }
    if (!validateDbName(restoreData.testDbName)) {
        return;
    }

    fetch("/api/restore/backup", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(restoreData)
    })
        .then(response => {
            if (!response.ok) {
                throw new Error("Сервер повернув помилку");
            }
            return response.text();
        })
        .then(data => alert("✅ Відновлення розпочато успішно!"))
        .catch(error => alert("❌ Помилка запуску відновлення: " + error));
});



document.getElementById("res_clusterAdmin").addEventListener("change", function () {
    const clusterCredentials = document.getElementById("res_clusterCredentials");
    clusterCredentials.style.display = this.checked ? "block" : "none";
});

const submitRestore = document.getElementById("submitFileRestore");
submitRestore.addEventListener('click', async(event) => {
    event.preventDefault();

    const restorePath = document.getElementById("restorePath").value;
    const restoreFile = document.getElementById("restoreFile").value;
    const nameSelect = document.getElementById("restore_nameSelect").value;

    fetch(`/api/restore/file?restorePath=${encodeURIComponent(restorePath)}&restoreFile=${encodeURIComponent(restoreFile)}&name=${encodeURIComponent(nameSelect)}`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        }
    })
        .then(response => {
            if (!response.ok) {
                throw new Error("Сервер повернув помилку");
            }
            alert("✅ Відновлення розпочато успішно!");
            return response.text();
        })
        .catch(error => alert("❌ Помилка запуску відновлення: " + error));
});

const submitButton = document.getElementById("submitDelete");
submitButton.addEventListener('click', (event) => {
    event.preventDefault();

    const man_DbServer = document.getElementById("man_dbServer").value;
    const man_DbUser = document.getElementById("man_dbUser").value;
    const man_DbPassword = document.getElementById("man_dbPassword").value;
    const selectTestDb = document.getElementById("selectTestDb").value;
    const confirmAction = document.getElementById("confirmAction").checked;

    if (!confirmAction) {
        alert("❗ Будь ласка, підтвердіть дію перед видаленням.");
        return;
    }

    fetch(`/api/restore/manage?testDb=${encodeURIComponent(selectTestDb)}&server=${encodeURIComponent(man_DbServer)}&user=${encodeURIComponent(man_DbUser)}&password=${encodeURIComponent(man_DbPassword)}`,
        {
            method: "POST",
            headers: {
                "Accept": "application/json"
            }
    })
        .then(response => {
            if (!response.ok) {
                throw new Error("Сервер повернув помилку");
            }
            alert("✅ Видалення розпочато успішно!")
            return response.json();
        })
        .then(data => {
            alert(data.message);
        })
        .catch(error => alert("❌ Помилка запуску видалення: " + error));
});

document.getElementById("man_clusterAdmin").addEventListener("change", function () {
    const clusterCredentials = document.getElementById("man_clusterCredentials");
    clusterCredentials.style.display = this.checked ? "block" : "none";
});

const submitButon = document.getElementById("submitSwitch");
submitButon.addEventListener('click', (event) => {
    event.preventDefault();

    const man_clusterAdmin = document.getElementById("man_clusterAdmin").checked;
    const sourceDbName = document.getElementById("sourceDbName").value;
    const confirmAction = document.getElementById("confirmAction").checked;

    let man_clusterUsername= "";
    let man_clusterPassword = "";

    if (man_clusterAdmin) {
        man_clusterUsername = document.getElementById("man_clusterUsername").value;
        man_clusterPassword = document.getElementById("man_clusterPassword").value;
    }

    if (!confirmAction) {
        alert("❗ Будь ласка, підтвердіть дію.");
        return;
    }

    fetch(`/api/restore/switch?clusterAd=${encodeURIComponent(man_clusterAdmin)}&clusterUser=${encodeURIComponent(man_clusterUsername)}&clusterPass=${encodeURIComponent(man_clusterPassword)}&infobase=${encodeURIComponent(sourceDbName)}`,
        {
            method: "POST",
            headers: {
                "Accept": "application/json"
            }
        })
        .then(response => {
            if (!response.ok) {
                throw new Error("Сервер повернув помилку");
            }
            alert("✅ Перемикання розпочато успішно!")
            return response.json();
        })
        .then(data => {
            alert(data.message);
})
        .catch(error => alert("❌ Помилка початку перемикання: " + error));
});



