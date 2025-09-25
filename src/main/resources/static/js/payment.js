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
                console.log("Accès autorisé à /payment");
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