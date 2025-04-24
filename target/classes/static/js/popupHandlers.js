function openPopup() {
    document.getElementById('backupPopup').style.display = 'flex';

    const dbServerInput = document.getElementById("dbServer");
    const dbUserInput = document.getElementById("dbUser");
    const dbPasswordInput = document.getElementById("dbPassword");

    if (dbServerInput && dbUserInput && dbPasswordInput) {
        const triggerLoad = () => {
            if (dbServerInput.value && dbUserInput.value && dbPasswordInput.value) {
                loadDatabases();
            }
        };

        dbServerInput.addEventListener("blur", triggerLoad);
        dbUserInput.addEventListener("blur", triggerLoad);
        dbPasswordInput.addEventListener("blur", triggerLoad);
    }
}

function closePopup() {
    document.getElementById('backupPopup').style.display = 'none';
}

function openAutoPopup() {
    document.getElementById('autoBackupPopup').style.display = 'flex';

    const dbServerInput2 = document.getElementById("dbServer2");
    const dbUserInput2 = document.getElementById("dbUser2");
    const dbPasswordInput2 = document.getElementById("dbPassword2");

    if (dbServerInput2 && dbUserInput2 && dbPasswordInput2) {
        const triggerLoad = () => {
            if (dbServerInput2.value && dbUserInput2.value && dbPasswordInput2.value) {
                loadAutoDatabases();
            }
        };

        dbServerInput2.addEventListener("blur", triggerLoad);
        dbUserInput2.addEventListener("blur", triggerLoad);
        dbPasswordInput2.addEventListener("blur", triggerLoad);
    }
}

function closeAutoPopup() {
    document.getElementById('autoBackupPopup').style.display = 'none';
}

function openRestorePopup() {
    document.getElementById('restorePopup').style.display = 'flex';

    const type = document.getElementById("res_storageType");
    const nameSelect = document.getElementById("res_nameSelect");

    if (type && nameSelect) {
        const triggerLoad = () => {
            if (type.value && nameSelect.value) {
                listFiles();
            }
        };

        type.addEventListener("blur", triggerLoad);
        nameSelect.addEventListener("blur", triggerLoad);
    }
}

function closeRestorePopup() {
    document.getElementById('restorePopup').style.display = 'none';
}


function openManageRestorePopup() {
    document.getElementById('manageRestorePopup').style.display = 'flex';

    const man_dbServer = document.getElementById("man_dbServer");
    const man_dbUser = document.getElementById("man_dbUser");
    const man_dbPassword = document.getElementById("man_dbPassword");

    if (man_dbServer && man_dbUser && man_dbPassword) {
        const triggerLoad = () => {
            if (man_dbServer.value && man_dbUser.value && man_dbPassword.value) {
                loadManDatabases();
            }
        };

        man_dbServer.addEventListener("blur", triggerLoad);
        man_dbUser.addEventListener("blur", triggerLoad);
        man_dbPassword.addEventListener("blur", triggerLoad);
    }

    const man_clusterAdmin = document.getElementById("man_clusterAdmin");
    const man_clusterUsername = document.getElementById("man_clusterUsername");
    const man_clusterPassword = document.getElementById("man_clusterPassword");

    if (man_clusterAdmin && man_clusterUsername && man_clusterPassword) {
        const triggerLoad = () => {
            if (man_clusterAdmin.value && man_clusterUsername.value && man_clusterPassword.value) {
                loadInfobases();
            }
        };

        man_clusterAdmin.addEventListener("blur", triggerLoad);
        man_clusterUsername.addEventListener("blur", triggerLoad);
        man_clusterPassword.addEventListener("blur", triggerLoad);
    }
}

function closeManageRestorePopup() {
    document.getElementById('manageRestorePopup').style.display = 'none';
}


function openFilePopup() {
    document.getElementById('fileBackupPopup').style.display = 'flex';
}

function closeFilePopup() {
    document.getElementById('fileBackupPopup').style.display = 'none';
}


function openFileRestorePopup() {
    document.getElementById('fileRestorePopup').style.display = 'flex';
}

function closeFileRestorePopup() {
    document.getElementById('fileRestorePopup').style.display = 'none';
}