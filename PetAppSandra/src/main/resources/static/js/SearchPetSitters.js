document.addEventListener('DOMContentLoaded', () => {
    const availableForFilter = document.getElementById('available-for');
    const statusFilter = document.getElementById('status');
    const searchPetSittersButton = document.getElementById('searchPetSittersButton');

    searchPetSittersButton.addEventListener('click', async (event) => {
        event.preventDefault();

        const serviceType = availableForFilter.value;
        const statusProfile = statusFilter.value;

        try {
            const response = await fetch(`/preferences/search?serviceType=${serviceType}&statusProfile=${statusProfile}`, {
                method: 'GET',
            });

            if (!response.ok) {
                throw new Error(`Failed to fetch pet sitters. Status: ${response.status}`);
            }

            const sitters = await response.json();
            console.log('Sitters fetched:', sitters);

            populateResultsTable(sitters);

            $('#resultsModal').modal('show'); // show modal
        } catch (error) {
            console.error('Error fetching pet sitters:', error);
            alert('Failed to fetch pet sitters. Please try again later.');
        }
    });
});

    // fill the table with the information
    function populateResultsTable(sitters) {
    resultsTableBody.innerHTML = ''; 

    if (sitters.length === 0) {
        const row = document.createElement('tr');
        row.innerHTML = `<td colspan="3" class="text-center">No sitters found.</td>`;
        resultsTableBody.appendChild(row);
        return;
    }

    sitters.forEach(sitter => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${sitter.sitterId}</td>
            <td>
                <a href="/petSitterPublicProfile.html?sitterId=${sitter.sitterId}" style="color: #d87db5; text-decoration: none;">
                    ${sitter.username}
                </a>
            </td>
        `;
        resultsTableBody.appendChild(row);
    });
}