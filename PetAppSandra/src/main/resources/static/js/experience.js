///for the EXPERIENCE PART
let sitterId = null; // Global variable to save ID from the backend

// Loading existing data 
async function fetchUserDataAndInitializeExperience() {
    try {
        const response = await fetch('/updatePetSitter/data');
        if (!response.ok) {
            throw new Error('Failed to fetch user profile data. Status: ' + response.status);
        }
        const data = await response.json();
        console.log('Fetched User Data:', data);

        // Here is  when this variable get the current ID from the sessionn
        sitterId = data.id;

        // I duplicate this from my update code ... review to delate duplicated one
        const phoneInput = document.getElementById('phone-number');
        const addressInput = document.getElementById('address');
        const profilePicturePreview = document.getElementById('profile-picture-preview');
        
        if (phoneInput) phoneInput.value = data.phone || '';
        if (addressInput) addressInput.value = data.address || '';
        if (profilePicturePreview) {
            profilePicturePreview.src = data.profilePictureUrl || 'https://via.placeholder.com/150';
        }

        // get the experience that was saved before
        loadExperience();
    } catch (error) {
        console.error('Error fetching user data:', error);
        alert('There was an error fetching your profile data. Please try again later.');
    }
}

// Load the experiece (from the backend)
async function loadExperience() {
    if (!sitterId) {
        console.error("Sitter ID is null or undefined. Cannot load experience.");
        alert("Unable to load experience: User ID is missing!");
        return;
    }

    try {
        const response = await fetch(`/experience/${sitterId}`);
        if (response.ok) {
            const experience = await response.json();
            console.log("Loaded experience:", experience);

            const experienceTextElement = document.getElementById('complementary-info');
            if (experienceTextElement) {
                experienceTextElement.value = experience.experienceText || '';
            } else {
                console.warn("Text area for experience not found.");
            }
        } else if (response.status === 404) {
            console.warn("No experience found for this sitter.");
            alert("No experience found for this sitter. You can add one.");
        } else {
            throw new Error(`Failed to load experience. Status: ${response.status}`);
        }
    } catch (error) {
        console.error("Error loading experience:", error);
        alert("An error occurred while loading experience data. Please try again later.");
    }
}

// Update or create experience (from the backend)
async function updateExperience() {
    const experienceTextElement = document.getElementById('complementary-info');
    const experienceText = experienceTextElement ? experienceTextElement.value : '';

    if (!sitterId || !experienceText.trim()) {
        console.error("Invalid sitter ID or experience text.");
        alert("Experience text is not filled out.");
        return;
    }

    try {
        const response = await fetch(`/experience/${sitterId}`, {
            method: "PUT", // Creation and update 
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({ experienceText }),
        });

        if (response.ok) {
            const message = response.status === 201
                ? "Experience created successfully!"
                : "Experience updated successfully!";
            console.log(message);
            alert(message);
        } else {
            const errorMessage = await response.text();
            console.error(`Failed to update/create experience: ${errorMessage}`);
            alert(`Failed to update or create experience: ${errorMessage}`);
        }
    } catch (error) {
        console.error("Error updating or creating experience:", error);
        alert("An error occurred while updating or creating experience. Please try again later.");
    }
}

// Starting the page: get information from the user and configuration for the events
document.addEventListener("DOMContentLoaded", () => {
    fetchUserDataAndInitializeExperience(); // Upload info from the user

    const updateExperienceButton = document.getElementById("updateExperience");
    if (updateExperienceButton) {
        updateExperienceButton.addEventListener("click", updateExperience);
    } else {
        console.warn("Update Experience button not found in the DOM.");
    }
});
