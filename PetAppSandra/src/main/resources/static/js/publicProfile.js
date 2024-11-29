document.addEventListener("DOMContentLoaded", () => {
    const ownerId = getOwnerIdFromURL(); // get ownerId  URL

    if (ownerId) {
        fetchPublicProfile(ownerId);
    } else {
        console.error("Owner ID not found.");
        alert("Unable to load profile: Owner ID is missing!");
    }
});

async function fetchPublicProfile(ownerId) {
    try {
        const response = await fetch(`/owner/ownerPublicProfile/${ownerId}`);
        if (!response.ok) {
            throw new Error(`Failed to fetch public profile. Status: ${response.status}`);
        }
        const data = await response.json();
        console.log("Public Profile Data:", data);

        // show owner information
        const ownerData = data.owner;
        document.getElementById("owner-name").textContent = ownerData.fullname || "N/A";
        document.getElementById("owner-email").textContent = ownerData.email || "N/A";
        document.getElementById("owner-username").textContent = ownerData.username || "N/A";
        const profilePicture = document.getElementById("profile-picture-preview");
        if (profilePicture) {
		    console.log("Profile Picture URL:", ownerData.profilePictureUrl); // Debugging
		    profilePicture.src = ownerData.profilePictureUrl && ownerData.profilePictureUrl.trim() !== ""
		        ? ownerData.profilePictureUrl
		        : "https://via.placeholder.com/150";
		}

        // Show pets information
        const petsContainer = document.getElementById("pet-selection");
        const pets = data.pets || [];
        petsContainer.innerHTML = '<option value="">Select Pet</option>';

        pets.forEach((pet) => {
            // pets for dropdown
            const petOption = document.createElement("option");
            petOption.value = pet.id;
            petOption.textContent = pet.name || "Unnamed Pet";
            petsContainer.appendChild(petOption);
        });

        // show information from the pet selected
        petsContainer.addEventListener("change", (event) => {
            const selectedPetId = event.target.value;
            const selectedPet = pets.find((pet) => pet.id == selectedPetId);

            if (selectedPet) {
                document.getElementById("pet-name").textContent = selectedPet.name || "N/A";
                document.getElementById("pet-age").textContent = selectedPet.age || "N/A";
                document.getElementById("pet-breed").textContent = selectedPet.breed || "N/A";
                document.getElementById("pet-specie").textContent = selectedPet.species || "N/A";
                document.getElementById("pet-size").textContent = selectedPet.size || "N/A";
                document.getElementById("complementary-info").textContent = selectedPet.description || "N/A";
            } else {
                document.getElementById("pet-name").textContent = "";
                document.getElementById("pet-age").textContent = "";
                document.getElementById("pet-breed").textContent = "";
                document.getElementById("pet-specie").textContent = "";
                document.getElementById("pet-size").textContent = "";
                document.getElementById("complementary-info").textContent = "";
            }
        });
    } catch (error) {
        console.error("Error loading public profile:", error);
        alert("An error occurred while loading the profile. Please try again later.");
    }
}

// to get ownerId from URL
function getOwnerIdFromURL() {
    const urlParams = new URLSearchParams(window.location.search);
    return urlParams.get("ownerId");
}
