// Validate if URL  has parameter `success`
const urlParams = new URLSearchParams(window.location.search);
if (urlParams.has('success') && urlParams.get('success') === 'true') {
    const successMessage = document.getElementById('success-message');
    if (successMessage) {
        successMessage.style.display = 'block'; // Mostrar el mensaje

        // Desappears the confirmation message after 5 seconds
        setTimeout(() => {
            successMessage.style.display = 'none';
        }, 5000);
    }
    // Delete the confirmation after reload the page
    history.replaceState(null, null, window.location.pathname);
}


// fOr the system to rate by the stars 
document.querySelectorAll('.profile-rating-stars .star').forEach((star, index) => {
    star.addEventListener('click', () => {
        document.querySelectorAll('.profile-rating-stars .star').forEach((s, i) => {
            if (i <= index) {
                s.classList.add('checked');
            } else {
                s.classList.remove('checked');
            }
        });

        // Update the value from the Input to send rating  selected
        const ratingInput = document.querySelector('input[name="rating"]');
        if (ratingInput) {
            ratingInput.value = index + 1; // adjust the value according to the star choosen
        }
    });
});