function validateEmail(email) {
    return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);
}
function validatePassword(password) {
    return password.length >= 6;
}
function validateDbName(dbName) {
    if (!dbName.startsWith('test_')) {
        return alert("❗ Имя тестовой базы должно начинаться с 'test_'");
    }
}