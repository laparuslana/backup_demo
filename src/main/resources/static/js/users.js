function addUser() {
    let username, email, password, role;

    while (!username) {
        username = prompt("Enter new username:");
        if (username === null) return;
    }

    while (!email || !email.includes("@")) {
        email = prompt("Enter user email:");
        if (email === null) return;
        if (!validateEmail(email)) alert("Invalid email! Please enter a valid email.");
    }

    while (!password || password.length < 6) {
        password = prompt("Enter user password (at least 6 characters):");
        if (password === null) return;
        if (!validatePassword(password)) alert("Password must be at least 6 characters long!");
    }

    while (true) {
        role = prompt("Enter role (USER or ADMIN):");
        if (role === null) return;
        role = role.toUpperCase();
        if (role === "USER" || role === "ADMIN") break;
        alert("Invalid role! Only USER or ADMIN allowed.");
    }

    fetch('/req/users/add', {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify({username, email, password, role})
    })
        .then(response => response.json())
        .then(data => alert(data.message))
        .catch(error => alert("Error: " + error));
}

function editUser() {
    let userId, newUsername, newEmail, newPassword, newRole;

    while (!userId) {
        userId = prompt("Enter User ID to edit:");
        if (userId === null) return;
    }

    while (!newUsername) {
        newUsername = prompt("Enter new username:");
        if (newUsername === null) return;
    }

    while (!newEmail || !newEmail.includes("@")) {
        newEmail = prompt("Enter new email:");
        if (newEmail === null) return;
        if (!newEmail.includes("@")) alert("Invalid email! Please enter a valid email.");
    }

    while (true) {
        newPassword = prompt("Enter new password (leave empty to keep current):");
        if (newPassword === null) return;
        if (newPassword === "") break;
        if (newPassword.length < 6) alert("Password must be at least 6 characters long!");
        else break;
    }

    while (true) {
        newRole = prompt("Enter new role (USER or ADMIN):");
        if (newRole === null) return;
        newRole = newRole.toUpperCase();
        if (newRole === "USER" || newRole === "ADMIN") break;
        alert("Invalid role! Only USER or ADMIN allowed.");
    }

    fetch(`/req/users/edit/${userId}`, {
        method: 'PUT',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify({username: newUsername, email: newEmail, password: newPassword, role: newRole})
    })
        .then(response => response.json())
        .then(data => alert(data.message))
        .catch(error => alert("Error: " + error));
}

function deleteUser() {
    let userId = prompt("Enter User ID to delete:");
    if (userId) {
        fetch(`/req/users/delete/${userId}`, {
            method: 'DELETE'
        }).then(response => response.json())
            .then(data => alert(data.message))
            .catch(error => alert("Error: " + error));
    }
}