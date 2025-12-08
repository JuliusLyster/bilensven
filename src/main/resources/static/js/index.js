// ============================================
// API CONFIGURATION
// ============================================
const API_BASE_URL = 'http://localhost:8080/api';

// ============================================
// API FETCH WRAPPER
// ============================================
async function fetchAPI(endpoint, options = {}) {
    try {
        const response = await fetch(`${API_BASE_URL}${endpoint}`, {
            headers: {
                'Content-Type': 'application/json',
                ...options.headers
            },
            ...options
        });

        if (!response.ok) {
            const error = await response.json().catch(() => ({ message: 'API request failed' }));
            throw new Error(error.message || `HTTP ${response.status}: ${response.statusText}`);
        }

        return await response.json();
    } catch (error) {
        console.error('API Error:', error);
        throw error;
    }
}

// ============================================
// FORM VALIDATION
// ============================================

// Validate form
function validateForm() {
    let isValid = true;

    // Name validation
    const name = document.getElementById('name').value.trim();
    if (name.length < 2) {
        showFieldError('name', 'Navn skal være mindst 2 tegn');
        isValid = false;
    }

    // Email validation
    const email = document.getElementById('email').value.trim();
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(email)) {
        showFieldError('email', 'Ugyldig email-adresse');
        isValid = false;
    }

    // Phone validation (optional but if filled, must be valid)
    const phone = document.getElementById('phone').value.trim();
    if (phone && !/^[\d\s\+\-\(\)]+$/.test(phone)) {
        showFieldError('phone', 'Ugyldigt telefonnummer');
        isValid = false;
    }

    // Message validation
    const message = document.getElementById('message').value.trim();
    if (message.length < 10) {
        showFieldError('message', 'Besked skal være mindst 10 tegn');
        isValid = false;
    }

    return isValid;
}

// Show field error
function showFieldError(fieldId, message) {
    const errorEl = document.getElementById(`${fieldId}-error`);
    if (errorEl) {
        errorEl.textContent = message;
    }
}

// Clear all errors
function clearErrors() {
    document.querySelectorAll('.form__error').forEach(el => {
        el.textContent = '';
    });
}

// ============================================
// CONTACT FORM HANDLER
// ============================================
document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById('contact-form');
    const submitBtn = document.getElementById('submit-btn');
    const successMsg = document.getElementById('form-success');
    const errorMsg = document.getElementById('form-error');

    form.addEventListener('submit', async (e) => {
        e.preventDefault();

        // Clear previous messages
        successMsg.style.display = 'none';
        errorMsg.style.display = 'none';
        clearErrors();

        // Validate form
        if (!validateForm()) {
            return;
        }

        // Get form data
        const formData = {
            name: document.getElementById('name').value.trim(),
            email: document.getElementById('email').value.trim(),
            phone: document.getElementById('phone').value.trim(),
            message: document.getElementById('message').value.trim()
        };

        // Disable button and show loading
        submitBtn.disabled = true;
        submitBtn.textContent = 'Sender...';

        try {
            await fetchAPI('/contact', {
                method: 'POST',
                body: JSON.stringify(formData)
            });

            // Success
            successMsg.style.display = 'block';
            form.reset();

            // Scroll to success message
            successMsg.scrollIntoView({ behavior: 'smooth', block: 'nearest' });

        } catch (error) {
            // Error
            let errorMessage = 'Kunne ikke sende besked. Prøv igen senere.';

            if (error.message.includes('Too Many Requests')) {
                errorMessage = 'Du har sendt for mange beskeder. Prøv igen om lidt.';
            }

            errorMsg.textContent = errorMessage;
            errorMsg.style.display = 'block';
            errorMsg.scrollIntoView({ behavior: 'smooth', block: 'nearest' });

        } finally {
            // Re-enable button
            submitBtn.disabled = false;
            submitBtn.innerHTML = 'Send Besked <span class="btn-arrow">→</span>';
        }
    });

    // ============================================
    // SMOOTH SCROLL
    // ============================================
    document.querySelectorAll('a[href^="#"]').forEach(anchor => {
        anchor.addEventListener('click', function (e) {
            e.preventDefault();
            const target = document.querySelector(this.getAttribute('href'));
            if (target) {
                target.scrollIntoView({ behavior: 'smooth', block: 'start' });
            }
        });
    });
});