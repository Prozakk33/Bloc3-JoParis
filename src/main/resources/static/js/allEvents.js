document.addEventListener("DOMContentLoaded", function () {
    const apiUrl = "/event/all"; // URL de l'API
    const eventsPerPage = 10; // Nombre d'événements par page
    let currentPage = 1; // Page actuelle

    // Fonction pour récupérer les événements depuis l'API
    function fetchEvents() {
        fetch(apiUrl)
            .then((response) => {
                if (!response.ok) {
                    throw new Error(
                        "Erreur lors de la récupération des événements"
                    );
                }
                return response.json(); // Convertir la réponse en JSON
            })
            .then((events) => {
                displayEvents(events, currentPage); // Afficher les événements pour la page actuelle
                setupPagination(events); // Configurer la pagination
            })
            .catch((error) => {
                console.error("Erreur :", error);
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

    // Fonction pour afficher les événements dans le tableau
    function displayEvents(events, page) {
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
                <td class="px-4 py-3">${event.title}</td>
                <td class="px-4 py-3">${event.sport}</td>
                <td class="px-4 py-3">${formatLocalDateTime(event.date)}</td>
                <td class="px-4 py-3">${event.city}</td>
                <td class="px-4 py-3">${event.stadium}</td>
                <td>
                    <a id="eventDetailButton" href="/eventDetail.html?id=${
                        event.id
                    }" class="text-white bg-blue-600 hover:bg-blue-800 focus:outline-none focus:ring-4 focus:ring-blue-300 font-medium rounded-full text-sm px-5 py-2.5 text-center me-2 mb-2 dark:bg-blue-600 dark:hover:bg-blue-700 dark:focus:ring-blue-800">Détails</a>
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
            button.className =
                "px-4 py-2 mx-1 bg-blue-600 text-white rounded hover:bg-blue-800";
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

    // Appeler la fonction pour récupérer les événements
    fetchEvents();
});
