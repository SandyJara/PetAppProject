let map;

// start the map
function initMap() {
    map = new google.maps.Map(document.getElementById("map"), {
        center: { lat: 53.349851529305866, lng:  -6.26009243725504 }, // INITIAL COORDINATES TO UPDATE
        zoom: 12,
    });

    // Load the services that I want to show
    loadPendingServices();
}

// Add markers in the map related with my services
function addMultipleMarkers(services) {
    services.forEach((service) => {
        if (service.latitude && service.longitude) { // Ensure coordinates exist
            const marker = new google.maps.Marker({
                position: { lat: service.latitude, lng: service.longitude },
                map: map,
                title: `Service Available: ${service.type}`, // Marker title
            });

            // InfoWindow to show service details
            const infoWindow = new google.maps.InfoWindow({
                content: `
                    <div>
                        <h4>Service Details</h4>
                        <p><strong>Type:</strong> ${service.type}</p>
                        <p><strong>Owner:</strong> ${service.name}</p>
                        <p><strong>Description:</strong> ${service.description || "No description provided"}</p>
                    </div>
                `,
            });

            // Add click listener to marker to show InfoWindow
            marker.addListener("click", () => {
                infoWindow.open(map, marker);
            });
        } else {
            console.error("Invalid coordinates for service:", service);
        }
    });
}

// Load pending services from the backend
async function loadPendingServices() {
    try {
        const response = await fetch("/api/maps/services");
        if (!response.ok) throw new Error("Failed to fetch pending services.");

        const services = await response.json(); // Services with coordinates 
        console.log("Pending services loaded:", services);

        // Add markers in the map
        addMultipleMarkers(services);
    } catch (error) {
        console.error("Error loading pending services:", error);
    }
}

// start the map with the page
window.onload = () => initMap();
