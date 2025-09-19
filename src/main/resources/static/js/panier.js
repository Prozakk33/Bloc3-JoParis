    function ajouterOptionAuPanier(event) {

        event.preventDefault();
        // Récupérer l'option sélectionnée
        const selectedOption = document.querySelector('input[name="ticketType"]:checked');

        // Déterminer les paramètres associés à l'option sélectionnée
        let quantite = 1;
        let prix = parseFloat(document.querySelector('span[for="' + selectedOption.id + '"]').textContent.replace('€', '').trim());

        if (selectedOption.value === "2") {
            quantite = 2; // Duo
        } else if (selectedOption.value === "4") {
            quantite = 4; // Famille
        }

        // Récupérer les informations de l'événement
        const eventId = document.querySelector('input[name="eventId"]').value;
        const eventTitle = document.getElementById("eventTitle").textContent;

        // Appeler la fonction ajouterAuPanier avec les données récupérées
        ajouterAuPanier({
            id: eventId,
            nom: eventTitle,
            type: selectedOption.value,
            date: document.getElementById("eventDate").textContent,
            prix: prix,
            quantite: quantite
        });

        // Mettre à jour le nombre d'articles dans le bouton Panier
        const panier = localStorage.getItem("panier");
        if (panier) {
            const panierItems = JSON.parse(panier);
            const totalItems = panierItems.reduce((total, item) => total + item.quantite, 0);
            document.getElementById("cart").textContent = `Panier (${totalItems})`;
        } else {
            document.getElementById("cart").textContent = "Panier";
        }

        // Afficher un message de confirmation
        afficherAlerte("L'article a été ajouté au panier !");
    }

    function afficherAlerte(message) {
        const retour = document.getElementById("back");
        retour.classList.add("hidden"); 
        const alertBox = document.getElementById("alertMessage");
        alertBox.textContent = message; // Définit le message de l'alerte
        alertBox.classList.remove("hidden"); // Affiche l'alerte
        // Masque l'alerte après 2 secondes (2000 ms)
        setTimeout(() => {
            alertBox.classList.add("hidden");
            retour.classList.remove("hidden");
        }, 2000);
    }

    // Fonction pour ajouter un article au panier (déjà définie dans votre gestionnaire de panier)
    function ajouterAuPanier(article) {
        const panier = JSON.parse(localStorage.getItem("panier")) || [];
        const index = panier.findIndex(item => item.id === article.id);

        if (index !== -1) {
            panier[index].quantite += article.quantite;
        } else {
            panier.push(article);
        }

        localStorage.setItem("panier", JSON.stringify(panier));
        console.log("Article ajouté au panier :", article);
    }


// Fonction pour récupérer le panier depuis le localStorage
function getPanier() {
    const panier = localStorage.getItem("panier");
    return panier ? JSON.parse(panier) : []; // Retourne un tableau vide si le panier n'existe pas
}

// Fonction pour sauvegarder le panier dans le localStorage
function savePanier(panier) {
    localStorage.setItem("panier", JSON.stringify(panier));
}

// Fonction pour ajouter un article au panier
function ajouterAuPanier(article) {
    const panier = getPanier();
    const index = panier.findIndex(item => item.id === article.id);

    if (index !== -1) {
        // Si l'article existe déjà, augmentez la quantité
        panier[index].quantite += article.quantite;
    } else {
        // Sinon, ajoutez l'article au panier
        panier.push(article);
    }

    savePanier(panier);
    console.log("Article ajouté au panier :", article);
}

// Fonction pour supprimer un article du panier
function supprimerDuPanier(id) {
    let panier = getPanier();
    panier = panier.filter(item => item.id !== id); // Supprime l'article avec l'ID donné
    savePanier(panier);
    console.log("Article supprimé du panier :", id);
}

// Fonction pour mettre à jour la quantité d'un article
function mettreAJourQuantite(id, quantite) {
    const panier = getPanier();
    const index = panier.findIndex(item => item.id === id);

    if (index !== -1) {
        panier[index].quantite = quantite;
        savePanier(panier);
        console.log("Quantité mise à jour pour l'article :", id, "Nouvelle quantité :", quantite);
    }
}

// Fonction pour calculer le total du panier
function calculerTotal() {
    const panier = getPanier();
    return panier.reduce((total, item) => total + item.prix * item.quantite, 0);
}

// Fonction pour afficher le panier dans le HTML
function afficherPanier() {
    const panier = getPanier();
    const panierListe = document.getElementById("panierList");
    const totalPanier = document.getElementById("totalPrice");

    panierListe.innerHTML = ""; // Vide la liste actuelle

    panier.forEach(item => {
        const li = document.createElement("li");
        li.textContent = `${item.nom} - ${item.prix} € x ${item.quantite}`;
        const supprimerBtn = document.createElement("button");
        supprimerBtn.textContent = "Supprimer";
        supprimerBtn.onclick = () => {
            supprimerDuPanier(item.id);
            afficherPanier(); // Met à jour l'affichage après suppression
        };
        li.appendChild(supprimerBtn);
        panierListe.appendChild(li);
    });

    totalPanier.textContent = calculerTotal().toFixed(2); // Affiche le total avec 2 décimales
}