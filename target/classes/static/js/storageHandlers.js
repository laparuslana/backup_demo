
const storageTypeSelect = document.getElementById("storageType");
const localPathContainer = document.getElementById("localPathContainer");
const ftpContainer = document.getElementById("ftpContainer");

storageTypeSelect.addEventListener("change", () => {
    const selectedType = storageTypeSelect.value;

    localPathContainer.style.display = selectedType === "local" ? "block" : "none";
    ftpContainer.style.display = selectedType === "ftp" ? "block" : "none";

});

const storageTypeSelect2 = document.getElementById("storageType2");
const localPathContainer2 = document.getElementById("localPathContainer2");
const ftpContainer2 = document.getElementById("ftpContainer2");

storageTypeSelect2.addEventListener("change", () => {
    const selectedType2 = storageTypeSelect2.value;

    localPathContainer2.style.display = selectedType2 === "LOCAL" ? "block" : "none";
    ftpContainer2.style.display = selectedType2 === "FTP" ? "block" : "none";

});