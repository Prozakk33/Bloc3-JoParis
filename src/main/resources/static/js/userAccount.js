document.addEventListener("DOMContentLoaded", function () {
    const ticketsPerPage = 10; // Nombre de tickets par page
    let currentPage = 1; // Page actuelle
    getTickets();
});

// Fonction pour récupérer les tickets depuis l'API
async function getTickets() {
    try {
        accessToken = getAccessToken();
    } catch (error) {
        console.error("Erreur lors de la récupération du token :", error);
        window.location.href =
            "/signin.html?errorMessage=Erreur lors de la récupération de votre compte - Reconnectez-vous";
        return;
    }
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
        //console.log("Tickets récupérés :", data);
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
            <td class="px-4 py-3">${formatLocalDateTime(ticket.eventDate)}</td>
            <td class="px-4 py-3">${ticket.eventCity}</td>
            <td class="px-4 py-3">${ticket.eventStadium}</td>
            <td class="px-4 py-3">${ticket.ticketType}</td>
            <td>
                <button id="eventDetailButton" onclick="showQRCode(${
                    ticket.ticketId
                })" class="button text-white bg-blue-600 hover:bg-blue-800 font-medium rounded-full text-sm px-5 py-2.5 text-center me-2 mb-2 dark:bg-blue-600 dark:hover:bg-blue-700">Afficher</button>
            </td>
        `;
        tableBody.appendChild(row);

        // Créer une ligne pour l'accordéon (initialement masquée)
        const accordionRow = document.createElement("tr");
        accordionRow.classList.add("accordion-row", "hidden");
        accordionRow.innerHTML = `
            <td colspan="7">
                <div class="grid accordion-content justify-items-center" id="accordion-${ticket.ticketId}">
                    <!-- Le contenu du QR code sera inséré ici -->
                </div>
            </td>
        `;
        tableBody.appendChild(accordionRow);
    });
}

// Fonction pour formater une date LocalDateTime
function formatLocalDateTime(localDateTime) {
    const date = new Date(localDateTime); // Convertir la chaîne en objet Date
    return new Intl.DateTimeFormat("fr-FR", {
        day: "2-digit",
        month: "long",
        year: "numeric",
        hour: "2-digit",
        minute: "2-digit",
    }).format(date); // Retourner la date formatée
}

async function showQRCode(ticketId) {
    console.log("Show QR Code for ticket ID:", ticketId);
    // Rediriger vers la page de détail du ticket avec l'ID du ticket
    try {
        const response = await fetch(`/tickets/QRCode`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                Authorization: "Bearer " + accessToken,
            },
            body: JSON.stringify({ ticketId }),
        });
        if (!response.ok) {
            throw new Error("Erreur lors de la récupération du QR Code");
            window.location.href =
                "/signin.html?errorMessage=Erreur lors de la récupération du QR Code - Reconnectez-vous";
        }
        const qrCodeData = await response.json();
        // Afficher le QR Code sur la page
        //console.log("QR Code data received:", qrCodeData);
        displayQRCode(qrCodeData, ticketId);
    } catch (error) {
        console.error("Erreur :", error);
    }
}

function displayQRCode(qrCodeData, ticketId) {
    // Créer un accordéon pour afficher le QR Code
    // Insérer le QR code dans l'accordéon correspondant
    const accordionContent = document.getElementById(`accordion-${ticketId}`);
    accordionContent.innerHTML = `
            <div class="accordion-body p-4 text-center justify-items-center h-full">
                <img src="data:image/png;base64,${qrCodeData.qrCodeImageBytes}" alt="QR Code" class="mb-4 justify-items-center"/>
                <p class="text-gray-700">Présentez ce QR code à l'entrée de l'événement.</p>
            </div>
        `;

    // Afficher l'accordéon
    const accordionRow = accordionContent.closest(".accordion-row");
    accordionRow.classList.toggle("hidden");

    // Modifier le bouton associé
    const button = document.querySelector(
        `button[onclick="showQRCode(${ticketId})"]`
    );
    if (!accordionRow.classList.contains("hidden")) {
        // Accordéon affiché
        button.textContent = "Masquer";
        button.classList.remove(
            "bg-blue-600",
            "hover:bg-blue-800",
            "dark:bg-blue-600",
            "dark:hover:bg-blue-700"
        );
        button.classList.add(
            "bg-red-600",
            "hover:bg-red-800",
            "dark:bg-red-600",
            "dark:hover:bg-red-700"
        );
    } else {
        // Accordéon masqué
        button.textContent = "Afficher";
        button.classList.remove(
            "bg-red-600",
            "hover:bg-red-800",
            "dark:bg-red-600",
            "dark:hover:bg-red-700"
        );
        button.classList.add(
            "bg-blue-600",
            "hover:bg-blue-800",
            "dark:bg-blue-600",
            "dark:hover:bg-blue-700"
        );
    }
}
