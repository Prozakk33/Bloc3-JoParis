function ajouterOptionAuPanier(event) {
    if (event) {
        event.preventDefault();
    }
    // Récupérer l'option sélectionnée
    const selectedOption = document.querySelector('input[name="ticketType"]:checked');

    const spanElement = document.querySelector('span[for="' + selectedOption.id + '"]');
    if (!spanElement) {
        console.error(`Aucun élément <span> trouvé pour l'ID : ${selectedOption.id}`);
        return;
    }

    // Déterminer les paramètres associés à l'option sélectionnée
    let quantite = 1;
    let prix = parseFloat(
        document
            .querySelector('span[for="' + selectedOption.id + '"]')
            .textContent.replace("€", "")
            .trim()
    );

    if (selectedOption.value === "2") {
        quantite = 2; // Duo
    } else if (selectedOption.value === "4") {
        quantite = 4; // Famille
    }

    // Récupérer les informations de l'événement
    const eventId = document.getElementById("eventId").value;
    const eventTitle = document.getElementById("eventTitle").textContent;

    console.log("Ajout au panier :", {
        id: eventId,
        //nom: eventTitle,
        type: selectedOption.value,
        //date: document.getElementById("eventDate").textContent,
        //sport: document.getElementById("eventSport").textContent,
        //city: document.getElementById("eventCity").textContent,
        prix: prix,
        quantite: quantite,
    });

    // Appeler la fonction ajouterAuPanier avec les données récupérées
    ajouterAuPanier({
        id: eventId,
        //nom: eventTitle,
        type: selectedOption.value,
        //date: document.getElementById("eventDate").textContent,
        //sport: document.getElementById("eventSport").textContent,
        //city: document.getElementById("eventCity").textContent,
        prix: prix,
        quantite: quantite,
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
    const add = document.getElementById("addToCart");
    add.classList.add("hidden");
    const alertBox = document.getElementById("alertMessage");
    alertBox.textContent = message; // Définit le message de l'alerte
    alertBox.classList.remove("hidden"); // Affiche l'alerte
    // Masque l'alerte après 2 secondes (2000 ms)
    setTimeout(() => {
        alertBox.classList.add("hidden");
        add.classList.remove("hidden");
    }, 2000);
}

// Fonction pour ajouter un article au panier (déjà définie dans votre gestionnaire de panier)
function ajouterAuPanier(article) {
    console.log("Ajout de l'article au panier :", article);
    const panier = JSON.parse(localStorage.getItem("panier")) || [];
    const index = panier.findIndex((item) => item.id === article.id);

    // Si l'évènement est déjà dans le panier, on met à jour la quantité seulement si le type de billet est le même
    if (index !== -1) {
        console.log("Article déjà dans le panier, Check type de billet :", article.type);
        // Si le type de ticket est le même, on ajoute la quantité
        if (panier[index].type === article.type) {
            console.log("Même type de billet, mise à jour de la quantité");
            panier[index].quantite += article.quantite;
        } else {
            console.log("Type de billet différent, ajout d'une nouvelle entrée");
            panier.push(article); // Ajoute une nouvelle entrée pour un type de billet différent
        }
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

// Fonction pour supprimer un article du panier
function supprimerDuPanier(id, type) {
    let panier = getPanier();
    panier = panier.filter((item) => item.id !== id || item.type !== type); // Supprime l'article avec l'ID et le type donnés
    savePanier(panier);
    console.log("Article supprimé du panier :", id);
}

// Fonction pour mettre à jour la quantité d'un article
function mettreAJourQuantite(id, quantite, type) {
    const panier = getPanier();
    const index = panier.findIndex((item) => item.id === id);

    if (index !== -1) {
        if (item.type === type) {
            panier[index].quantite = quantite;
            savePanier(panier);
            console.log("Quantité mise à jour pour l'article :", id, "Nouvelle quantité :", quantite);
        }
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
    console.log("Affichage du panier :", panier);
    const panierListe = document.getElementById("tdBody");
    panierListe.innerHTML = ""; // Vider la liste avant de la remplir
    const totalPanier = document.getElementById("totalPrice");

    panier.forEach((item) => {
        // Créer une nouvelle ligne pour chaque article
        const tr = document.createElement("tr");

        console.log("Récupération des détails de l'événement pour l'ID :", item.id);
        fetch("/event/" + item.id, {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
            },
        })
            .then((response) => {
                console.log("Réponse brute de l'événement :", response);
                return response.json();
            })
            .then((data) => {
                // Colonne : Titre de l'événement
                const tdTitle = document.createElement("td");
                tdTitle.classList.add("px-4", "py-3");
                tdTitle.textContent = data.title; // Remplacez "N/A" par une valeur par défaut si nécessaire
                tr.appendChild(tdTitle);

                // Colonne : Sport (ajoutez cette propriété dans vos données si nécessaire)
                const tdSport = document.createElement("td");
                tdSport.classList.add("px-4", "py-3");
                tdSport.textContent = data.sport;
                tr.appendChild(tdSport);

                // Colonne : Date
                const tdDate = document.createElement("td");
                tdDate.classList.add("px-4", "py-3");
                tdDate.textContent = data.date;
                tr.appendChild(tdDate);

                // Colonne : Ville
                const tdCity = document.createElement("td");
                tdCity.classList.add("px-4", "py-3");
                tdCity.textContent = data.city; // Remplacez "N/A" par une valeur par défaut si nécessaire
                tr.appendChild(tdCity);

                //Colonne : Type Billet
                const tdType = document.createElement("td");
                tdType.classList.add("px-4", "py-3");
                tdType.textContent = item.type; // Remplacez "N/A" par une valeur par défaut si nécessaire
                tr.appendChild(tdType);

                // Colonne : Prix
                const tdPrice = document.createElement("td");
                tdPrice.classList.add("px-4", "py-3");
                tdPrice.textContent = item.prix.toFixed(2) + " €";
                tr.appendChild(tdPrice);

                // Colonne : Quantité
                const tdQuantite = document.createElement("td");
                tdQuantite.classList.add("px-4", "py-3");
                tdQuantite.textContent = item.quantite;
                tr.appendChild(tdQuantite);

                //Colonne : Total Ligne
                const tdTotalLine = document.createElement("td");
                tdTotalLine.classList.add("px-4", "py-3");
                const totalLine = item.prix * item.quantite;
                tdTotalLine.textContent = totalLine.toFixed(2) + " €";
                tr.appendChild(tdTotalLine);

                // Colonne : Bouton "Supprimer"
                const tdDelete = document.createElement("td");
                const supprimerBtn = document.createElement("button");
                supprimerBtn.textContent = "Supprimer";
                supprimerBtn.classList.add(
                    "text-white",
                    "bg-red-600",
                    "hover:bg-red-800",
                    "focus:outline-none",
                    "focus:ring-4",
                    "focus:ring-red-300",
                    "font-medium",
                    "rounded-full",
                    "text-sm",
                    "px-5",
                    "py-2.5",
                    "text-center",
                    "me-2",
                    "mb-2",
                    "dark:bg-blue-600",
                    "dark:hover:bg-blue-700",
                    "dark:focus:ring-blue-800"
                );
                supprimerBtn.onclick = () => {
                    supprimerDuPanier(item.id, item.type);
                    afficherPanier(); // Met à jour l'affichage après suppression
                };
                tdDelete.appendChild(supprimerBtn);
                tr.appendChild(tdDelete);

                // Ajouter la ligne à la table
                panierListe.appendChild(tr);
            })
            .catch((error) => {
                console.error("Erreur lors de la récupération de l'événement :", error);
            });
    });

    // Mettre à jour le total du panier
    totalPanier.textContent = "Total de la commande : " + calculerTotal().toFixed(2) + " €"; // Affiche le total avec 2 décimales
}

async function fetchUserAccountforPayment() {
    // Récupérer le token JWT depuis le localStorage
    const token = localStorage.getItem("jwtToken");
    const message = "Merci de vous connecter pour accéder au paiement";
    if (!token) {
        window.location.href = "/signin.html?errorMessage=" + message; // Redirige vers la page de connexion si aucun token n'est trouvé
        return;
    }
    // Vérifier si le token existe
    const jwtToken = localStorage.getItem("jwtToken");
    if (!jwtToken) {
        window.location.href = "/signin.html?errorMessage=" + message; // Redirige vers la page de connexion si aucun token n'est trouvé
        return;
    }

    console.log("jwtToken récupéré depuis le localStorage :", jwtToken);

    // Extraire l'accessToken du jwtToken
    let accessToken;
    try {
        const parsedToken = JSON.parse(jwtToken); // Convertit la chaîne JSON en objet
        accessToken = parsedToken.accessToken; // Récupère le champ accessToken
    } catch (e) {
        console.error("Erreur lors du parsing du jwtToken :", e);
        window.location.href = "/user/signin.html?errorMessage=" + message; // Redirige si le parsing échoue
        return;
    }

    // Vérifier si l'accessToken est présent
    if (!accessToken) {
        console.error("Aucun accessToken trouvé dans jwtToken. Redirection vers la page de connexion.");
        window.location.href = "/user/signin.html?errorMessage=" + message; // Redirige si accessToken est absent
        return;
    }

    console.log("AccessToken extrait :", accessToken);

    // Calculer le montant total à payer
    const totalAmount = calculerTotal();

    panier = getPanier();
    console.log("Tickets envoyés pour le paiement :", panier);

    const cartData = {
        panier: panier,
    };

    console.log("Données complètes envoyées pour le paiement :", cartData);
    setTimeout(() => {
        console.log("Délai écoulé, envoi de la requête de paiement.");
    }, 30000); // 30 secondes

    // Envoyer la requête de paiement au serveur
    try {
        const response = await fetch("/payment", {
            method: "POST",
            headers: {
                Authorization: `Bearer ${accessToken}`, // Ajoute le token dans l'en-tête Authorization
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                priceId: "price_1SCbFXDtl7ori9LmCx62oVOI",
                quantity: totalAmount,
                cart: cartData,
            }),
        });
        if (!response.ok) {
            //throw new Error("Erreur lors de la création de la session de paiement.");
            if (response.status === 401) {
                console.error("Token invalide ou expiré. Redirection vers la page de connexion.");
                const message = "Token invalide ou expiré. Veuillez vous reconnecter.";
                //Supprimer le token invalide
                localStorage.removeItem("jwtToken");
                window.location.href = "/signin.html?errorMessage=" + message;
            } else if (response.status === 400) {
                console.error("Requête invalide. Vérifiez les données envoyées.");
                alert("Erreur : Requête invalide. Veuillez vérifier les informations.");
            } else if (response.status === 500) {
                console.error("Erreur interne du serveur.");
                alert("Erreur : Une erreur interne s'est produite. Veuillez réessayer plus tard.");
            } else {
                console.error("Erreur inattendue :", response.status);
                alert("Erreur : Une erreur inattendue s'est produite.");
            }
        }
        const data = await response.json();
        // Ici, on traite les données JSON retournées par le serveur
        console.log("Données reçues :", data);
        // Rediriger l'utilisateur vers l'URL de paiement
        window.location.href = data.url;
    } catch (error) {
        console.error("Erreur lors de la requête de paiement :", error);
        alert("Erreur lors de la requête de paiement. Veuillez réessayer.");
    }
}
