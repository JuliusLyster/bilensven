// ============================================
// API CONFIGURATION
// ============================================

const API_BASE_URL = 'http://localhost:8080/api';

// ============================================
// DASHBOARD
// ============================================

async function loadDashboard() {
    try {
        const [services, employees] = await Promise.all([
            fetch(`${API_BASE_URL}/services`).then(r => r.json()),
            fetch(`${API_BASE_URL}/employees`).then(r => r.json())
        ]);

        document.getElementById('services-count').textContent = services.length;
        document.getElementById('employees-count').textContent = employees.length;

    } catch (error) {
        console.error('Error loading dashboard:', error);
    }
}

// ============================================
// SERVICES MANAGEMENT
// ============================================

let servicesData = [];

async function loadServices() {
    try {
        const loadingEl = document.getElementById('services-loading');
        const tableEl = document.getElementById('services-table');

        if (loadingEl) loadingEl.style.display = 'block';
        if (tableEl) tableEl.style.display = 'none';

        const response = await fetch(`${API_BASE_URL}/services`);
        servicesData = await response.json();

        if (loadingEl) loadingEl.style.display = 'none';
        if (tableEl) tableEl.style.display = 'table';

        renderServicesTable();
    } catch (error) {
        console.error('Error loading services:', error);
        const loadingEl = document.getElementById('services-loading');
        if (loadingEl) loadingEl.style.display = 'none';
        showAlert('Kunne ikke hente services', 'error');
    }
}

function renderServicesTable() {
    const tbody = document.querySelector('#services-table tbody');
    if (!tbody) return;

    tbody.innerHTML = servicesData.map(service => `
        <tr>
            <td>${service.id}</td>
            <td>${escapeHtml(service.name)}</td>
            <td>${escapeHtml(service.description)}</td>
            <td>${service.price.toFixed(2)} DKK</td>
            <td><span class="badge ${service.active ? 'badge-success' : 'badge-danger'}">${service.active ? 'Aktiv' : 'Inaktiv'}</span></td>
            <td>
                <button class="btn-sm btn-primary" onclick="editService(${service.id})">Rediger</button>
                <button class="btn-sm btn-danger" onclick="deleteService(${service.id})">Slet</button>
            </td>
        </tr>
    `).join('');
}

function openServiceModal(serviceId = null) {
    const modal = document.getElementById('service-modal');
    const form = document.getElementById('service-form');
    const title = document.getElementById('service-modal-title');

    if (serviceId) {
        const service = servicesData.find(s => s.id === serviceId);
        title.textContent = 'Rediger Service';
        document.getElementById('service-id').value = service.id;
        document.getElementById('service-name').value = service.name;
        document.getElementById('service-description').value = service.description;
        document.getElementById('service-price').value = service.price;
        document.getElementById('service-image-url').value = service.imageUrl || '';
        document.getElementById('service-active').checked = service.active;
    } else {
        title.textContent = 'Ny Service';
        form.reset();
        document.getElementById('service-id').value = '';
    }

    modal.style.display = 'block';
}

function closeServiceModal() {
    document.getElementById('service-modal').style.display = 'none';
}

async function saveService(event) {
    event.preventDefault();

    const id = document.getElementById('service-id').value;
    const data = {
        name: document.getElementById('service-name').value,
        description: document.getElementById('service-description').value,
        price: parseFloat(document.getElementById('service-price').value),
        imageUrl: document.getElementById('service-image-url').value,
        active: document.getElementById('service-active').checked
    };

    try {
        const url = id ? `${API_BASE_URL}/services/${id}` : `${API_BASE_URL}/services`;
        const method = id ? 'PUT' : 'POST';

        const response = await fetch(url, {
            method: method,
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        });

        if (!response.ok) throw new Error('Failed to save service');

        showAlert(id ? 'Service opdateret' : 'Service oprettet', 'success');
        closeServiceModal();
        await loadServices();
        await loadDashboard();
    } catch (error) {
        console.error('Error saving service:', error);
        showAlert('Kunne ikke gemme service', 'error');
    }
}

function editService(id) {
    openServiceModal(id);
}

async function deleteService(id) {
    if (!confirm('Er du sikker på at du vil slette denne service?')) return;

    try {
        const response = await fetch(`${API_BASE_URL}/services/${id}`, {
            method: 'DELETE'
        });

        if (!response.ok) throw new Error('Failed to delete service');

        showAlert('Service slettet', 'success');
        await loadServices();
        await loadDashboard();
    } catch (error) {
        console.error('Error deleting service:', error);
        showAlert('Kunne ikke slette service', 'error');
    }
}

// ============================================
// EMPLOYEES MANAGEMENT
// ============================================

let employeesData = [];

async function loadEmployees() {
    try {
        const loadingEl = document.getElementById('employees-loading');
        const tableEl = document.getElementById('employees-table');

        if (loadingEl) loadingEl.style.display = 'block';
        if (tableEl) tableEl.style.display = 'none';

        const response = await fetch(`${API_BASE_URL}/employees`);
        employeesData = await response.json();

        if (loadingEl) loadingEl.style.display = 'none';
        if (tableEl) tableEl.style.display = 'table';

        renderEmployeesTable();
    } catch (error) {
        console.error('Error loading employees:', error);
        const loadingEl = document.getElementById('employees-loading');
        if (loadingEl) loadingEl.style.display = 'none';
        showAlert('Kunne ikke hente medarbejdere', 'error');
    }
}

function renderEmployeesTable() {
    const tbody = document.querySelector('#employees-table tbody');
    if (!tbody) return;

    tbody.innerHTML = employeesData.map(employee => `
        <tr>
            <td>${employee.id}</td>
            <td>${escapeHtml(employee.name)}</td>
            <td>${escapeHtml(employee.position)}</td>
            <td>${escapeHtml(employee.email || '-')}</td>
            <td>${escapeHtml(employee.phone || '-')}</td>
            <td><span class="badge ${employee.active ? 'badge-success' : 'badge-danger'}">${employee.active ? 'Aktiv' : 'Inaktiv'}</span></td>
            <td>
                <button class="btn-sm btn-primary" onclick="editEmployee(${employee.id})">Rediger</button>
                <button class="btn-sm btn-danger" onclick="deleteEmployee(${employee.id})">Slet</button>
            </td>
        </tr>
    `).join('');
}

function openEmployeeModal(employeeId = null) {
    const modal = document.getElementById('employee-modal');
    const form = document.getElementById('employee-form');
    const title = document.getElementById('employee-modal-title');

    if (employeeId) {
        const employee = employeesData.find(e => e.id === employeeId);
        title.textContent = 'Rediger Medarbejder';
        document.getElementById('employee-id').value = employee.id;
        document.getElementById('employee-name').value = employee.name;
        document.getElementById('employee-position').value = employee.position;
        document.getElementById('employee-email').value = employee.email || '';
        document.getElementById('employee-phone').value = employee.phone || '';
        document.getElementById('employee-image-url').value = employee.imageUrl || '';
        document.getElementById('employee-active').checked = employee.active;
    } else {
        title.textContent = 'Ny Medarbejder';
        form.reset();
        document.getElementById('employee-id').value = '';
    }

    modal.style.display = 'block';
}

function closeEmployeeModal() {
    document.getElementById('employee-modal').style.display = 'none';
}

async function saveEmployee(event) {
    event.preventDefault();

    const id = document.getElementById('employee-id').value;
    const data = {
        name: document.getElementById('employee-name').value,
        position: document.getElementById('employee-position').value,
        email: document.getElementById('employee-email').value || null,
        phone: document.getElementById('employee-phone').value || null,
        imageUrl: document.getElementById('employee-image-url').value || null,
        active: document.getElementById('employee-active').checked
    };

    try {
        const url = id ? `${API_BASE_URL}/employees/${id}` : `${API_BASE_URL}/employees`;
        const method = id ? 'PUT' : 'POST';

        const response = await fetch(url, {
            method: method,
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        });

        if (!response.ok) throw new Error('Failed to save employee');

        showAlert(id ? 'Medarbejder opdateret' : 'Medarbejder oprettet', 'success');
        closeEmployeeModal();
        await loadEmployees();
        await loadDashboard();
    } catch (error) {
        console.error('Error saving employee:', error);
        showAlert('Kunne ikke gemme medarbejder', 'error');
    }
}

function editEmployee(id) {
    openEmployeeModal(id);
}

async function deleteEmployee(id) {
    if (!confirm('Er du sikker på at du vil slette denne medarbejder?')) return;

    try {
        const response = await fetch(`${API_BASE_URL}/employees/${id}`, {
            method: 'DELETE'
        });

        if (!response.ok) throw new Error('Failed to delete employee');

        showAlert('Medarbejder slettet', 'success');
        await loadEmployees();
        await loadDashboard();
    } catch (error) {
        console.error('Error deleting employee:', error);
        showAlert('Kunne ikke slette medarbejder', 'error');
    }
}

// ============================================
// UTILITY FUNCTIONS
// ============================================

function escapeHtml(text) {
    if (!text) return '';
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}

function showAlert(message, type = 'info') {
    const alert = document.createElement('div');
    alert.className = `alert alert-${type}`;
    alert.style.cssText = `
        position: fixed;
        top: 20px;
        right: 20px;
        padding: 1rem 1.5rem;
        background: ${type === 'success' ? '#10b981' : type === 'error' ? '#ef4444' : '#3b82f6'};
        color: white;
        border-radius: 8px;
        box-shadow: 0 4px 12px rgba(0,0,0,0.15);
        z-index: 10000;
        animation: slideIn 0.3s ease-out;
        max-width: 400px;
    `;
    alert.textContent = message;

    document.body.appendChild(alert);

    setTimeout(() => {
        alert.style.animation = 'slideOut 0.3s ease-out';
        setTimeout(() => alert.remove(), 300);
    }, 3000);
}

// ============================================
// EVENT LISTENERS
// ============================================

document.addEventListener('DOMContentLoaded', function() {
    console.log('Admin panel initializing...');

    // Logout button
    const logoutBtn = document.getElementById('logout-btn');
    if (logoutBtn) {
        logoutBtn.addEventListener('click', function() {
            if (confirm('Er du sikker på at du vil logge ud?')) {
                sessionStorage.removeItem('adminLoggedIn');
                sessionStorage.removeItem('adminUsername');
                window.location.href = 'login.html';
            }
        });
    }

    // Load all data
    loadDashboard();
    loadServices();
    loadEmployees();

    console.log('Admin panel initialized!');
});

// Close modals when clicking outside
window.onclick = function(event) {
    const serviceModal = document.getElementById('service-modal');
    const employeeModal = document.getElementById('employee-modal');

    if (event.target == serviceModal) {
        serviceModal.style.display = 'none';
    }
    if (event.target == employeeModal) {
        employeeModal.style.display = 'none';
    }
}