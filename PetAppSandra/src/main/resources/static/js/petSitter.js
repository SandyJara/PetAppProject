document.addEventListener('DOMContentLoaded', () => {
    // Log out button
    const logOutButton = document.getElementById('logOutButton');
    if (logOutButton) {
        logOutButton.addEventListener('click', async () => {
            const userConfirmation = confirm('Are you sure you want to log out?');

            if (userConfirmation) {
                try {
                    const response = await fetch('/logout', {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/json',
                        },
                        credentials: 'include', // includes cookies for the session.
                    });

                    if (response.ok) {
                        alert('You have successfully logged out.');
                        window.location.href = '/home';
                    } else {
                        alert('There was an issue logging out. Try again please.');
                    }
                } catch (error) {
                    console.error('Error while logging out:', error);
                    alert('Connection error. Please try again.');
                }
            } else {
                alert('You chose to stay on the current page.');
            }
        });
    }

    // Update button
    const updateInfoButton = document.getElementById('updateInfo');
    if (updateInfoButton) {
        updateInfoButton.addEventListener('click', () => {
            window.location.href = '/updatePetSitter';
        });
    }
    
    
    
    
    
    
    
    // Fetch user data from the backend
    fetch('/updatePetSitter/data')
        .then(response => {
            if (!response.ok) {
                throw new Error('Failed to fetch user profile data. Status: ' + response.status);
            }
            return response.json();
        })
        .then(data => {
            console.log('User ID:', data.id);

            const phoneInput = document.getElementById('phone-number');
            const addressInput = document.getElementById('address');

            if (phoneInput) {
                phoneInput.value = data.phone || '';
            } else {
                console.warn('Phone number input not found in the DOM.');
            }

            if (addressInput) {
                addressInput.value = data.address || '';
            } else {
                console.warn('Address input not found in the DOM.');
            }
        })
        .catch(error => {
            console.error('Error fetching user data:', error);
            alert('There was an error fetching your profile data. Please try again later.');
        });
    
    
    
    
    
});