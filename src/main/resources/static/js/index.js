document.addEventListener("DOMContentLoaded", function () {

    const sportImages = {
        football: "/img/football.png",
        rugby: "/img/rugby.png",
        basketball: "/img/basket.png",
        judo: "/img/judo.png",
        athletisme: "/img/athletisme.png",
        cheval: "/img/cheval.png",
        escrime: "/img/escrime.png",
        golf: "/img/golf.png",
        handball: "/img/handball.png",
        natation: "/img/natation.png",
        plongeon: "/img/plongeon.webp",
        skateboard: "/img/skateboard.jpg",
        triathlon: "/img/triathlon.jpg",
        voile: "/img/voile.webp"
    };
    // Appeler l'API pour récupérer les 3 événements
    fetch("/event/three")
        .then(response => {
            if (!response.ok) {
                throw new Error("Erreur lors de la récupération des événements");
            }
            return response.json(); // Convertir la réponse en JSON
        })
        .then(events => {
            // Sélectionner la div où les événements seront affichés
            const eventList = document.getElementById("eventList");

            // Vider la div avant d'ajouter les nouveaux événements
            eventList.innerHTML = "";

            // Parcourir les événements et créer une div pour chacun
            events.forEach(event => {
                const eventDiv = document.createElement("div");
                eventDiv.className = "p-4 md:w-1/3 sm:mb-0 mb-6";

                eventDiv.innerHTML = `
                    <div class="rounded-lg h-64 overflow-hidden">
                        <img src="${sportImages[event.sport.toLowerCase()]}"" alt="content" class="object-contain object-center h-full w-full">
                    </div>
                    <h2 class="text-xl font-medium title-font text-gray-900 mt-5">${event.title}</h2>
                    <h4 class="text-md font-medium title-font text-gray-600 mt-1">${event.sport}</h4>
                    <h4 class="text-md font-medium title-font text-gray-600 mt-1">${event.date}</h4>
                    <p class="text-base leading-relaxed mt-2">${event.description}</p>
                    <a class="text-indigo-500 inline-flex text-blue-600 items-center mt-3">Détails
                        <svg fill="none" stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2" class="w-4 h-4 ml-2" viewBox="0 0 24 24">
                            <path d="M5 12h14M12 5l7 7-7 7"></path>
                        </svg>
                    </a>
                `;

                // Ajouter la div de l'événement à la liste
                eventList.appendChild(eventDiv);
            });
        })
        .catch(error => {
            console.error("Erreur :", error);
        });

});