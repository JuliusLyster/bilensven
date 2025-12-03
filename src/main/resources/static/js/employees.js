// Fetch and display employees
async function loadEmployees() {
    const container = document.getElementById('employees-grid');  // ✅ Ændret til employees-grid
    const loadingEl = document.getElementById('employees-loading');
    const errorEl = document.getElementById('employees-error');

    try {
        showLoading('employees-loading');

        const employees = await fetchAPI('/employees');

        hideLoading('employees-loading');
        container.classList.remove('hidden');  // ✅ Vis grid

        if (employees.length === 0) {
            container.innerHTML = '<p class="no-data">Ingen medarbejdere tilgængelige.</p>';
            return;
        }

        // Render employees
        container.innerHTML = employees.map(employee => `
            <div class="team-card">
                <div class="team-card__image">
                    ${employee.imageUrl
            ? `<img src="${escapeHtml(employee.imageUrl)}" alt="${escapeHtml(employee.name)}" style="width: 100%; height: 100%; border-radius: 50%; object-fit: cover;">`
            : `<div style="width: 150px; height: 150px; border-radius: 50%; background: #e5e7eb; display: flex; align-items: center; justify-content: center; font-size: 2rem; margin: 0 auto;">${getInitials(employee.name)}</div>`
        }
                </div>
                <h3 class="team-card__name">${escapeHtml(employee.name)}</h3>
                <p class="team-card__position">${escapeHtml(employee.position)}</p>
                ${employee.email ? `<p class="team-card__contact">✉️ ${escapeHtml(employee.email)}</p>` : ''}
                ${employee.phone ? `<p class="team-card__contact">📞 ${escapeHtml(employee.phone)}</p>` : ''}
            </div>
        `).join('');

    } catch (error) {
        hideLoading('employees-loading');
        container.classList.remove('hidden');  // ✅ Vis container selv ved fejl
        errorEl.textContent = 'Kunne ikke hente medarbejdere. Prøv igen senere.';
        errorEl.classList.remove('hidden');  // ✅ Vis error
    }
}

// Get initials from name for placeholder
function getInitials(name) {
    return name
        .split(' ')
        .map(part => part[0])
        .join('')
        .toUpperCase()
        .substring(0, 2);
}

// Escape HTML to prevent XSS
function escapeHtml(text) {
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}

// Load employees when page loads
document.addEventListener('DOMContentLoaded', loadEmployees);