// Function to fetch new data (replace this with an API call if needed)
async function fetchNewData() {
    const response = await fetch('/clima/random'); // API endpoint
    return response.json(); //
}

// Update the UI with the new data
function updateUI(data) {

    const [year, month] = data.time.split('-');
    
    // Convertir el número de mes a nombre (opcional)
    const monthNames = [
        "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
        "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
    ];
    const monthName = monthNames[parseInt(month) - 1];

    document.getElementById('month').textContent = monthName;
    document.getElementById('year').textContent = year;
    document.getElementById('tmp').textContent = data.temperatura;
    document.getElementById('pr').textContent = data.precipitacion;

    const tempElement = document.getElementById('tmp');
    const precElement = document.getElementById('pr');

    tempElement.className = 'value-display';
    precElement.className = 'value-display';

    // temperatura (°C)
    if (data.temperatura < 15) {
        tempElement.classList.add('low');        // Frío
    } else if (data.temperatura <= 20) {
        tempElement.classList.add('normal');     // Normal
    } else {
        tempElement.classList.add('high');       // Caliente
    }

    // precipitación (mm)
    if (data.precipitacion < 30) {
        precElement.classList.add('low');        // Arido
    } else if (data.precipitacion <= 55) {
        precElement.classList.add('normal');     // Templado
    } else {
        precElement.classList.add('high');       // Húmedo
    }
}

// Set up periodic refresh
async function refreshData() {
    const newData = await fetchNewData();
    updateUI(newData);
}

async function uploadCsv(event) {
    event.preventDefault();

    const fileInput = document.querySelector('input[type="file"]');
    if (!fileInput.files.length) {
        alert('Please select a CSV file to upload.');
        return;
    }

    const formData = new FormData();
    formData.append('file', fileInput.files[0]);

    try {
        const response = await fetch('/clima/upload', {
            method: 'POST',
            body: formData,
        });

        if (response.ok) {
            alert('CSV uploaded successfully!');
            fileInput.value = ''; // Reset file input
        } else {
            const errorText = await response.text();
            alert(`Error uploading CSV: ${errorText}`);
        }
    } catch (error) {
        alert(`Unexpected error: ${error.message}`);
    }
}

// Refresh interval
setInterval(refreshData, 60000);

// Manual refresh
document.getElementById('refresh-btn').addEventListener('click', refreshData);

// CSV upload form
// document.querySelector('form').addEventListener('submit', uploadCsv);

window.onload = refreshData; // Initial data load