let sportList = [];
let cityList = [];

document.addEventListener("DOMContentLoaded", async function () {
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
                //throw new Error("Erreur lors de la récupération des événements");
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
            row.id = `${event.eventId}`;

            row.innerHTML = `
                <td class="px-4 py-3">${event.eventTitle}</td>
                <td class="px-4 py-3">${event.eventSport}</td>
                <td class="px-4 py-3">${formatLocalDateTime(event.eventDate)}</td>
                <td class="px-4 py-3">${event.eventCity}</td>
                <td class="px-4 py-3">${event.eventStadium}</td>
                <td class="px-4 py-3">${event.totalTicketsSold || 0}</td>
                <td class="px-4 py-3">${event.totalRevenue ? event.totalRevenue.toFixed(2) + " €" : "0.00 €"}</td>
                <td>
                    <button id="eventModifyButton" onclick="showModifyEventForm(${
                        event.eventId
                    })" class="text-white bg-blue-600 hover:bg-blue-800 focus:outline-none focus:ring-4 focus:ring-blue-300 font-medium rounded-full text-sm px-5 py-2.5 text-center me-2 mb-2 dark:bg-blue-600 dark:hover:bg-blue-700 dark:focus:ring-blue-800">Modifier</button>
                </td>
            `;
            tbody.appendChild(row);

            // Créer une ligne pour l'accordéon (initialement masquée)
            const accordionRow = document.createElement("tr");
            accordionRow.classList.add("accordion-row", "hidden");
            accordionRow.innerHTML = `
            <td colspan="8">
                <div class="grid accordion-content justify-items-center" id="accordion-${event.eventId}">
                    <!-- Le contenu du QR code sera inséré ici -->
                </div>
            </td>
        `;
            tbody.appendChild(accordionRow);
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

document.getElementById("newEventForm").addEventListener("submit", async function (event) {
    event.preventDefault();

    const title = document.getElementById("newTitle").value;
    const description = document.getElementById("newDescription").value;
    const sport = document.getElementById("newSport").value;
    const date = document.getElementById("newDate").value;
    const city = document.getElementById("newCity").value;
    const stadium = document.getElementById("newStadium").value;
    const capacity = document.getElementById("newCapacity").value;
    const price = document.getElementById("newPrice").value;

    console.log("Données du formulaire :", {
        title,
        description,
        sport,
        date,
        city,
        stadium,
        capacity,
        price,
    });

    if (title == "") {
        document.getElementById("titleError").innerText = "Le titre doit être saisi.";
        return;
    } else {
        document.getElementById("titleError").innerText = "";
    }
    if (description == "") {
        document.getElementById("descriptionError").innerText = "La description doit être saisie.";
        return;
    } else {
        document.getElementById("descriptionError").innerText = "";
    }
    if (sport == "") {
        document.getElementById("sportError").innerText = "Le sport doit être sélectionné.";
        return;
    } else {
        document.getElementById("sportError").innerText = "";
    }
    if (date == "") {
        document.getElementById("dateError").innerText = "La date doit être saisie.";
        return;
    } else {
        document.getElementById("dateError").innerText = "";
    }
    if (city == "") {
        document.getElementById("cityError").innerText = "La ville doit être sélectionnée.";
        return;
    } else {
        document.getElementById("cityError").innerText = "";
    }
    if (stadium == "") {
        document.getElementById("stadiumError").innerText = "Le stade doit être saisi.";
        return;
    } else {
        document.getElementById("stadiumError").innerText = "";
    }
    if (capacity == "") {
        document.getElementById("capacityError").innerText = "La capacité doit être saisie.";
        return;
    } else {
        document.getElementById("capacityError").innerText = "";
    }
    if (price == "") {
        document.getElementById("priceError").innerText = "Le prix doit être saisi.";
        return;
    } else {
        document.getElementById("priceError").innerText = "";
    }
    createEvent();
    hideNewEventForm();
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
    document.getElementById("newTitle").value = "";
    document.getElementById("newDescription").value = "";
    document.getElementById("newSport").value = "";
    document.getElementById("newDate").value = "";
    document.getElementById("newCity").value = "";
    document.getElementById("newStadium").value = "";
    document.getElementById("newCapacity").value = "";
    document.getElementById("newPrice").value = "";
    document.getElementById("titleError").innerText = "";
    document.getElementById("descriptionError").innerText = "";
    document.getElementById("sportError").innerText = "";
    document.getElementById("dateError").innerText = "";
    document.getElementById("cityError").innerText = "";
    document.getElementById("stadiumError").innerText = "";
    document.getElementById("capacityError").innerText = "";
    document.getElementById("priceError").innerText = "";
}

// Show the modify event form
async function showModifyEventForm(eventId) {
    // Créer un accordéon pour afficher le formulaire de modification
    const accordionContent = document.getElementById(`accordion-${eventId}`);
    accordionContent.innerHTML = `
            <div class="accordion-body text-center justify-items-center h-full">
                <div id="modifyEventFormContainer" class="w-full bg-white p-8 rounded-lg shadow-md">
            <h2 class="text-2xl font-bold mb-5 text-center">Modifier un Événement</h2>
                <form id="modifyEventForm-${eventId}" class="flex flex-col">
                <div class="flex w-full sm:flex-row flex-col mx-auto px-8 sm:space-x-4 sm:space-y-0 space-y-4 sm:px-0 items-end">
                    <!-- Champ Title -->
                    <div class="relative flex-grow w-full">
                        <label for="modifiedTitle-${eventId}" class="leading-7 text-sm text-gray-600">Title</label>
                        <input type="text" id="modifiedTitle-${eventId}" maxlength="80" name="title" class="w-full bg-gray-100 bg-opacity-50 rounded border border-gray-300 focus:border-indigo-500 focus:bg-transparent focus:ring-2 focus:ring-indigo-200 text-base outline-none text-gray-700 py-1 px-3 leading-8 transition-colors duration-200 ease-in-out">
                        <div id="modifiedTitleError-${eventId}" class="text-red-500 text-sm"></div>
                    </div>

                    <!-- Champ Description -->
                    <div class="relative flex-grow w-full">
                        <label for="modifiedDescription-${eventId}" class="leading-7 text-sm text-gray-600">Description</label>
                        <input type="text" id="modifiedDescription-${eventId}" maxlength="255" name="description" class="w-full bg-gray-100 bg-opacity-50 rounded border border-gray-300 focus:border-indigo-500 focus:bg-transparent focus:ring-2 focus:ring-indigo-200 text-base outline-none text-gray-700 py-1 px-3 leading-8 transition-colors duration-200 ease-in-out">
                        <div id="modifiedDescriptionError-${eventId}" class="text-red-500 text-sm"></div>
                    </div>

                    <!-- Champ Date -->
                    <div class="relative flex-grow w-full">
                        <label for="modifiedDate-${eventId}" class="leading-7 text-sm text-gray-600">Date</label>
                        <input type="datetime-local" id="modifiedDate-${eventId}" name="date" class="w-full bg-gray-100 bg-opacity-50 rounded border border-gray-300 focus:border-indigo-500 focus:bg-transparent focus:ring-2 focus:ring-indigo-200 text-base outline-none text-gray-700 py-1 px-3 leading-8 transition-colors duration-200 ease-in-out">
                        <div id="modifiedDateError-${eventId}" class="text-red-500 text-sm"></div>
                    </div>
                </div>

                <div class="flex w-full sm:flex-row flex-col mx-auto px-8 sm:space-x-4 sm:space-y-0 space-y-4 sm:px-0 items-end">

                    <!-- Champ City -->
                    <div class="relative flex-grow w-full">
                        <label for="modifiedCity-${eventId}" class="leading-7 text-sm text-gray-600">City</label>
                            <select id="modifiedCity-${eventId}" name="City" class="w-full bg-gray-100 bg-opacity-50 rounded border border-gray-300 focus:border-indigo-500 focus:bg-transparent focus:ring-2 focus:ring-indigo-200 text-base outline-none text-gray-700 py-1 px-3 leading-8 transition-colors duration-200 ease-in-out">
                                <option value="" disabled selected>Choisissez une ville</option>
                            </select>
                            <div id="modifiedCityError-${eventId}" class="text-red-500 text-sm"></div>
                    </div>

                    <!-- Champ Sport -->
                    <div class="relative flex-grow w-full">
                        <label for="modifiedSport-${eventId}" class="leading-7 text-sm text-gray-600">Sport</label>
                        <select id="modifiedSport-${eventId}" name="sport" class="w-full bg-gray-100 bg-opacity-50 rounded border border-gray-300 focus:border-indigo-500 focus:bg-transparent focus:ring-2 focus:ring-indigo-200 text-base outline-none text-gray-700 py-1 px-3 leading-8 transition-colors duration-200 ease-in-out">
                            <option value="" disabled selected>Choisissez un sport</option>
                        </select>
                        <div id="modifiedSportError-${eventId}" class="text-red-500 text-sm"></div>
                    </div>
                </div>
                
                <div class="flex w-full sm:flex-row flex-col mx-auto px-8 sm:space-x-4 sm:space-y-0 space-y-4 sm:px-0 items-end">

                    <!-- Champ Stadium -->
                    <div class="relative flex-grow w-full">
                        <label for="modifiedStadium-${eventId}" class="leading-7 text-sm text-gray-600">Stadium</label>
                            <input type="text" id="modifiedStadium-${eventId}" maxlength="80" class="w-full bg-gray-100 bg-opacity-50 rounded border border-gray-300 focus:border-indigo-500 focus:bg-transparent focus:ring-2 focus:ring-indigo-200 text-base outline-none text-gray-700 py-1 px-3 leading-8 transition-colors duration-200 ease-in-out" />
                            <div id="modifiedStadiumError-${eventId}" class="text-red-500 text-sm"></div>
                    </div>

                    <!-- Champ Capacity -->
                    <div class="relative flex-grow w-full">
                        <label for="modifiedCapacity-${eventId}" class="leading-7 text-sm text-gray-600">Capacity</label>
                        <input type="number" min="1" max="200000" id="modifiedCapacity-${eventId}" class="w-full bg-gray-100 bg-opacity-50 rounded border border-gray-300 focus:border-indigo-500 focus:bg-transparent focus:ring-2 focus:ring-indigo-200 text-base outline-none text-gray-700 py-1 px-3 leading-8 transition-colors duration-200 ease-in-out" />
                        <div id="modifiedCapacityError-${eventId}" class="text-red-500 text-sm"></div>
                    </div>

                    <!-- Champ Price -->
                    <div class="relative flex-grow w-full">
                        <label for="modifiedPrice-${eventId}" class="leading-7 text-sm text-gray-600">Price</label>
                        <input type="number" id="modifiedPrice-${eventId}" min="1" max="10000" class="w-full bg-gray-100 bg-opacity-50 rounded border border-gray-300 focus:border-indigo-500 focus:bg-transparent focus:ring-2 focus:ring-indigo-200 text-base outline-none text-gray-700 py-1 px-3 leading-8 transition-colors duration-200 ease-in-out" />
                        <div id="modifiedPriceError-${eventId}" class="text-red-500 text-sm"></div>
                    </div>
                </div>
                <!-- Boutons -->
                 <div class="lg:w-2/3 w-full mx-auto px-8 sm:px-0 mt-4">   
                    <button class="text-white bg-indigo-500 border-0 py-2 px-8 focus:outline-none hover:bg-indigo-600 rounded text-lg">Enregistrer</button>
                    <button onclick="hideModifyEventForm(${eventId})" class="ml-4 text-red-500 text-font-bold">Annuler</button>
                </div>
            </form>
            </div>
            </div>
        `;

    // Afficher l'accordéon
    const accordionRow = accordionContent.closest(".accordion-row");
    accordionRow.classList.toggle("hidden");

    const sportOptions = document.getElementById("modifiedSport-" + eventId);
    const cityOptions = document.getElementById("modifiedCity-" + eventId);

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

    // Requête vers le serveur pour récupérer les informations de l'évènements
    let eventData = {};
    try {
        const response = await fetch(`/event/${eventId}`, {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
            },
        });
        if (!response.ok) {
            throw new Error("Erreur lors de la récupération de l'événement");
        }
        eventData = await response.json();
    } catch (error) {
        console.error("Erreur lors de la récupération de l'événement :", error);
    }
    // Replissage avec les valeurs actuelles de l'événement
    document.getElementById(`modifiedTitle-${eventId}`).value = eventData.title;
    document.getElementById(`modifiedDescription-${eventId}`).value = eventData.description;
    document.getElementById(`modifiedDate-${eventId}`).value = eventData.date.replace(" ", "T");
    document.getElementById(`modifiedCity-${eventId}`).value = eventData.city;
    document.getElementById(`modifiedSport-${eventId}`).value = eventData.sport;
    document.getElementById(`modifiedStadium-${eventId}`).value = eventData.stadium;
    document.getElementById(`modifiedCapacity-${eventId}`).value = eventData.capacity;
    document.getElementById(`modifiedPrice-${eventId}`).value = eventData.price;
}

async function createEvent() {
    console.log("Création d'un nouvel événement");
    // Récupérer les valeurs du formulaire
    const eventTitle = document.getElementById("newTitle").value;
    const eventSport = document.getElementById("newSport").value;
    const eventDate = document.getElementById("newDate").value;
    const eventCity = document.getElementById("newCity").value;
    const eventStadium = document.getElementById("newStadium").value;
    const eventDescription = document.getElementById("newDescription").value;
    const eventCapacity = document.getElementById("newCapacity").value;
    const eventPrice = document.getElementById("newPrice").value;

    console.log("Données du formulaire :", {
        eventTitle,
        eventSport,
        eventDate,
        eventCity,
        eventStadium,
        eventDescription,
        eventCapacity,
        eventPrice,
    });

    const token = getAccessToken();

    try {
        const response = await fetch("/admin/createEvent", {
            method: "POST",
            headers: {
                Authorization: `Bearer ${token}`,
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                eventTitle,
                eventSport,
                eventDate,
                eventCity,
                eventStadium,
                eventDescription,
                eventCapacity,
                eventPrice,
            }),
        });
        if (!response.ok) {
            throw new Error("Erreur lors de la création de l'événement");
        } else {
            const data = await response.json();
            console.log("Événement créé avec succès :", data);
        }
    } catch (error) {
        console.error("Erreur lors de la création de l'événement :", error);
    }
}

document.addEventListener("submit", async function (event) {
    if (event.target && event.target.id.startsWith("modifyEventForm-")) {
        console.log("Formulaire de modification soumis :", event.target.id);
        event.preventDefault();
        const eventId = event.target.id.split("-")[1];
        console.log("ID de l'événement à modifier :", eventId);

        await modifyEvent(eventId);
    }
});

// Modifier un évènement
async function modifyEvent(eventId) {
    console.log("Modifier l'événement avec l'ID :", eventId);
    // Récupérer les valeurs du formulaire

    // Check si formulaire est OK
    if (document.getElementById(`modifiedTitle-${eventId}`).value == "") {
        document.getElementById(`modifiedTitleError-${eventId}`).innerText = "Le titre doit être saisi.";
        return;
    } else {
        document.getElementById(`modifiedTitleError-${eventId}`).innerText = "";
    }
    if (document.getElementById(`modifiedDescription-${eventId}`).value == "") {
        document.getElementById(`modifiedDescriptionError-${eventId}`).innerText = "La description doit être saisie.";
        return;
    } else {
        document.getElementById(`modifiedDescriptionError-${eventId}`).innerText = "";
    }
    if (document.getElementById(`modifiedSport-${eventId}`).value == "") {
        document.getElementById(`modifiedSportError-${eventId}`).innerText = "Le sport doit être sélectionné.";
        return;
    } else {
        document.getElementById(`modifiedSportError-${eventId}`).innerText = "";
    }
    if (document.getElementById(`modifiedDate-${eventId}`).value == "") {
        document.getElementById(`modifiedDateError-${eventId}`).innerText = "La date doit être saisie.";
        return;
    } else {
        document.getElementById(`modifiedDateError-${eventId}`).innerText = "";
    }
    if (document.getElementById(`modifiedCity-${eventId}`).value == "") {
        document.getElementById(`modifiedCityError-${eventId}`).innerText = "La ville doit être sélectionnée.";
        return;
    } else {
        document.getElementById(`modifiedCityError-${eventId}`).innerText = "";
    }
    if (document.getElementById(`modifiedStadium-${eventId}`).value == "") {
        document.getElementById(`modifiedStadiumError-${eventId}`).innerText = "Le stade doit être saisi.";
        return;
    } else {
        document.getElementById(`modifiedStadiumError-${eventId}`).innerText = "";
    }

    const eventTitle = document.getElementById(`modifiedTitle-${eventId}`).value;
    const eventSport = document.getElementById(`modifiedSport-${eventId}`).value;
    const eventDate = document.getElementById(`modifiedDate-${eventId}`).value;
    const eventCity = document.getElementById(`modifiedCity-${eventId}`).value;
    const eventStadium = document.getElementById(`modifiedStadium-${eventId}`).value;
    const eventDescription = document.getElementById(`modifiedDescription-${eventId}`).value;
    const eventCapacity = document.getElementById(`modifiedCapacity-${eventId}`).value;
    const eventPrice = document.getElementById(`modifiedPrice-${eventId}`).value;

    console.log("Données du formulaire :", {
        eventId,
        eventTitle,
        eventSport,
        eventDate,
        eventCity,
        eventStadium,
        eventDescription,
        eventCapacity,
        eventPrice,
    });

    const token = getAccessToken();
    // Envoyer les données modifiées au serveur (à implémenter)
    try {
        const response = await fetch("/admin/updateEvent", {
            method: "PUT",
            headers: {
                Authorization: `Bearer ${token}`,
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                eventId,
                eventTitle,
                eventSport,
                eventDate,
                eventCity,
                eventStadium,
                eventDescription,
                eventCapacity,
                eventPrice,
            }),
        });
        if (!response.ok) {
            console.log("Response not ok:", response);
            throw new Error("Fetch - Erreur lors de la modification de l'événement");
        } else {
            const data = await response.text();
            console.log("Événement modifié avec succès :", data);
        }
    } catch (error) {
        console.error("Erreur lors de la modification de l'événement :", error);
    }

    hideModifyEventForm(eventId);
    alert("Événement modifié avec succès !");
    // Recharger la page pour afficher les modifications
    window.location.reload();
}

function hideModifyEventForm(eventId) {
    // Masquer le formulaire de modification
    const accordionRow = document.getElementById(`accordion-${eventId}`).closest(".accordion-row");
    accordionRow.classList.add("hidden");
    accordionRow.innerHTML = "";
}
