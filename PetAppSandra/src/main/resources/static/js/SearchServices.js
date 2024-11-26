document.addEventListener('DOMContentLoaded', () => {
    console.log('Google Maps integration placeholder. To be implemented later.');

    // Get DOM elements
    const serviceTypeFilter = document.getElementById('service-type-filter');
    const searchServicesButton = document.getElementById('search-services');
    const servicesTableBody = document.getElementById('servicesTableBody');

    // Check if required DOM elements exist
    if (!serviceTypeFilter || !searchServicesButton || !servicesTableBody) {
        console.error("Required DOM elements not found.");
        return;
    }

    // Filter Services by Type
    searchServicesButton.addEventListener('click', async () => {
        const selectedType = serviceTypeFilter.value;

        try {
            const response = await fetch(`/services/pending?serviceType=${selectedType}`, {
                method: 'GET',
            });

            if (!response.ok) {
                throw new Error(`Failed to fetch services. Status: ${response.status}`);
            }

            const services = await response.json();
            console.log('Services received:', services);

            // Populate the table and show modal
            populateServicesTable(services);
            $('#servicesModal').modal('show'); // Open modal after populating
        } catch (error) {
            console.error('Error fetching services:', error);
            alert('There was an error fetching services. Please try again later.');
        }
    });

    // Function to populate the services table with data
    function populateServicesTable(services) {
        servicesTableBody.innerHTML = ''; // Clear the table

        if (services.length === 0) {
            const row = document.createElement('tr');
            row.innerHTML = `<td colspan="8" class="text-center">No services available.</td>`;
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
               <td>${service.startDate.split('T')[0]}</td> <!--dont show time -->
   				<td>${service.endDate.split('T')[0]}</td> <!-- dont show time -->
                <td>${service.description}</td>
                <td>
       				 <button style="background-color: #d87db5; color: white; padding: 10px 15px; border: none; border-radius: 5px; transition: background-color 0.3s ease, transform 0.2s ease;" onclick="applyToService(${service.id})">Apply</button>
       				 
    			</td>
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
