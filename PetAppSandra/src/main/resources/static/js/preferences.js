document.getElementById('updatePreferences').addEventListener('click', async () => {


    const selectedTypes = Array.from(document.querySelectorAll('input[name="type"]:checked')).map(checkbox => checkbox.value);
    const selectedPetTypes = Array.from(document.querySelectorAll('input[name="petType"]:checked')).map(checkbox => checkbox.value);
    const statusProfile = document.getElementById('statusProfile').value;

    const preferencesData = {
        serviceTypes: selectedTypes,
        petTypes: selectedPetTypes,
        statusProfile: statusProfile,
    };

    try {
        const response = await fetch('/preferences/update', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(preferencesData),
        });

        if (response.ok) {
            alert('Preferences updated successfully!');
            console.log("Service type selected:", selectedTypes);
            console.log("Pet type selected:", selectedPetTypes);
        } else {
            const errorMessage = await response.text();
            alert(`Failed to update preferences: ${errorMessage}`);
        }
    } catch (error) {
        console.error('Error updating preferences:', error);
        alert('An unexpected error occurred while updating preferences.');
    }
});



//get valie sitterID
const sitterId = document.getElementById('user-id').value;
console.log("Sitter ID:", sitterId);

//to see the information already saved
document.addEventListener('DOMContentLoaded', async () => {
    let userId;
	

    try {
        const response = await fetch(`/preferences/${sitterId}`);
        if (response.ok) {
            const preferences = await response.json();

            // Preseleccionar los tipos de servicio
            const serviceTypes = preferences.serviceType.split(','); // Convierte la cadena en array
            serviceTypes.forEach(type => {
                const checkbox = document.querySelector(`input[name="type"][value="${type}"]`);
                if (checkbox) checkbox.checked = true;
            });

            // Preseleccionar los tipos de mascotas
            const petTypes = preferences.petType.split(','); // Convierte la cadena en array
            petTypes.forEach(pet => {
                const checkbox = document.querySelector(`input[name="petType"][value="${pet}"]`);
                if (checkbox) checkbox.checked = true;
            });

            // Preseleccionar el estado del perfil
            document.getElementById('statusProfile').value = preferences.statusProfile;
        } else {
            console.error('Failed to load preferences:', response.status);
        }
    } catch (error) {
        console.error('Error loading preferences:', error);
    }
});