document.addEventListener('DOMContentLoaded', async () => {
    const serviceType = document.getElementById('service-type');
    const petSelection = document.getElementById('pet-selection');
    const startDate = document.getElementById('start-date');
    const endDate = document.getElementById('end-date');
    const payment = document.getElementById('payment');
    const comments = document.getElementById('comments');
    const createServiceButton = document.getElementById('create-service');
    const consultServicesButton = document.getElementById('consult-services');
    const servicesTableBody = document.getElementById('servicesTableBody');

    // Load the pets 
    const loadPetsForOwner = async () => {
        try {
            const response = await fetch('/services/owner/pets', {
                method: 'GET',
                headers: { 'Content-Type': 'application/json' }
            });

            if (response.ok) {
                const pets = await response.json();
                petSelection.innerHTML = '<option value="">Select Pet</option>';
                pets.forEach(pet => {
                    petSelection.innerHTML += `<option value="${pet.id}">${pet.name}</option>`;
                });
            } else {
                console.error('Failed to load pets for owner');
                alert('Failed to load pets. Please try again.');
            }
        } catch (error) {
            console.error('Error loading pets:', error);
            alert('An error occurred while loading pets.');
        }
    };

    // load the page 
    await loadPetsForOwner();

    // Load the services 
    const loadServices = async () => {
        try {
            const response = await fetch('/services/owner/services', {
                method: 'GET',
                headers: { 'Content-Type': 'application/json' }
            });

            if (response.ok) {
                const services = await response.json();
                servicesTableBody.innerHTML = ''; // Cleans the table bedore add information

                if (services.length === 0) {
                    // If there are not services, it shows a related message
                    servicesTableBody.innerHTML = `
                        <tr>
                            <td colspan="8" class="text-center">No services found.</td>
                        </tr>
                    `;
                } else {
                    // Shows the services if there are
                    services.forEach(service => {
                    
                    // To give format to the dates 
					    const formatDate = (dateString) => {
					        const date = new Date(dateString);
					        const year = date.getFullYear();
					        const month = String(date.getMonth() + 1).padStart(2, '0'); 
					        const day = String(date.getDate()).padStart(2, '0');
					        return `${year}/${month}/${day}`;
					    };
                    
                    
                        const row = `
                            <tr>
                                <td>${service.id}</td>
                                <td>${service.serviceType}</td>
                                <td>${service.petName || 'N/A'}</td>
                                <td>${service.sitterUsername || 'N/A'}</td> <!-- to show Pet Sitter -->
                                <td>${formatDate(service.startDate)}</td> <!--formatDate to change without time -->
        					    <td>${formatDate(service.endDate)}</td> <!--formatDate to change without time -->
                                <td>${service.payment} €</td>
                                <td>${service.status}</td>
                                <td>${service.description}</td>                                
                                <td>
                                    <button class="delete-service" data-id="${service.id}" title="Delete Service">
               					     <img src="/images/bin.png" alt="Delete" class="delete-icon">
             						 </button>
                                </td>
                            </tr>
                        `;
                        servicesTableBody.innerHTML += row;
                    });
                }
            } else {
                const errorMessage = await response.text();
                alert(`Failed to load services: ${errorMessage}`);
            }
        } catch (error) {
            console.error('Error loading services:', error);
            alert('An error occurred while loading services.');
        }
    };

    // Event to create a service
    createServiceButton.addEventListener('click', async (event) => {
        event.preventDefault();

        const serviceData = {
            serviceType: serviceType.value,
            petId: petSelection.value || null,
            startDate: startDate.value,
            endDate: endDate.value,
            payment: payment.value,
            description: comments.value
        };

        try {
            const response = await fetch('/services', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(serviceData)
            });

            if (response.ok) {
                alert('Service registered successfully!');
                // Cleans the form after the creation of a service
                serviceType.value = '';
                petSelection.value = '';
                startDate.value = '';
                endDate.value = '';
                payment.value = '';
                comments.value = '';
                // Load the services to show the new added
                await loadServices();
            } else {
                const errorMessage = await response.text();
                alert(`Failed to create service: ${errorMessage}`);
            }
        } catch (error) {
            console.error('Error creating service:', error);
            alert('An unexpected error occurred while creating the service');
        }
    });

    // Event to consult services
    consultServicesButton.addEventListener('click', async () => {
        await loadServices();
        $('#servicesModal').modal('show'); // Show modal created
    });

    // Delete a service
    document.addEventListener('click', async (event) => {
        if (event.target.closest('.delete-service')) {
            const serviceId = event.target.closest('.delete-service').getAttribute('data-id');

            const confirmation = confirm('Are you sure you want to delete this service?');
            if (!confirmation) return;

            try {
                const response = await fetch(`/services/${serviceId}`, {
                    method: 'DELETE',
                });

                if (response.ok) {
                    alert('Service deleted successfully!');
                    await loadServices(); //Load the table after deleting a service
                } else {
                    const errorMessage = await response.text();
                    alert(`Failed to delete service: ${errorMessage}`);
                }
            } catch (error) {
                console.error('Error deleting service:', error);
                alert('An unexpected error occurred while deleting the service.');
            }
        }
    });
});
