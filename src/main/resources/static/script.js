document.getElementById('uploadForm').addEventListener('submit', function(event) {
    event.preventDefault();
    
    var formData = new FormData();
    var fileInput = document.getElementById('file');
    var title = document.getElementById('title').value
    var description = document.getElementById('description').value
    
    formData.append('title', title);
    formData.append('description', description);
    formData.append('file', fileInput.files[0]);

    fetch(`/tasks/upload`, {
        method: 'POST',
        body: formData
    })
    .then(response => response.json())
    .then(data => {
        alert('Archivo subido exitosamente. URL del archivo: ' + data.filePath);
        loadTasks();
    })
    .catch(error => {
        console.error('Error al subir el archivo:', error);
        alert('Hubo un error al subir el archivo.');
    });
});

function loadTasks() {
    fetch('/tasks')
        .then(response => response.json())
        .then(data => {
            var taskList = document.getElementById('taskList');
            taskList.innerHTML = ''; 

            data.forEach(task => {
                var taskElement = document.createElement('div');
                taskElement.innerHTML = `
                    <p><strong>Título:</strong> ${task.title}</p>
                    <p><strong>Descripción:</strong> ${task.description}</p>
                    <p><strong>Archivo:</strong> <a href="${task.filePath}" target="_blank">Ver archivo</a></p>
                    <hr>
                `;
                taskList.appendChild(taskElement);
            });
        })
        .catch(error => {
            console.error('Error al cargar las tareas:', error);
        });
}

window.onload = loadTasks;