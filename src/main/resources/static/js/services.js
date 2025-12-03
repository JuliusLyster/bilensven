// Fetch and display services
async function loadServices() {
    const container = document.getElementById('services-grid');  // ✅ Ændret til services-grid
    const loadingEl = document.getElementById('services-loading');
    const errorEl = document.getElementById('services-error');

    try {
        showLoading('services-loading');

        const services = await fetchAPI('/services');

        hideLoading('services-loading');
        container.classList.remove('hidden');  // ✅ Vis grid

        if (services.length === 0) {
            container.innerHTML = '<p class="no-data">Ingen ydelser tilgængelige.</p>';
            return;
        }

        // Render services
        container.innerHTML = services.map(service => `
            <div class="service-card">
                <h3 class="service-card__title">${escapeHtml(service.name)}</h3>
                <p class="service-card__description">${escapeHtml(service.description)}</p>
                <p class="service-card__price">${formatPrice(service.price)} DKK</p>
            </div>
        `).join('');

    } catch (error) {
        hideLoading('services-loading');
        container.classList.remove('hidden');  // ✅ Vis container selv ved fejl
        errorEl.textContent = 'Kunne ikke hente ydelser. Prøv igen senere.';
        errorEl.classList.remove('hidden');  // ✅ Vis error
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
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}

// Load services when page loads
document.addEventListener('DOMContentLoaded', loadServices);