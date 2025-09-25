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

function fetchUserAccount() {
    // Récupérer le token JWT depuis le localStorage
    const token = localStorage.getItem("jwtToken");

    // Vérifier si le token existe
    const jwtToken = localStorage.getItem("jwtToken");
    if (!jwtToken) {
        window.location.href = "/user/signin"; // Redirige vers la page de connexion si aucun token n'est trouvé
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
        window.location.href = "/user/signin"; // Redirige si le parsing échoue
        return;
    }

    // Vérifier si l'accessToken est présent
    if (!accessToken) {
        console.error("Aucun accessToken trouvé dans jwtToken. Redirection vers la page de connexion.");
        window.location.href = "/user/signin"; // Redirige si accessToken est absent
        return;
    } 
    // Effectuer une requête GET avec l'en-tête Authorization
    fetch("/auth/account", {
        method: "GET",
        headers: {
            "Authorization": `Bearer ${accessToken}`, // Ajoute le token dans l'en-tête Authorization
            "Content-Type": "application/json"
        }
    })
        .then(response => {
            if (response.ok) {
                console.log("Accès autorisé à /auth/account.");
                return response.text(); // Récupère la page HTML renvoyée par le serveur
            } else if (response.status === 401) {
                console.error("Accès refusé. Redirection vers la page de connexion.");
                window.location.href = "/signin"; // Redirige si le token est invalide
            } else {
                console.error("Erreur lors de la requête :", response.status);
            }
        })
        .then(html => {
            // Affiche la page HTML renvoyée par le serveur
            document.open();
            document.write(html);
            document.close();
        })
        .catch(error => {
            console.error("Erreur lors de la requête :", error);
        });
}
