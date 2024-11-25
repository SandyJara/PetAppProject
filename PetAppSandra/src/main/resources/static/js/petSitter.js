document.addEventListener('DOMContentLoaded', () => {

// read userId from hidden field DOM
const userIdField = document.getElementById('user-id');
    if (!userIdField) {
        console.error('Field with ID "user-id" not found in the DOM.');
        return; // checking if it finds the field
    }
    const userId = userIdField.value;
    console.log('User ID:', userId);


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
    
    


//fOR the button update, the information is not all the one registered at the begining, not allowing change user name or email
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
        }
    

    //fOR the button to upload the profile pic
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
                alert('There was an issue, but the system is verifying it, Click UPDATE to proceed with your new information.');
                // message updated for the original saying there was an error, but at the end the photo it's been uploading correctly
            }
        });
    }




    
    
});