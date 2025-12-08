const API_BASE_URL = 'http://localhost:8080/api';

function getInitials(name) {
    return name
        .split(' ')
        .map(word => word[0])
        .join('')
        .toUpperCase()
        .substring(0, 2);
}


function escapeHtml(text) {
    if (!text) return '';
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}

async function loadEmployees() {
    try {
        const response = await fetch(`${API_BASE_URL}/employees`);
        const employees = await response.json();

        const teamGrid = document.getElementById('team-grid');
        const loadingEl = document.getElementById('team-loading');

        if (employees.length === 0) {
            loadingEl.innerHTML = '<p>Ingen medarbejdere at vise.</p>';
            return;
        }

        teamGrid.innerHTML = employees.map(employee => `
            <div class="team-card">
                <div class="employee-image-container">
                    ${employee.imageUrl ?
            `<img src="${employee.imageUrl}" alt="${escapeHtml(employee.name)}" class="employee-image" onerror="this.style.display='none'; this.nextElementSibling.style.display='flex';">
                     <div class="employee-initials" style="display: none;">${getInitials(employee.name)}</div>` :
            `<div class="employee-initials">${getInitials(employee.name)}</div>`
        }
                </div>

                <h3 class="employee-name">${escapeHtml(employee.name)}</h3>
                <p class="employee-position">${escapeHtml(employee.position)}</p>

                <div class="employee-contact">
                    ${employee.email ? `
                        <div class="contact-item">
                            <span class="contact-icon">📧</span>
                            <a href="mailto:${escapeHtml(employee.email)}">${escapeHtml(employee.email)}</a>
                        </div>
                    ` : ''}

                    ${employee.phone ? `
                        <div class="contact-item">
                            <span class="contact-icon">📞</span>
                            <a href="tel:${escapeHtml(employee.phone)}">${escapeHtml(employee.phone)}</a>
                        </div>
                    ` : ''}
                </div>
            </div>
        `).join('');

        loadingEl.style.display = 'none';
        teamGrid.style.display = 'grid';

    } catch (error) {
        console.error('Error loading employees:', error);
        document.getElementById('team-loading').innerHTML =
            '<p style="color: #ef4444;">Kunne ikke indlæse medarbejdere. Prøv igen senere.</p>';
    }
}

document.addEventListener('DOMContentLoaded', loadEmployees);