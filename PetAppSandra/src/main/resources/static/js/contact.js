document.addEventListener('DOMContentLoaded', () => {
    const contactButton = document.getElementById('contact-button');

    contactButton.addEventListener('click', async (event) => {
        event.preventDefault();

        const email = document.getElementById('email').value;
        const comments = document.getElementById('comments').value;

        if (!email || !comments) {
            alert('Please fill in all fields.');
            return;
        }

        const data = { email, message: comments };

        try {
            const response = await fetch('/contact/send-message', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(data),
            });

            if (response.ok) {
                alert(`You have sent us a message, we will contact you soon with your email: ${email}`);
            
            // Reload the page, cleaning the space for the message and email
                window.location.reload();
              
            } else {
                const error = await response.text();
                alert(`Failed to send the message: ${error}`);
            }
        } catch (error) {
            console.error('Error sending message:', error);
            alert('An error occurred. Please try again.');
        }
    });
});
