document.addEventListener('DOMContentLoaded', () => {
    // Placeholder for Google Maps integration
    console.log('TODO: Implement Google Maps functionality here for area filter.');

    // Filter Services by Type
    const serviceTypeFilter = document.getElementById('service-type-filter');
    const searchServicesButton = document.getElementById('search-services');

    searchServicesButton.addEventListener('click', async () => {
        const selectedType = serviceTypeFilter.value;

        try {
            const response = await fetch(`/api/services?type=${selectedType}&status=PENDING`); // Fetch services based on filter
            if (!response.ok) {
                throw new Error(`Failed to fetch services. Status: ${response.status}`);
            }

            const services = await response.json();
            populateServicesTable(services);
        } catch (error) {
            console.error('Error fetching services:', error);
            alert('There was an error fetching services. Please try again later.');
        }
    });

    // Populate the Modal Table with Services
    function populateServicesTable(services) {
        const servicesTableBody = document.getElementById('servicesTableBody');
        servicesTableBody.innerHTML = ''; // Clear previous info
        
        if (services.length === 0) {
            const noDataRow = document.createElement('tr');
            noDataRow.innerHTML = `
                <td colspan="8" class="text-center">No services available for the selected criteria.</td>
            `;
            servicesTableBody.appendChild(noDataRow);
            return;
        }

        services.forEach((service) => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${service.id}</td>
                <td>${service.type}</td>
                <td>${service.petName}</td>
                <td>${service.ownerName}</td>
                <td>${service.startDate}</td>
                <td>${service.endDate}</td>
                <td>${service.description}</td>
                <td>
                    <button id="apply-btn-${service.id}" class="btn btn-success btn-apply-service">APPLY</button>
                </td>
            `;
            servicesTableBody.appendChild(row);

            // Add event listener for the APPLY button
            const applyButton = document.getElementById(`apply-btn-${service.id}`);
            applyButton.addEventListener('click', () => {
                applyToService(service.id);
            });
        });
    }

    // Apply to a Service
    function applyToService(serviceId) {
        // Placeholder for actual application logic
        console.log(`Applying to service with ID: ${serviceId}`);
        alert(`Application for service ID: ${serviceId} submitted successfully!`);
    }
});