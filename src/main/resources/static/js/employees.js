// Fetch and display employees
async function loadEmployees() {
    const container = document.getElementById('employees-grid');
    const loadingEl = document.getElementById('employees-loading');
    const errorEl = document.getElementById('employees-error');

    try {
        showLoading('employees-loading');

        const employees = await fetchAPI('/employees');

        hideLoading('employees-loading');
        container.classList.remove('hidden');

        if (employees.length === 0) {
            container.innerHTML = '<p class="no-data">Ingen medarbejdere tilgængelige.</p>';
            return;
        }

        container.innerHTML = employees.map(employee => {
            const initials = employee.name.split(' ').map(part => part[0]).join('').toUpperCase().substring(0, 2);

            return `
                <div class="team-card">
                    ${employee.imageUrl ?
                `<img src="${escapeHtml(employee.imageUrl)}" alt="${escapeHtml(employee.name)}" class="employee-image">` :
                `<div class="employee-placeholder">${initials}</div>`
            }
                    <h3>${escapeHtml(employee.name)}</h3>
                    <p class="employee-position">${escapeHtml(employee.position)}</p>
                    ${employee.email ? `<p class="employee-contact">✉️ ${escapeHtml(employee.email)}</p>` : ''}
                    ${employee.phone ? `<p class="employee-contact">📞 ${escapeHtml(employee.phone)}</p>` : ''}
                </div>
            `;
        }).join('');

    } catch (error) {
        console.error('Error loading employees:', error);
        hideLoading('employees-loading');
        container.classList.remove('hidden');
        if (errorEl) {
            errorEl.textContent = 'Kunne ikke hente medarbejdere. Prøv igen senere.';
            errorEl.classList.remove('hidden');
        }
    }
}

// Load employees when page loads
document.addEventListener('DOMContentLoaded', loadEmployees);