document.addEventListener("DOMContentLoaded", () => {
    
    
    const sportDivs = document.querySelectorAll('[data-sport]')

    // Tableau associant les sports aux images
    //FOOTBALL, RUGBY, BASKETBALL, JUDO, ATHLETISME, CHEVAL, ESCRIME, GOLF, HANDBALL, NATATION, PLONGEON, SKATEBOARD, TRIATHLON, VOILE
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

    // Vérifier si une image existe dans le DOM
    const imageElement = document.querySelector("img");
    if (!imageElement) {
        console.log("Aucune image trouvée dans le DOM.");
        return;
    }

    // Parcourir chaque div et mettre à jour l'image correspondante
    sportDivs.forEach(div => {
        const sport = div.getAttribute("data-sport").toLowerCase(); // Récupère la valeur de l'attribut data-sport
        console.log("Sport trouvé :", sport);

        // Vérifier si une image est associée à ce sport
        if (sportImages[sport]) {
            // Trouver l'image à l'intérieur de cette div
            const imageElement = div.querySelector("img");
            if (imageElement) {
                // Mettre à jour l'image avec l'URL correspondante
                imageElement.src = sportImages[sport];
                console.log(`Image mise à jour pour le sport "${sport}" : ${sportImages[sport]}`);
            } else {
                console.log(`Aucune image trouvée dans la div pour le sport "${sport}".`);
            }
        } else {
            console.log(`Aucune image associée au sport "${sport}".`);
        }
    });
});