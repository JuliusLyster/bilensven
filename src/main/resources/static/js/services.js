// Fetch and display services
async function loadServices() {
    const container = document.getElementById('services-grid');
    const loadingEl = document.getElementById('services-loading');
    const errorEl = document.getElementById('services-error');

    try {
        showLoading('services-loading');

        const services = await fetchAPI('/services');

        hideLoading('services-loading');
        container.classList.remove('hidden');

        if (services.length === 0) {
            container.innerHTML = '<p class="no-data">Ingen ydelser tilgængelige.</p>';
            return;
        }

        // Render services with images
        container.innerHTML = services.map(service => `
            <div class="service-card">
                ${service.imageUrl ?
            `<img src="${escapeHtml(service.imageUrl)}" 
                         alt="${escapeHtml(service.name)}" 
                         class="service-image"
                         onerror="this.style.display='none'; this.nextElementSibling.style.display='flex';">
                     <div class="service-placeholder" style="display: none;">
                        <svg width="80" height="80" viewBox="0 0 24 24" fill="none" stroke="#cbd5e1" stroke-width="2">
                            <rect x="3" y="3" width="18" height="18" rx="2" ry="2"/>
                            <circle cx="8.5" cy="8.5" r="1.5"/>
                            <polyline points="21 15 16 10 5 21"/>
                        </svg>
                    </div>` :
            `<div class="service-placeholder">
                        <svg width="80" height="80" viewBox="0 0 24 24" fill="none" stroke="#cbd5e1" stroke-width="2">
                            <rect x="3" y="3" width="18" height="18" rx="2" ry="2"/>
                            <circle cx="8.5" cy="8.5" r="1.5"/>
                            <polyline points="21 15 16 10 5 21"/>
                        </svg>
                    </div>`
        }
                <div class="service-content">
                    <h3 class="service-card__title">${escapeHtml(service.name)}</h3>
                    <p class="service-card__description">${escapeHtml(service.description)}</p>
                    <p class="service-card__price">${formatPrice(service.price)} DKK</p>
                </div>
            </div>
        `).join('');

    } catch (error) {
        console.error('Error loading services:', error);
        hideLoading('services-loading');
        container.classList.remove('hidden');
        if (errorEl) {
            errorEl.textContent = 'Kunne ikke hente ydelser. Prøv igen senere.';
            errorEl.classList.remove('hidden');
        }
    }
}

// Format price with Danish formatting
function formatPrice(price) {
    return new Intl.NumberFormat('da-DK', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
    }).format(price);
}

// Escape HTML to prevent XSS
function escapeHtml(text) {
    if (!text) return '';
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}

// Load services when page loads
document.addEventListener('DOMContentLoaded', loadServices);