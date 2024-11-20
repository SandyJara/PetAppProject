


document.addEventListener('DOMContentLoaded', async () => {

let userId; // variable to use the ID after, I added it with the photos code, this has to be first, if not error 
 


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
            
            
            
             console.log("Datos del usuario recibidos:", user); // voy a borrar esto solo es para validacion: Verifica lo que devuelve el backend
           
           
           
           
             userId = user.id; //save the ID 
             
             
              console.log("User ID obtenido:", userId); //  voy a borrar esto solo es para validacion: Log para verificar el ID asignado

            // Fill the information  with the data gotten 
            getElementById('phone-number').value = user.phone || '';
            document.getElementById('address').value = user.address || '';
            document.getElementById('new-password').value = '';
            document.getElementById('confirm-password').value = '';
        
        
          // update photo if exists
           if (user.profilePictureUrl) {
   			 document.getElementById('profile-picture-preview').src = user.profilePictureUrl;
				} else {
 			   document.getElementById('profile-picture-preview').src = '/images/default-image.png';
				}
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
    
    
    // Handle the "Upload" button for profile picture
     const uploadButton = document.getElementById('upload-buttonProfilePhoto');
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

            //Update photo from profile in frontend
            document.getElementById('profile-picture-preview').src = result.url;
        } else {
            const errorMessage = await response.text();
            alert(`Image upload failed: ${errorMessage}`);
        }
    } catch (error) {
        console.error('Error uploading profile picture:', error);
        alert('An error occurred while uploading the profile picture.');
    }
});
    
    
    
});