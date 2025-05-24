function loadAllUsers() {
    fetch('req/users/all')
        .then(response => response.json())
        .then(users => {
            const tableBody = document.getElementById('usersTable').getElementsByTagName('tbody')[0];
            users.forEach(user => {
                const row = tableBody.insertRow();
                row.insertCell(0).textContent = user.username;
                row.insertCell(1).textContent = user.role;
            });
        });
}

function addUser() {
    let username, email, password, role;

    while (!username) {
        username = prompt("Введіть нове ім'я користувача:");
        if (username === null) return;
    }

    while (!email || !email.includes("@")) {
        email = prompt("Введіть адресу електронної пошти користувача:");
        if (email === null) return;
        if (!validateEmail(email)) alert("Недійсна електронна адреса! Будь ласка, введіть дійсну електронну адресу.");
    }

    while (!password || password.length < 6) {
        password = prompt("Введіть пароль користувача (принаймні 6 символів):");
        if (password === null) return;
        if (!validatePassword(password)) alert("Пароль має містити щонайменше 6 символів!");
    }

    while (true) {
        role = prompt("Введіть роль (USER or ADMIN):");
        if (role === null) return;
        role = role.toUpperCase();
        if (role === "USER" || role === "ADMIN") break;
        alert("Недійсна роль! Тільки USER або ADMIN дозволено.");
    }

    fetch('/req/users/add', {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify({username, email, password, role})
    })
        .then(response => response.json())
        .then(data => {
            alert(data.message);
            loadAllUsers();
        })
        .catch(error => alert("Помилка: " + error));
}

function editUser() {
    let userName, newUsername, newEmail, newPassword, newRole;

    while (!userName) {
        userName = prompt("Введіть ім'я користувача для редагування:");
        if (userName === null) return;
    }

    while (!newUsername) {
        newUsername = prompt("Введіть нове ім'я користувача:");
        if (newUsername === null) return;
    }

    while (!newEmail || !newEmail.includes("@")) {
        newEmail = prompt("Введіть новий email:");
        if (newEmail === null) return;
        if (!newEmail.includes("@")) alert("Недійсна електронна адреса! Будь ласка, введіть дійсну електронну адресу.");
    }

    while (true) {
        newPassword = prompt("Введіть новий пароль (залиште порожнім, щоб зберегти актуальний):");
        if (newPassword === null) return;
        if (newPassword === "") break;
        if (newPassword.length < 6) alert("Пароль має містити щонайменше 6 символів!");
        else break;
    }

    while (true) {
        newRole = prompt("Введіть нову роль (USER or ADMIN):");
        if (newRole === null) return;
        newRole = newRole.toUpperCase();
        if (newRole === "USER" || newRole === "ADMIN") break;
        alert("Недійсна роль! Тільки USER або ADMIN дозволено.");
    }

    fetch(`/req/users/edit/${userName}`, {
        method: 'PUT',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify({username: newUsername, email: newEmail, password: newPassword, role: newRole})
    })
        .then(response => response.json())
        .then(data => {
            alert(data.message);
            loadAllUsers();
        })
        .catch(error => alert("Помилка: " + error));
}

function deleteUser() {
    let userName = prompt("Введіть ім'я користувача для видалення:");
    if (userName) {
        fetch(`/req/users/delete/${userName}`, {
            method: 'DELETE'
        }).then(response => response.json())
            .then(data => {
                alert(data.message);
                loadAllUsers();
            })
            .catch(error => alert("Помилка: " + error));
    }
}