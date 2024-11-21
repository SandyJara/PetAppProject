document.addEventListener('DOMContentLoaded', async () => {
    let userId; // variable to use the ID after, I added it with the photos code, this has to be first, if not error 
 
	//BUTTON FROM PAGE OWNER.HTML
 //1.-
		    const logOutButton = document.getElementById('logOutButton');
		
		    logOutButton.addEventListener('click', async () => {
		        
		        const userConfirmation = confirm('Are you sure you want to log out?');
		
		        if (userConfirmation) {
		            try {
		                const response = await fetch('/logout', {
		                    method: 'POST',
		                    headers: {
		                        'Content-Type': 'application/json'
		                    },
		                    credentials: 'include' // includes cookies for the session.
		                });
		
		                if (response.ok) {
		                    alert('You have successfully logged out.');
		                    window.location.href = '/home'; // when the user logs out, will see again home page.
		                } else {
		                    alert('There was an issue logging out. Try again please.');
		                }
		            } catch (error) {
		                console.error('Error while logging out:', error);
		                alert('Connection error. Please try again.'); //managing error, the user has knownledge if something fails
		            }
		        } else {
		            // if the user chooses not to log out...
		            alert('You chose to stay on the current page.');
		        }
		    });


    // this is to update the information from the current user (from the backend)
    const loadProfileData = async (url, updatePage = false) => {
        try {
            const response = await fetch(url, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                },
            });

            if (response.ok) {
                const user = await response.json();
                console.log('User Data:', user);

                // stablish user ID 
                userId = user.id;

                if (!updatePage) {
                    // get the information from the profile page and show it
                    document.querySelector('.profile-title').textContent = `WELCOME ${user.fullname}`;
                    document.getElementById('user-fullname').textContent = user.fullname || 'N/A';
                    document.getElementById('user-birthdate').textContent = user.birthdate || 'N/A';
                    document.getElementById('user-email').textContent = user.email || 'N/A';
                    document.getElementById('user-phone').textContent = user.phone || 'N/A';
                    document.getElementById('user-address').textContent = user.address || 'N/A';
                    document.getElementById('user-username').textContent = user.username || 'N/A';

                    const imageElement = document.getElementById('profile-picture-preview');
                    imageElement.src = user.profilePictureUrl || 'https://via.placeholder.com/150';
                }
            } else {
                alert('Failed to load user data. Please log in again.');
                window.location.href = '/login';
            }
        } catch (error) {
            console.error('Error fetching user data:', error);
            alert('An error occurred while loading the user data.');
        }
    };

    // This is to verify the current page 
    const currentPath = window.location.pathname;

    if (currentPath === '/owner') {
        await loadProfileData('/owner/data');

        const updateInfoButton = document.getElementById('updateInfo');
        if (updateInfoButton) {
            updateInfoButton.addEventListener('click', () => {
                window.location.href = '/updateOwner';
            });
        }
    } else if (currentPath === '/updateOwner') {
        await loadProfileData('/updateOwner/data', true);



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
                    const response = await fetch('/updateOwner', {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/json',
                        },
                        body: JSON.stringify(updateData),
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
        }
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
