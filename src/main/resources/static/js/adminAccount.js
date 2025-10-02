let sportList = [];
let cityList = [];

document.addEventListener("DOMContentLoaded", async function () {
    const form = document.getElementById("newEventForm");

    form.addEventListener("submit", async function (event) {
        event.preventDefault(); // Empêcher le rechargement de la page

        const eventsPerPage = 10; // Nombre d'événements par page
        let currentPage = 1; // Page actuelle

        fetchEvents();

        sportList = await getAllEventsSports();
        cityList = await getAllEventsCities();

        // Fonction pour récupérer les événements depuis l'API
        async function fetchEvents() {
            try {
                accessToken = getAccessToken();

                const response = await fetch("/admin/events", {
                    method: "GET",
                    headers: {
                        Authorization: `Bearer ${accessToken}`,
                    },
                });
                if (!response.ok) {
                    throw new Error("Erreur lors de la récupération des événements");
                    window.location.href =
                        "/signin.html?errorMessage=Erreur lors de la récupération de votre compte - Reconnectez-vous";
                } else {
                    data = await response.json();
                    console.log("Événements récupérés :", data);
                    setupPagination(data);
                }
            } catch (error) {
                console.error("Erreur lors de la récupération du compte :", error);
                window.location.href =
                    "/signin.html?errorMessage=Erreur lors de la récupération de votre compte - Reconnectez-vous";
                return;
            }
            displayEvents(data, currentPage);
        }

        // Fonction pour formater une date LocalDateTime
        function formatLocalDateTime(localDateTime) {
            //console.log("Event date:", localDateTime);
            const date = new Date(localDateTime); // Convertir la chaîne en objet Date
            return new Intl.DateTimeFormat("fr-FR", {
                day: "2-digit",
                month: "long",
                year: "numeric",
                hour: "2-digit",
                minute: "2-digit",
            }).format(date); // Retourner la date formatée
        }

        // Fonction pour afficher les événements dans le tableau
        function displayEvents(events, page) {
            const eventsPerPage = 10; // Nombre d'événements par page
            const tbody = document.querySelector("tbody");
            tbody.innerHTML = ""; // Vider le tableau avant d'ajouter les nouveaux événements

            // Calculer les indices des événements à afficher pour la page actuelle
            const startIndex = (page - 1) * eventsPerPage;
            const endIndex = Math.min(startIndex + eventsPerPage, events.length);

            // Ajouter les événements dans le tableau
            for (let i = startIndex; i < endIndex; i++) {
                const event = events[i];
                const row = document.createElement("tr");
                row.innerHTML = `
                <td class="px-4 py-3">${event.eventTitle}</td>
                <td class="px-4 py-3">${event.eventSport}</td>
                <td class="px-4 py-3">${formatLocalDateTime(event.eventDate)}</td>
                <td class="px-4 py-3">${event.eventCity}</td>
                <td class="px-4 py-3">${event.eventStadium}</td>
                <td class="px-4 py-3">${event.totalTicketsSold || 0}</td>
                <td class="px-4 py-3">${event.totalRevenue ? event.totalRevenue.toFixed(2) + " €" : "0.00 €"}</td>
                <td>
                    <a id="eventDetailButton" action="modifyEvent(${
                        event.id
                    })" class="text-white bg-blue-600 hover:bg-blue-800 focus:outline-none focus:ring-4 focus:ring-blue-300 font-medium rounded-full text-sm px-5 py-2.5 text-center me-2 mb-2 dark:bg-blue-600 dark:hover:bg-blue-700 dark:focus:ring-blue-800">Modifier</a>
                </td>
            `;
                tbody.appendChild(row);
            }
        }

        // Fonction pour configurer la pagination
        function setupPagination(events) {
            const paginationDiv = document.getElementById("pagination");
            paginationDiv.innerHTML = ""; // Vider la pagination avant de la recréer

            const totalPages = Math.ceil(events.length / eventsPerPage);

            // Créer les boutons de pagination
            for (let i = 1; i <= totalPages; i++) {
                const button = document.createElement("button");
                button.textContent = i;
                button.className = "px-4 py-2 mx-1 bg-blue-600 text-white rounded hover:bg-blue-800";
                if (i === currentPage) {
                    button.classList.add("bg-blue-800"); // Mettre en surbrillance la page actuelle
                }
                button.addEventListener("click", () => {
                    currentPage = i;
                    displayEvents(events, currentPage); // Afficher les événements pour la page sélectionnée
                });
                paginationDiv.appendChild(button);
            }
        }
    });
});

// ----------------------------------------------------------------------------------------
// Gestion du formulaire de création d'un nouvel événement
async function getAllEventsSports() {
    try {
        const response = await fetch("/event/sportList", {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
            },
        });
        if (!response.ok) {
            throw new Error("Erreur lors de la récupération des sports");
        }
        const data = await response.json();
        // Trier les sports par ordre alphabétique
        data.sort((a, b) => a.localeCompare(b, "fr", { sensitivity: "base" }));
        console.log("Liste des sports récupérée :", data);
        return data; // Retourner les données
    } catch (error) {
        console.error("Erreur lors de la récupération des sports :", error);
        return []; // Retourner un tableau vide en cas d'erreur
    }
}

async function getAllEventsCities() {
    try {
        const response = await fetch("/event/cityList", {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
            },
        });
        if (!response.ok) {
            throw new Error("Erreur lors de la récupération des villes");
        }
        const data = await response.json();
        // Trier les villes par ordre alphabétique
        data.sort((a, b) => a.localeCompare(b, "fr", { sensitivity: "base" }));
        console.log("Liste des villes récupérée :", data);
        return data; // Retourner les données
    } catch (error) {
        console.error("Erreur lors de la récupération des villes :", error);
        return []; // Retourner un tableau vide en cas d'erreur
    }
}

function showNewEventForm() {
    // Afficher le formulaire de création d'un nouvel événement
    const formContainer = document.getElementById("NewEventFormContainer");
    const sportOptions = document.getElementById("newSport");
    const cityOptions = document.getElementById("newCity");

    console.log("Sport List:", sportList);
    console.log("City List:", cityList);

    // Ajouter les options de sport au formulaire
    sportList.forEach((sport) => {
        const option = new Option(sport, sport);
        sportOptions.add(option);
    });

    // Ajouter les options de ville au formulaire
    cityList.forEach((city) => {
        const option = new Option(city, city);
        cityOptions.add(option);
    });

    formContainer.classList.remove("hidden");
}

function hideNewEventForm() {
    // Masquer le formulaire de création d'un nouvel événement
    const formContainer = document.getElementById("NewEventFormContainer");
    formContainer.classList.add("hidden");
}

// Show the modify event form
function showModifyEventForm(eventId) {
    // Créer un accordéon pour afficher le QR Code
    // Insérer le QR code dans l'accordéon correspondant
    const accordionContent = document.getElementById(`accordion-${eventId}`);
    accordionContent.innerHTML = `
            <div class="accordion-body p-4 text-center justify-items-center h-full">
                <img src="data:image/png;base64,${qrCodeData.qrCodeImageBytes}" alt="QR Code" class="mb-4 justify-items-center"/>
                <p class="text-gray-700 text-font-bold">Présentez ce QR code à l'entrée de l'événement.</p>
            </div>
        `;

    // Afficher l'accordéon
    const accordionRow = accordionContent.closest(".accordion-row");
    accordionRow.classList.toggle("hidden");

    // Modifier le bouton associé
    const button = document.querySelector(`button[onclick="showQRCode(${ticketId})"]`);
    if (!accordionRow.classList.contains("hidden")) {
        // Accordéon affiché
        button.textContent = "Masquer";
        button.classList.remove("bg-blue-600", "hover:bg-blue-800", "dark:bg-blue-600", "dark:hover:bg-blue-700");
        button.classList.add("bg-red-600", "hover:bg-red-800", "dark:bg-red-600", "dark:hover:bg-red-700");
    } else {
        // Accordéon masqué
        button.textContent = "Afficher";
        button.classList.remove("bg-red-600", "hover:bg-red-800", "dark:bg-red-600", "dark:hover:bg-red-700");
        button.classList.add("bg-blue-600", "hover:bg-blue-800", "dark:bg-blue-600", "dark:hover:bg-blue-700");
    }
}

function createEvent() {
    console.log("Création d'un nouvel événement");
    // Récupérer les valeurs du formulaire
    const eventTitle = document.getElementById("newTitle").value;
    const eventSport = document.getElementById("newSport").value;
    const eventDate = document.getElementById("newDate").value;
    const eventCity = document.getElementById("newCity").value;
    const eventStadium = document.getElementById("newStadium").value;
    const totalTickets = document.getElementById("newTotalTickets").value;
    const ticketPrice = document.getElementById("newTicketPrice").value;

    console.log("Données du formulaire :", {
        eventTitle,
        eventSport,
        eventDate,
        eventCity,
        eventStadium,
        totalTickets,
        ticketPrice,
    });

    // Valider les champs du formulaire (exemple simple)
    if (!eventTitle || !eventSport || !eventDate || !eventCity || !eventStadium || !totalTickets || !ticketPrice) {
        alert("Veuillez remplir tous les champs.");
        return;
    }

    // Si tous les champs sont valides, vous pouvez procéder à la création de l'événement
}

// Modifier un évènement
function modifyEvent(eventId) {
    console.log("Modifier l'événement avec l'ID :", eventId);
}
