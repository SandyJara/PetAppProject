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

		// Check if all the field have information
        if (!fullname || !birthdate || !email || !phone || !address || !username || !password || !confirmPassword) {
            alert("Please fill in all required fields.");
            return; // no register if a data is missing
        }


		//VERIFICATION OF INFORMATION FOR REGISTER
			// verifies that a user is 18 years old or more
				    console.log(`Birthdate: ${birthdate}`);  // just a print to check for me the birthdate
				    
				    const birthdateObj = new Date(birthdate);
				    console.log(`Birthdate Object: ${birthdateObj}`);  // for testing that the date is being converting correctly (because I had errors)
				    
				    if (isNaN(birthdateObj)) {
				        alert("The date entered is not valid.");
				        return;
				    }
				    
				    const today = new Date();
				    console.log(`Today: ${today}`);  // Verifying current date
				    
				    // this its just to stablish midnigth, to void have error with the different hours
				    today.setHours(0, 0, 0, 0);
				    
				    const age = today.getFullYear() - birthdateObj.getFullYear();
				    const m = today.getMonth() - birthdateObj.getMonth();
				
				    console.log(`Calculated Age: ${age}, Month Difference: ${m}`);  //just checking age and diference in months, for testing
				
				    // Formula to calculate the age with the dates
				    if (m < 0 || (m === 0 && today.getDate() < birthdateObj.getDate())) {
				        age--;
				    }
				
				    console.log(`Final Age after adjustment: ${age}`);  // print for me final age
				
				    if (age < 18) {
				        alert('You must be at least 18 years old to register.');
				        return; // Requirement to be over than 18
				    }
				    
					// Verify phone data is only numbers
			        const phonePattern = /^[0-9]+$/;
			        if (!phonePattern.test(phone)) {
			            alert('Please enter a valid phone number (numbers only).');
			            return; 
			        }
			
			        // Verify email has the correct format
			        const emailPattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
			        if (!emailPattern.test(email)) {
			            alert('Please enter a valid email address.');
			            return; 
			        }

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
                alert('Registration successful!');
                window.location.href = '/home';
            } else {
                const errorMessage = await response.text();
                
                if (errorMessage === "Username already exists") {
                    alert("Sorry, this username is already taken. Please choose another.");
                } else if (errorMessage === "Email already exists") {
                    alert("Sorry, this email is already registered. Please use another one.");
                } else {
                    alert(`Registration failed: ${errorMessage}`);
                }
            }
        } catch (error) {
            console.error('Error in registration:', error);
            alert('An error occurred. Please try again.');
        }
    });
    
    //added when I added the profile pic, it wasnt connecting without this
    const userId = localStorage.getItem('userId');
    if (userId) {
        console.log(`User ID retrieved from localStorage: ${userId}`);
    }
    
});