document.addEventListener('DOMContentLoaded', () => {

//BUTTON FROM PAGE LOGIN.HTML to resiter a new user, working
     const joinButton = document.getElementById('joinButton');
    const passwordInput = document.getElementById('user-password'); //i changes the ID because it was having like a duplication issue before this change
    const confirmPasswordInput = document.getElementById('confirm-password');
    

    joinButton.addEventListener('click', async (event) => {
        event.preventDefault(); 
        
        const account = document.getElementById('account').value.trim();
        const fullname = document.getElementById('fullname').value.trim();
        const birthdate = document.getElementById('birthdate').value.trim();
        const email = document.getElementById('email').value.trim();
        const phone = document.getElementById('phone').value.trim();
        const address = document.getElementById('address').value.trim();
        const username = document.getElementById('username').value.trim();
        const password = passwordInput.value.trim();
        const confirmPassword = confirmPasswordInput.value.trim();


		 console.log(`Password: "${password}"`);
         console.log(`Confirm Password: "${confirmPassword}"`);


        // password and its confirmation should be the same
        if (password !== confirmPassword) {
            alert('Passwords do not match. Try again please.');
            return;
        }

        // This is my object to send all the information to my database
        const userData = {
            account,
            fullname,
            birthdate,
            email,
            phone,
            address,
            username,
            password
        };

        try {
            // this is to send my info to the backend.
            const response = await fetch('/register', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(userData)
            });

            
            if (response.ok) {
                alert('Your registration was successful!');
                window.location.href = '/home'; // after the user registers, it sends back to home page.
            } else {
                const errorMessage = await response.text();
                alert(`Registration failed: ${errorMessage}`);
            }
        } catch (error) {
            console.error('Error in the registration:', error);
            alert('An error occurred. Try again please.');
        }
    });
    
    //added when I added the profile pic, it wasnt connecting without this
    const userId = localStorage.getItem('userId');
    if (userId) {
        console.log(`User ID retrieved from localStorage: ${userId}`);
    }
    
});