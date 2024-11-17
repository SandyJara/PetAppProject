document.addEventListener('DOMContentLoaded', () => { //same code as for the owner page, same functionality of the button
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
});