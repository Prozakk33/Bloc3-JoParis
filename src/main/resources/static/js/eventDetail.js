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
    
    // Récupérer l'ID de l'événement depuis l'URL
    const urlParams = new URLSearchParams(window.location.search);
    const eventId = urlParams.get("id");

    if (!eventId) {
        console.error("Aucun ID d'événement trouvé dans l'URL.");
        return;
    }

    // Appeler l'API pour récupérer les détails de l'événement
    fetch(`/event/${eventId}`)
        .then(response => {
            if (!response.ok) {
                throw new Error("Erreur lors de la récupération des détails de l'événement.");
            }
            return response.json(); // Convertir la réponse en JSON
        })
        .then(event => {
            // Remplir les informations de l'événement dans la page
            document.getElementById("eventId").value = event.id;
            document.getElementById("eventSport").textContent = event.sport;
            document.getElementById("eventTitle").textContent = event.title;
            document.getElementById("eventCity").textContent = event.city;
            document.getElementById("eventDate").textContent = formatDate(event.date);
            document.getElementById("eventDescription").textContent = event.description;
            document.querySelector('[data-sport]').setAttribute("data-sport", event.sport);
            document.querySelector('[data-sport] img').setAttribute("src", sportImages[event.sport.toLowerCase()] || "https://dummyimage.com/400x400");

            // Calculer et afficher les prix des tickets
            const soloPrice = event.price.toFixed(2);
            const duoPrice = (event.price * 2 * 0.9).toFixed(2); // Réduction de 10%
            const famillePrice = (event.price * 4 * 0.8).toFixed(2); // Réduction de 20%

            document.getElementById("soloPrice").textContent = `${soloPrice} €`;
            document.getElementById("duoPrice").textContent = `${duoPrice} €`;
            document.getElementById("familyPrice").textContent = `${famillePrice} €`;

            // Ajouter l'ID de l'événement dans le champ caché
            document.getElementById("eventId").value = event.id;
        })
        .catch(error => {
            console.error("Erreur :", error);
        });
});

// Fonction pour formater la date
function formatDate(dateString) {
    const date = new Date(dateString);
    return new Intl.DateTimeFormat("fr-FR", {
        day: "2-digit",
        month: "long",
        year: "numeric",
        hour: "2-digit",
        minute: "2-digit"
    }).format(date);
}