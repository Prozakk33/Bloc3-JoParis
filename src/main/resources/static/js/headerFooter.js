document.addEventListener("DOMContentLoaded", () => {
    // Charger le header
    fetch("../templates/header.html")
        .then(response => response.text())
        .then(data => {
            document.body.insertAdjacentHTML("afterbegin", data);
            updateHeaderButtons(); // Met à jour les boutons du header après le chargement
        });

    // Charger le footer
    fetch("../templates/footer.html")
        .then(response => response.text())
        .then(data => {
            document.body.insertAdjacentHTML("beforeend", data);
        });

    // Initialiser le MutationObserver après le chargement du header
            const adminButton = document.getElementById("adminButton");
            if (adminButton) {
                const observer = new MutationObserver(mutations => {
                    mutations.forEach(mutation => {
                        console.log("Modification détectée sur adminButton :", mutation);
                    });
                });
                observer.observe(adminButton, { attributes: true });
            } else {
                console.error("adminButton n'existe pas dans le DOM.");
            }
    
});

// Fonction pour décoder un token JWT
    function parseJwt(token) {
        try {
            const base64Url = token.split('.')[1];
            const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
            const jsonPayload = decodeURIComponent(
                atob(base64)
                    .split('')
                    .map(function (c) {
                        return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
                    })
                    .join('')
            );
            console.log("Token décodé :", jsonPayload);
            return JSON.parse(jsonPayload);
        } catch (e) {
            console.error("Erreur lors du décodage du token :", e);
            return null;
        }
    }

    // Vérifier si l'utilisateur est connecté et a le rôle USER
    function updateHeaderButtons() {

        console.log("Appel de updateHeaderButtons()");

        const token = localStorage.getItem("jwtToken"); // Récupère le token JWT depuis le localStorage

        if (token) {
            const decodedToken = parseJwt(token); // Décode le token JWT
            const roles = decodedToken?.role?.map(r => r.authority) || []; // Récupère les rôles

            console.log("Rôles de l'utilisateur :", roles);
            
            // Afficher les boutons pour les utilisateurs connectés
            document.getElementById("logoutButton").classList.remove("hidden");

            // Afficher le bouton "Mon Compte" uniquement si l'utilisateur a le rôle USER
            if (roles.includes("ROLE_USER")) {
                console.log("L'utilisateur a le rôle USER");
                document.getElementById("accountButton").classList.remove("hidden");
            }

            // Afficher le bouton "Admin" uniquement si l'utilisateur a le rôle ADMIN
            if (roles.includes("ROLE_ADMIN")) {
                console.log("L'utilisateur a le rôle ADMIN");
                document.getElementById("adminButton").classList.remove("hidden");
            }

            // Masquer le bouton "Connexion"
            document.getElementById("loginButton").classList.add("hidden");
        } else {
            // Si l'utilisateur n'est pas connecté, afficher uniquement le bouton "Connexion"
            console.log("Aucun token trouvé, utilisateur non connecté");
            document.getElementById("loginButton").classList.remove("hidden");
            document.getElementById("logoutButton").classList.add("hidden");
            document.getElementById("adminButton").classList.add("hidden");
        }
        
        const panier = localStorage.getItem("panier");
        if (panier) {
            const panierItems = JSON.parse(panier);
            const totalItems = panierItems.reduce((total, item) => total + item.quantite, 0);
            document.getElementById("cart").textContent = `Panier (${totalItems})`;
        } else {
            document.getElementById("cart").textContent = "Panier";
        }
    }
// Fonction de déconnexion
function logout() {
            console.log("Déconnexion");
            localStorage.removeItem("jwtToken");
            updateHeaderButtons();
        }