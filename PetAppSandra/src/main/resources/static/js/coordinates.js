async function getCoordinates(address) {
    try {
        const response = await fetch(`/api/getCoordinates?address=${encodeURIComponent(address)}`);
        if (!response.ok) throw new Error("Failed to fetch coordinates");
        const coordinates = await response.json();
        console.log(`Coordinates for ${address}:`, coordinates);
        return coordinates;
    } catch (error) {
        console.error("Error fetching coordinates:", error);
        return null;
    }
}

// example
getCoordinates("1600 Amphitheatre Parkway, Mountain View, CA");
