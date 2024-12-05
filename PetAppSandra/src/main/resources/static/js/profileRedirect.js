document.addEventListener("DOMContentLoaded", () => {
    console.log("DOM fully loaded for profile redirection!");

    // Usamos setInterval para comprobar dinámicamente si el 'user-id' está disponible
    const checkUserIdInterval = setInterval(() => {
        const userIdElement = document.getElementById('user-id');
        const userId = userIdElement?.value;

        if (userId) {
            console.log("User ID detected:", userId);

            // Asignar las funciones a los botones solo cuando el user-id esté disponible
            const ownersButton = document.getElementById('linkOwnersButton');
            const petSittersButton = document.getElementById('linkPetsittersButton');

            if (ownersButton && petSittersButton) {
                ownersButton.addEventListener('click', () => setRedirectURL('owner', userId));
                petSittersButton.addEventListener('click', () => setRedirectURL('petSitter', userId));
                console.log("Event listeners assigned for profile redirection.");

                // Detenemos el intervalo una vez que todo esté configurado
                clearInterval(checkUserIdInterval);
            } else {
                console.warn("Buttons for redirection not found. Waiting...");
            }
        } else {
            console.warn("Waiting for 'user-id' to be dynamically filled.");
        }
    }, 500); // Revisamos cada 500ms
});

// Función para redirigir al perfil adecuado
function setRedirectURL(profileType, userId) {
    // Verificamos si el userId está presente y tiene valor
    if (!userId) {
        alert("You must be logged in to view your profile.");
        window.location.href = "/login"; // Redirigir al login si no está loggeado
        return;
    }

    // Redirigimos según el tipo de perfil
    if (profileType === 'owner') {
        window.location.href = `/owner/${userId}`; // Redirigir al perfil de Owner
    } else if (profileType === 'petSitter') {
        window.location.href = `/petSitter/${userId}`; // Redirigir al perfil de PetSitter
    }
}

