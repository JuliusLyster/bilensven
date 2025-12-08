// ============================================
// API CONFIGURATION
// ============================================
const API_BASE_URL = 'http://localhost:8080/api';

// ============================================
// UTILITY FUNCTIONS
// ============================================

// Escape HTML to prevent XSS
function escapeHtml(text) {
    if (!text) return '';
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}

// ============================================
// LOAD SERVICES
// ============================================
async function loadServices() {
    try {
        const response = await fetch(`${API_BASE_URL}/services?sortBy=name`);
        const services = await response.json();

        const servicesGrid = document.getElementById('services-grid');
        const loadingEl = document.getElementById('services-loading');

        if (services.length === 0) {
            loadingEl.innerHTML = '<p>Ingen services at vise.</p>';
            return;
        }

        servicesGrid.innerHTML = services.map(service => `
            <div class="service-card">
                <div class="service-image">
                    ${service.imageUrl ?
            `<img src="${service.imageUrl}" alt="${escapeHtml(service.name)}" onerror="this.style.display='none'; this.nextElementSibling.style.display='flex';">` :
            ''}
                    <div class="image-placeholder" style="${service.imageUrl ? 'display: none;' : 'display: flex;'}">
                        <svg xmlns="http://www.w3.org/2000/svg" width="80" height="80" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                            <rect x="3" y="3" width="18" height="18" rx="2" ry="2"></rect>
                            <circle cx="8.5" cy="8.5" r="1.5"></circle>
                            <polyline points="21 15 16 10 5 21"></polyline>
                        </svg>
                    </div>
                </div>
                <div class="service-content">
                    <h3 class="service-name">${escapeHtml(service.name)}</h3>
                    <p class="service-description">${escapeHtml(service.description)}</p>
                    <div class="service-footer">
                        <div class="service-price">${service.price.toFixed(2)} DKK</div>
                    </div>
                </div>
            </div>
        `).join('');

        loadingEl.style.display = 'none';
        servicesGrid.style.display = 'grid';

    } catch (error) {
        console.error('Error loading services:', error);
        document.getElementById('services-loading').innerHTML =
            '<p style="color: #ef4444;">Kunne ikke indlæse services. Prøv igen senere.</p>';
    }
}

// ============================================
// INITIALIZE
// ============================================
document.addEventListener('DOMContentLoaded', loadServices);
