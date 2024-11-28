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
