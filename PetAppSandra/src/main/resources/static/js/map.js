let map;

// start the map
function initMap() {
    map = new google.maps.Map(document.getElementById("map"), {
        center: { lat: 37.7749, lng: -122.4194 }, // Coordenadas iniciales
        zoom: 12,
    });

    // Load the services that I want to show
    loadPendingServices();
}

// Add markers in the map related with my services
function addMultipleMarkers(services) {
    services.forEach((service) => {
        new google.maps.Marker({
            position: { lat: service.latitude, lng: service.longitude },
            map: map,
            title: `${service.name} - ${service.type}`, // Title 
        });
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
