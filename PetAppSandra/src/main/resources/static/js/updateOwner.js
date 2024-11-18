document.addEventListener('DOMContentLoaded', async () => {
    try {
        // Ask backend for the information
        const response = await fetch('/updateOwner/data', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        });

        if (response.ok) {
            const user = await response.json();

            // Fill the information  with the data gotten 
            document.getElementById('phone-number').value = user.phone || '';
            document.getElementById('address').value = user.address || '';
            document.getElementById('new-password').value = '';
            document.getElementById('confirm-password').value = '';
        } else {
            alert('Failed to load user data. Please log in again.');
            window.location.href = '/login';
        }
    } catch (error) {
        console.error('Error fetching user data:', error);
        alert('An error occurred while loading the user data.');
    }

    // This is for my button update For the Owner Profile (OP) 
    const updateButton = document.getElementById('update-buttonOP');
    updateButton.addEventListener('click', async (event) => {
        event.preventDefault();

        const phone = document.getElementById('phone-number').value;
        const address = document.getElementById('address').value;
        const newPassword = document.getElementById('new-password').value;
        const confirmPassword = document.getElementById('confirm-password').value;

        // To update the information, validate also that the password and its confirmation coincide
        if (newPassword && newPassword !== confirmPassword) {
            alert('New passwords do not match. Please try again.');
            return;
        }

        const updateData = {
            phone,
            address,
            newPassword
        };

        try {
            const response = await fetch('/updateOwner', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(updateData)
            });

            if (response.ok) {
                alert('Profile updated successfully.');
                window.location.href = '/owner';
            } else {
                const errorMessage = await response.text();
                alert(`Update failed: ${errorMessage}`);
            }
        } catch (error) {
            console.error('Error updating profile:', error);
            alert('An error occurred while updating the profile.');
        }
    });
});
