// Admin Panel JavaScript
const API_BASE = 'http://localhost:8080/api';

// ============================================
// INITIALIZATION
// ============================================
document.addEventListener('DOMContentLoaded', function() {
    loadServices();
    loadEmployees();
    setupEventListeners();
});

function setupEventListeners() {
    document.getElementById('service-form').addEventListener('submit', handleServiceSubmit);
    document.getElementById('employee-form').addEventListener('submit', handleEmployeeSubmit);
}

// ============================================
// SERVICES
// ============================================
async function loadServices() {
    try {
        const response = await fetch(`${API_BASE}/services`);
        const services = await response.json();

        document.getElementById('services-loading').style.display = 'none';
        document.getElementById('services-table').style.display = 'block';
        document.getElementById('services-count').textContent = services.length;

        const tbody = document.getElementById('services-tbody');
        tbody.innerHTML = services.map(service => `
            <tr>
                <td>${service.id}</td>
                <td><strong>${service.name}</strong></td>
                <td>${service.description || '-'}</td>
                <td>${service.price.toFixed(2)} DKK</td>
                <td>
                    <span class="badge ${service.active ? 'badge-active' : 'badge-inactive'}">
                        ${service.active ? 'Aktiv' : 'Inaktiv'}
                    </span>
                </td>
                <td>
                    <button class="btn btn-primary btn-small" onclick='editService(${JSON.stringify(service)})'>
                        Rediger
                    </button>
                    <button class="btn btn-danger btn-small" onclick="deleteService(${service.id}, '${service.name}')">
                        Slet
                    </button>
                </td>
            </tr>
        `).join('');
    } catch (error) {
        console.error('Error loading services:', error);
        showAlert('Kunne ikke indlæse services', 'error');
    }
}

function openServiceModal(service = null) {
    const modal = document.getElementById('service-modal');
    const title = document.getElementById('service-modal-title');
    const form = document.getElementById('service-form');

    form.reset();

    if (service) {
        title.textContent = 'Rediger Service';
        document.getElementById('service-id').value = service.id;
        document.getElementById('service-name').value = service.name;
        document.getElementById('service-description').value = service.description || '';
        document.getElementById('service-price').value = service.price;
        document.getElementById('service-active').value = service.active.toString();
    } else {
        title.textContent = 'Ny Service';
        document.getElementById('service-active').value = 'true';
    }

    modal.classList.add('active');
}

function closeServiceModal() {
    document.getElementById('service-modal').classList.remove('active');
}

function editService(service) {
    openServiceModal(service);
}

async function handleServiceSubmit(e) {
    e.preventDefault();

    const id = document.getElementById('service-id').value;
    const data = {
        name: document.getElementById('service-name').value,
        description: document.getElementById('service-description').value,
        price: parseFloat(document.getElementById('service-price').value),
        active: document.getElementById('service-active').value === 'true'
    };

    try {
        const url = id ? `${API_BASE}/services/${id}` : `${API_BASE}/services`;
        const method = id ? 'PUT' : 'POST';

        const response = await fetch(url, {
            method: method,
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        });

        if (!response.ok) throw new Error('Failed to save service');

        showAlert(id ? 'Service opdateret!' : 'Service oprettet!', 'success');
        closeServiceModal();
        loadServices();
    } catch (error) {
        console.error('Error saving service:', error);
        showAlert('Kunne ikke gemme service', 'error');
    }
}

async function deleteService(id, name) {
    if (!confirm(`Er du sikker på at du vil slette "${name}"?`)) return;

    try {
        const response = await fetch(`${API_BASE}/services/${id}`, {
            method: 'DELETE'
        });

        if (!response.ok) throw new Error('Failed to delete service');

        showAlert('Service slettet!', 'success');
        loadServices();
    } catch (error) {
        console.error('Error deleting service:', error);
        showAlert('Kunne ikke slette service', 'error');
    }
}

// ============================================
// EMPLOYEES
// ============================================
async function loadEmployees() {
    try {
        const response = await fetch(`${API_BASE}/employees`);
        const employees = await response.json();

        document.getElementById('employees-loading').style.display = 'none';
        document.getElementById('employees-table').style.display = 'block';
        document.getElementById('employees-count').textContent = employees.length;

        const tbody = document.getElementById('employees-tbody');
        tbody.innerHTML = employees.map(employee => `
            <tr>
                <td>${employee.id}</td>
                <td><strong>${employee.name}</strong></td>
                <td>${employee.position}</td>
                <td>${employee.email || '-'}</td>
                <td>${employee.phone || '-'}</td>
                <td>
                    <span class="badge ${employee.active ? 'badge-active' : 'badge-inactive'}">
                        ${employee.active ? 'Aktiv' : 'Inaktiv'}
                    </span>
                </td>
                <td>
                    <button class="btn btn-primary btn-small" onclick='editEmployee(${JSON.stringify(employee)})'>
                        Rediger
                    </button>
                    <button class="btn btn-danger btn-small" onclick="deleteEmployee(${employee.id}, '${employee.name}')">
                        Slet
                    </button>
                </td>
            </tr>
        `).join('');
    } catch (error) {
        console.error('Error loading employees:', error);
        showAlert('Kunne ikke indlæse medarbejdere', 'error');
    }
}

function openEmployeeModal(employee = null) {
    const modal = document.getElementById('employee-modal');
    const title = document.getElementById('employee-modal-title');
    const form = document.getElementById('employee-form');

    form.reset();

    if (employee) {
        title.textContent = 'Rediger Medarbejder';
        document.getElementById('employee-id').value = employee.id;
        document.getElementById('employee-name').value = employee.name;
        document.getElementById('employee-position').value = employee.position;
        document.getElementById('employee-email').value = employee.email || '';
        document.getElementById('employee-phone').value = employee.phone || '';
        document.getElementById('employee-imageUrl').value = employee.imageUrl || '';
        document.getElementById('employee-active').value = employee.active.toString();
    } else {
        title.textContent = 'Ny Medarbejder';
        document.getElementById('employee-active').value = 'true';
    }

    modal.classList.add('active');
}

function closeEmployeeModal() {
    document.getElementById('employee-modal').classList.remove('active');
}

function editEmployee(employee) {
    openEmployeeModal(employee);
}

async function handleEmployeeSubmit(e) {
    e.preventDefault();

    const id = document.getElementById('employee-id').value;
    const data = {
        name: document.getElementById('employee-name').value,
        position: document.getElementById('employee-position').value,
        email: document.getElementById('employee-email').value || null,
        phone: document.getElementById('employee-phone').value || null,
        imageUrl: document.getElementById('employee-imageUrl').value || null,
        active: document.getElementById('employee-active').value === 'true'
    };

    try {
        const url = id ? `${API_BASE}/employees/${id}` : `${API_BASE}/employees`;
        const method = id ? 'PUT' : 'POST';

        const response = await fetch(url, {
            method: method,
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        });

        if (!response.ok) throw new Error('Failed to save employee');

        showAlert(id ? 'Medarbejder opdateret!' : 'Medarbejder oprettet!', 'success');
        closeEmployeeModal();
        loadEmployees();
    } catch (error) {
        console.error('Error saving employee:', error);
        showAlert('Kunne ikke gemme medarbejder', 'error');
    }
}

async function deleteEmployee(id, name) {
    if (!confirm(`Er du sikker på at du vil slette "${name}"?`)) return;

    try {
        const response = await fetch(`${API_BASE}/employees/${id}`, {
            method: 'DELETE'
        });

        if (!response.ok) throw new Error('Failed to delete employee');

        showAlert('Medarbejder slettet!', 'success');
        loadEmployees();
    } catch (error) {
        console.error('Error deleting employee:', error);
        showAlert('Kunne ikke slette medarbejder', 'error');
    }
}

// ============================================
// UTILITY FUNCTIONS
// ============================================
function showAlert(message, type) {
    const container = document.getElementById('alert-container');
    const alert = document.createElement('div');
    alert.className = `alert alert-${type}`;
    alert.textContent = message;
    container.appendChild(alert);

    setTimeout(() => {
        alert.remove();
    }, 5000);
}