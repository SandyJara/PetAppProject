document.addEventListener("DOMContentLoaded", () => {
    const sitterId = getUserIdFromURL();

    if (sitterId) {
        fetchPetSitterPublicProfile(sitterId);
    } else {
        console.error("Pet Sitter ID not found.");
        alert("Unable to load profile: Pet Sitter ID is missing!");
    }
});

async function fetchPetSitterPublicProfile(sitterId) {
    try {
        const response = await fetch(`/sitter/petSitterPublicProfile/${sitterId}`);
        if (!response.ok) {
            throw new Error(`Failed to fetch public profile. Status: ${response.status}`);
        }
        const data = await response.json();
        console.log("Pet Sitter Public Profile Data:", data);

        // getting data from the backend
        const preferences = data.preferences || [];
        const sitterData = data.sitter || {};
        const experience = data.experience || "N/A"; 

        // functions to update information
        populateSitterProfile(sitterData);
        populatePreferences(preferences, experience);

    } catch (error) {
        console.error("Error loading pet sitter public profile:", error);
        alert("An error occurred while loading the profile. Please try again later.");
    }
}

function populateSitterProfile(sitterData) {
    document.getElementById("sitter-name").textContent = sitterData.fullname || "N/A";
    document.getElementById("sitter-email").textContent = sitterData.email || "N/A";
    document.getElementById("sitter-username").textContent = sitterData.username || "N/A";

    const profilePicture = document.getElementById("profile-picture-preview");
    if (profilePicture) {
        profilePicture.src = sitterData.profilePictureUrl && sitterData.profilePictureUrl.trim() !== ""
            ? sitterData.profilePictureUrl
            : "https://via.placeholder.com/150";
    }
}

function populatePreferences(preferences, experience) {
    if (!preferences || preferences.length === 0) {
        document.getElementById("preferred-service-types").innerHTML = "N/A";
        document.getElementById("preferred-pet-types").innerHTML = "N/A";
        document.getElementById("status-profile").textContent = "N/A";
        document.getElementById("experience-text").textContent = "N/A";
        return;
    }

    const serviceTypes = preferences.map(pref => pref.serviceType).join(", ") || "N/A";
    const petTypes = preferences.map(pref => pref.petType).join(", ") || "N/A";
    const statusProfile = preferences[0].statusProfile || "N/A";

    document.getElementById("preferred-service-types").innerHTML = serviceTypes;
    document.getElementById("preferred-pet-types").innerHTML = petTypes;
    document.getElementById("status-profile").textContent = statusProfile;
    document.getElementById("experience-text").textContent = experience || "N/A"; 
}

function getUserIdFromURL() {
    const urlParams = new URLSearchParams(window.location.search);
    return urlParams.get("sitterId");
}
