document.addEventListener('DOMContentLoaded', () => {
    console.log("DOM fully loaded for Pet Sitters!");

    // Use setInterval to wait for the user ID
    const checkUserIdInterval = setInterval(() => {
        const userIdField = document.getElementById('user-id');
        const userId = userIdField?.value?.trim();

        if (userId) {
            console.log('User ID detected:', userId);
            clearInterval(checkUserIdInterval); // Stop the interval when the userId is found

            // Call functions that need userId
            initProfilePage(userId);
        } else {
            console.warn('Waiting for user ID to be available in the DOM...');
        }
    }, 500); // Check every 500ms
});

// Function to initialize profile page logic
function initProfilePage(userId) {
    console.log('Initializing profile page for User ID:', userId);

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
    } else {
        console.warn("Log out button not found.");
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
            console.log('Fetched user data:', data);

            const phoneInput = document.getElementById('phone-number');
            const addressInput = document.getElementById('address');
            const profilePicturePreview = document.getElementById('profile-picture-preview');

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

            if (profilePicturePreview) {
                profilePicturePreview.src = data.profilePictureUrl || 'https://via.placeholder.com/150';
            } else {
                console.warn('Profile picture preview element not found in the DOM.');
            }
        })
        .catch(error => {
            console.error('Error fetching user data:', error);
            alert('There was an error fetching your profile data. Please try again later.');
        });

    // FOR the button update, the information is not all the one registered at the beginning, not allowing change username or email
    const updateButton = document.getElementById('update-buttonOP');
    if (updateButton) {
        updateButton.addEventListener('click', async (event) => {
            event.preventDefault();

            const phone = document.getElementById('phone-number').value;
            const address = document.getElementById('address').value;
            const newPassword = document.getElementById('new-password').value;
            const confirmPassword = document.getElementById('confirm-password').value;

            if (newPassword && newPassword !== confirmPassword) {
                alert('New passwords do not match. Please try again.');
                return;
            }

            const updateData = {
                phone,
                address,
                newPassword,
            };

            try {
                const response = await fetch('/updatePetSitter', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify(updateData),
                });

                if (response.ok) {
                    alert('Profile updated successfully.');
                    window.location.href = '/petSitter';
                } else {
                    const errorMessage = await response.text();
                    alert(`Update failed: ${errorMessage}`);
                }
            } catch (error) {
                console.error('Error updating profile:', error);
                alert('An error occurred while updating the profile.');
            }
        });
    } else {
        console.warn("Update button not found.");
    }

    // FOR the button to upload the profile pic
    const uploadButton = document.getElementById('upload-buttonProfilePhoto');
    if (uploadButton) {
        uploadButton.addEventListener('click', async (event) => {
            event.preventDefault();

            if (!userId) {
                alert('User ID not found. Please refresh the page or log in again.');
                return;
            }

            const fileInput = document.getElementById('profile-picture');
            const file = fileInput.files[0];

            if (!file) {
                alert('Please select a file to upload.');
                return;
            }

            const formData = new FormData();
            formData.append('file', file);

            try {
                const response = await fetch(`/images/upload/profile/${userId}`, {
                    method: 'POST',
                    body: formData,
                });

                if (response.ok) {
                    const result = await response.json();
                    alert('Profile picture uploaded successfully.');

                    // Update the profile pic in frontend
                    const profilePicturePreview = document.getElementById('profile-picture-preview');
                    profilePicturePreview.src = result.url;
                } else {
                    const errorMessage = await response.text();
                    alert(`Image upload failed: ${errorMessage}`);
                }
            } catch (error) {
                console.error('Error uploading profile picture:', error);
            }
        });
    } else {
        console.warn("Upload button not found.");
    }
}
