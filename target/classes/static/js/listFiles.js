// document.addEventListener("DOMContentLoaded", () => {
//     const type = document.getElementById("res_storageType");
//     type.addEventListener("change", loadFiles);
//
//     function loadFiles() {
//             const storageType = type.value;
//             fetch(`/api/restore/list?type=${storageType}`)
//                 .then(response => response.json())
//                 .then(files => {
//                     const select = document.getElementById('backupFile');
//                     select.innerHTML = "";
//                     files.forEach(file => {
//                         const option = document.createElement('option');
//                         option.value = file;
//                         option.textContent = file;
//                         select.appendChild(option);
//                     });
//                 })
//                 .catch(err => console.error('Error fetching backups:', err));
//         }
// loadFiles();
// })