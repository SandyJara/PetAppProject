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
        const response = await fetch(`/owner/publicProfile/${ownerId}`);
        if (!response.ok) {
            throw new Error(`Failed to fetch public profile. Status: ${response.status}`);
        }
        const data = await response.json();
        console.log("Public Profile Data:", data);

        // show owner information
        const ownerData = data.owner;
        document.getElementById("owner-name").textContent = ownerData.fullname || "N/A";
        document.getElementById("owner-email").textContent = ownerData.email || "N/A";
        document.getElementById("owner-phone").textContent = ownerData.phone || "N/A";
        const profilePicture = document.getElementById("profile-picture");
        if (profilePicture) {
            profilePicture.src = ownerData.profilePictureUrl || "https://via.placeholder.com/150";
        }

        // Show pets information
        const petsContainer = document.getElementById("pets-container");
        const pets = data.pets || [];
        petsContainer.innerHTML = ""; 

        pets.forEach((pet) => {
            const petElement = document.createElement("div");
            petElement.className = "pet-card";
            petElement.innerHTML = `
                <h4>${pet.name}</h4>
                <p>Age: ${pet.age || "N/A"} years</p>
                <p>Breed: ${pet.breed || "N/A"}</p>
                <p>Size: ${pet.size || "N/A"}</p>
                <p>Complementary Info: ${pet.complementaryInfo || "N/A"}</p>
            `;
            petsContainer.appendChild(petElement);
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
