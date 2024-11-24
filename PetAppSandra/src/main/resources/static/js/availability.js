<script>
    document.addEventListener("DOMContentLoaded", function () {
        // Obtener el contenedor de disponibilidades
        const availabilityList = document.getElementById("availability-list");

        // Hacer una petición al backend
        fetch("/petsitter/availability")
            .then((response) => {
                if (response.ok) return response.json();
                else throw new Error("Failed to fetch availability");
            })
            .then((availabilities) => {
                // Limpiar el contenedor
                availabilityList.innerHTML = "";

                if (availabilities.length === 0) {
                    availabilityList.innerHTML = "<p>No availability set. Create one below!</p>";
                } else {
                    availabilities.forEach((availability) => {
                        const div = document.createElement("div");
                        div.classList.add("availability-item");
                        div.innerHTML = `
                            <p><strong>Type:</strong> ${availability.serviceType}</p>
                            <p><strong>Species:</strong> ${availability.preferredSpecies || "Any"}</p>
                            <p><strong>Dates:</strong> ${availability.startDate} to ${availability.endDate}</p>
                            <p><strong>Area:</strong> ${availability.area}</p>
                            <button class="btn btn-warning btn-sm">Edit</button>
                            <button class="btn btn-danger btn-sm">Delete</button>
                        `;
                        availabilityList.appendChild(div);
                    });
                }
            })
            .catch((error) => {
                console.error("Error fetching availability:", error);
                availabilityList.innerHTML = "<p>Error loading availability. Please try again later.</p>";
            });
    });
</script>