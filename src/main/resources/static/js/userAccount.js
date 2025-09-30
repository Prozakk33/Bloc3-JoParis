document.addEventListener("DOMContentLoaded", function () {
    const ticketsPerPage = 10; // Nombre de tickets par page
    let currentPage = 1; // Page actuelle
    getTickets();
});

// Fonction pour récupérer les tickets depuis l'API
async function getTickets() {
    // Récupérer le token JWT depuis le localStorage
    const token = localStorage.getItem("jwtToken");

    // Vérifier si le token existe
    const jwtToken = localStorage.getItem("jwtToken");
    if (!jwtToken) {
        window.location.href =
            "/signin.html?errorMessage=Connectez-vous pour accéder au paiement"; // Redirige vers la page de connexion si aucun token n'est trouvé
        return;
    }

    console.log(
        "CheckUserAccount - jwtToken récupéré depuis le localStorage :",
        jwtToken
    );

    // Extraire l'accessToken du jwtToken
    let accessToken;
    try {
        const parsedToken = JSON.parse(jwtToken); // Convertit la chaîne JSON en objet
        accessToken = parsedToken.accessToken; // Récupère le champ accessToken
    } catch (e) {
        console.error(
            "CheckUserAccount - Erreur lors du parsing du jwtToken :",
            e
        );
        window.location.href =
            "/signin.html?errorMessage=Erreur lors de la lecture du Token - Reconnectez-vous"; // Redirige si le parsing échoue
        return;
    }

    // Vérifier si l'accessToken est présent
    if (!accessToken) {
        console.error(
            "CheckUserAccount - Aucun accessToken trouvé dans jwtToken. Redirection vers la page de connexion."
        );
        window.location.href =
            "/signin.html?errorMessage=Aucun accessToken trouvé - Reconnectez-vous"; // Redirige si accessToken est absent
        return;
    }
    // Effectuer une requête POST avec l'en-tête Authorization
    console.log(
        "CheckUserAccount - Envoi de la requête POST à /tickets/list avec l'accessToken :",
        accessToken
    );
    try {
        const response = await fetch("/tickets/list", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                Authorization: "Bearer " + accessToken,
            },
        });
        if (!response.ok) {
            window.location.href =
                "/signin.html?errorMessage=Erreur lors de la récupération des tickets - Reconnectez-vous";
            throw new Error("Erreur lors de la récupération des tickets");
        }
        const data = await response.json();
        console.log("Tickets récupérés :", data);
        displayTickets(data);
    } catch (error) {
        console.error("Erreur :", error);
    }
}

// Fonction pour afficher les tickets dans le tableau
function displayTickets(tickets) {
    console.log("Displaying tickets:", tickets);
    const tableBody = document.getElementById("ticketsTableBody");
    tableBody.innerHTML = ""; // Vider le corps du tableau avant d'ajouter de nouvelles lignes

    if (tickets.length === 0) {
        console.log("No tickets found for the user.");
        const row = document.createElement("tr");
        row.innerHTML = `<td colspan="6">Aucun ticket trouvé.</td>`;
        tableBody.appendChild(row);
        return;
    }

    tickets.forEach((ticket) => {
        console.log("Processing ticket:", ticket);
        const row = document.createElement("tr");
        row.innerHTML = `
            <td class="px-4 py-3">${ticket.eventTitle}</td>
            <td class="px-4 py-3">${ticket.eventSport}</td>
            <td class="px-4 py-3">${ticket.eventDate}</td>
            <td class="px-4 py-3">${ticket.eventCity}</td>
            <td class="px-4 py-3">${ticket.eventStadium}</td>
            <td class="px-4 py-3">${ticket.ticketType}</td>
            <td>
                <a id="eventDetailButton" href="/ticketQRCode.html?id=${ticket.id}" class="text-white bg-blue-600 hover:bg-blue-800 focus:outline-none focus:ring-4 focus:ring-blue-300 font-medium rounded-full text-sm px-5 py-2.5 text-center me-2 mb-2 dark:bg-blue-600 dark:hover:bg-blue-700 dark:focus:ring-blue-800">Afficher</a>
            </td>
        `;
        tableBody.appendChild(row);
    });
}
