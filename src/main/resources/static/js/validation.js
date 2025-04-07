function validateEmail(email) {
    return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);
}
function validatePassword(password) {
    return password.length >= 6;
}
function validateDbName(dbName) {
    if (!dbName.startsWith('test_')) {
        alert("❗ Test DB Name should start with 'test_'");
    return false;
    }
    return true;
}