document.addEventListener('DOMContentLoaded', () => {

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
		    
    
    //2.-to send to upload information
    const updateInfoButton = document.getElementById('updateInfo');

    updateInfoButton.addEventListener('click', () => {
        window.location.href = '/updateOwner';
    });
    
    
     // 3. Uplead information from Owner profile
    
    const loadProfileData = async () => {
    try {
        // requesting backend for the data
        const response = await fetch('/owner/data', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        });

        if (response.ok) {
            const user = await response.json();

            // filling the info
          	  	document.querySelector('.profile-title').textContent = `WELCOME ${user.fullname}`;
                document.getElementById('user-fullname').textContent = user.fullname || 'N/A';
                document.getElementById('user-birthdate').textContent = user.birthdate || 'N/A';
                document.getElementById('user-email').textContent = user.email || 'N/A';
                document.getElementById('user-phone').textContent = user.phone || 'N/A';
                document.getElementById('user-address').textContent = user.address || 'N/A';
                document.getElementById('user-username').textContent = user.username || 'N/A';
        } else {
            alert('Failed to load user data. Please log in again.');
            window.location.href = '/login';
        }
    } catch (error) {
        console.error('Error fetching user data:', error);
        alert('An error occurred while loading the user data.');
    }
  };
    
    // calling function to charge profile data
   loadProfileData();;
    
});