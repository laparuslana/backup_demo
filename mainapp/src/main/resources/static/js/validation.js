function validateEmail(email) {
    return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);
}
function validatePassword(password) {
    return password.length >= 6;
}
function validateDbName(dbName) {
    if (!dbName.startsWith('test_')) {
        alert("❗ Назва тестової бази даних має починатися з 'test_'");
    return false;
    }
    return true;
}
