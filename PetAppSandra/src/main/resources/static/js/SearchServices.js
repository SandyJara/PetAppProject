document.addEventListener('DOMContentLoaded', () => {
    // Placeholder for Google Maps integration for later
    console.log('Google Maps integration placeholder. To be implemented later.');

    // Get DOM elements
    const serviceTypeFilter = document.getElementById('service-type-filter');
    const searchServicesButton = document.getElementById('search-services');
    const servicesTableBody = document.getElementById('servicesTableBody');

    // Check if the required DOM elements are present
    if (!serviceTypeFilter) {
        console.error("Service type filter not found in the DOM.");
        return;
    }

    if (!searchServicesButton) {
        console.error("Search services button not found in the DOM.");
        return;
    }

    if (!servicesTableBody) {
        console.error("Services table body not found in the DOM.");
        return;
    }

    // Filter Services by Type
	 
	searchServicesButton.addEventListener('click', async () => {
        const selectedType = serviceTypeFilter.value; // Obtén el valor seleccionado

        try {
            const response = await fetch(`/services/pending?serviceType=${selectedType}`, {
                method: 'GET', // Asegúrate de que sea GET
            });

            if (!response.ok) {
                throw new Error(`Failed to fetch services. Status: ${response.status}`);
            }

            const services = await response.json();
            console.log('Services received:', services); // Verifica los datos en la consola
            populateServicesTable(services);
        } catch (error) {
            console.error('Error fetching services:', error);
            alert('There was an error fetching services. Please try again later.');
        }
    });
		
		 // Function to populate the services table with data
   function populateServicesTable(services) {
        const servicesTableBody = document.getElementById('servicesTableBody');
        servicesTableBody.innerHTML = ''; // Limpia la tabla antes de llenarla

        if (services.length === 0) {
            const row = document.createElement('tr');
            row.innerHTML = `<td colspan="6">No services available.</td>`;
            servicesTableBody.appendChild(row);
            return;
        }

        services.forEach(service => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${service.id}</td>
                <td>${service.serviceType}</td>
                <td>${service.petName}</td>
                <td>${service.ownerName}</td>
                <td>${service.startDate}</td>
                <td>${service.endDate}</td>
                <td>${service.description}</td>
            `;
            servicesTableBody.appendChild(row);
        });
    }
    
		
		// Function to handle the APPLY button click
    async function applyToService(serviceId) {
        try {
            const response = await fetch(`/api/services/${serviceId}/apply`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
            });

            if (!response.ok) {
                throw new Error(`Failed to apply for service. Status: ${response.status}`);
            }

            alert('You have successfully applied to the service.');
        } catch (error) {
            console.error('Error applying to service:', error);
            alert('There was an error applying to the service. Please try again later.');
        }
    }
});
		