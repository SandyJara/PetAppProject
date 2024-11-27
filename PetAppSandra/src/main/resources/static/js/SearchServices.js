document.addEventListener('DOMContentLoaded', () => {

let userId;
let serviceId;

    console.log('Google Maps integration placeholder. To be implemented later.');

    // Get DOM elements
    const serviceTypeFilter = document.getElementById('service-type-filter');
    const searchServicesButton = document.getElementById('search-services');
    const servicesTableBody = document.getElementById('servicesTableBody');
     const consultApplicationsButton = document.getElementById('consultApplications'); 
    const applicationsTableBody = document.getElementById('myApplicationsTableBody');

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
                <td>${service.payment} €</td>
                <td>${service.description}</td>
                <td>
       				 <button 
	       				  id="apply-button-${service.id}" 
	       				  style="background-color: #d87db5; color: white; padding: 10px 15px; border: none; border-radius: 5px; transition: background-color 0.3s ease, transform 0.2s ease;"
	       				  onclick="applyToService(${service.id})">
	       				  Apply
       				  </button>
       				 
    			</td>
            `;
            servicesTableBody.appendChild(row);
        });
    }

    // Function to handle the APPLY button click
 	window.applyToService = async function(serviceId) { //allows the function globally
        try {
            const response = await fetch(`/services/api/services/${serviceId}/apply`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
            });

            if (!response.ok) {
                throw new Error(`Failed to apply for service. Status: ${response.status}`);
            }

            alert('You have successfully applied to the service.');
        
          // If the user applied, unhabilitate it 
        const button = document.getElementById(`apply-button-${serviceId}`);
        if (button) {
            button.disabled = true;
            button.textContent = 'Applied'; 
            button.style.backgroundColor = '#b76c9b'; 
        }

    } catch (error) {
        console.error('Error applying to service:', error);
        alert('There was an error applying to the service. Please try again later.');
    }
}

// Applied Services
consultApplicationsButton.addEventListener('click', async () => {
    const sitterId = document.getElementById('user-id').value; // get ID from Pet Sitter (fron hidden in html)
    if (!sitterId) {
        alert("Pet Sitter ID is missing. Please log in or verify your profile.");
        return;
    }

    try {
        const response = await fetch(`/services/services/petsitter/${sitterId}`, { 
            method: 'GET',
        });

        if (!response.ok) {
            throw new Error(`Failed to fetch applications. Status: ${response.status}`);
        }

        const applications = await response.json();
        console.log('Applications received:', applications);

        populateApplicationsTable(applications);

        // Show modal
        $('#myApplicationsModal').modal('show');
    } catch (error) {
        console.error('Error fetching applications:', error);
        alert('There was an error fetching your applications. Please try again later.');
    }
});

// Function for the information of the applications
function populateApplicationsTable(applications) {
    applicationsTableBody.innerHTML = ''; // clean table

    if (applications.length === 0) {
        const row = document.createElement('tr');
        row.innerHTML = `<td colspan="9" class="text-center">No applications found.</td>`;
        applicationsTableBody.appendChild(row);
        return;
    }

    applications.forEach(application => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${application.id}</td>
            <td>${application.serviceType}</td>
            <td>${application.petName || 'N/A'}</td>
            <td>${application.ownerName || 'N/A'}</td>
            <td>${application.startDate.split('T')[0]}</td>
            <td>${application.endDate.split('T')[0]}</td>
            <td>${application.payment} €</td>
            <td>${application.status}</td> 
            <td>${application.description || 'N/A'}</td>
        `;
        applicationsTableBody.appendChild(row);
    });
}





});
