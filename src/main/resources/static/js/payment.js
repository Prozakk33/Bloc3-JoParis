document.addEventListener("DOMContentLoaded", function() {
    console.log("PAYMENT - DOM fully loaded and parsed");
    checkUserAccount();
});

function checkUserAccount() {
    // Récupérer le token JWT depuis le localStorage
    const token = localStorage.getItem("jwtToken");

    // Vérifier si le token existe
    const jwtToken = localStorage.getItem("jwtToken");
    if (!jwtToken) {
        window.location.href = "/signin.html?errorMessage=Connectez-vous pour accéder au paiement"; // Redirige vers la page de connexion si aucun token n'est trouvé
        return;
    } 

    console.log("CheckUserAccount - jwtToken récupéré depuis le localStorage :", jwtToken);

    // Extraire l'accessToken du jwtToken
    let accessToken;
    try {
        const parsedToken = JSON.parse(jwtToken); // Convertit la chaîne JSON en objet
        accessToken = parsedToken.accessToken; // Récupère le champ accessToken
    } catch (e) {
        console.error("CheckUserAccount - Erreur lors du parsing du jwtToken :", e);
        window.location.href = "/signin.html?errorMessage=Erreur lors de la lecture du Token - Reconnectez-vous"; // Redirige si le parsing échoue
        return;
    }

    // Vérifier si l'accessToken est présent
    if (!accessToken) {
        console.error("CheckUserAccount - Aucun accessToken trouvé dans jwtToken. Redirection vers la page de connexion.");
        window.location.href = "/signin.html?errorMessage=Aucun accessToken trouvé - Reconnectez-vous"; // Redirige si accessToken est absent
        return;
    } 
    // Effectuer une requête POST avec l'en-tête Authorization
    console.log("CheckUserAccount - Envoi de la requête POST à /auth/checkAccount avec l'accessToken :", accessToken);
    fetch("/auth/checkAccount", {
        method: "POST",
        headers: {
            "Authorization": `Bearer ${accessToken}`, // Ajoute le token dans l'en-tête Authorization
            "Content-Type": "application/json"
        }
    })
        .then(response => {
            if (response.ok) {
                console.log("Accès autorisé au paiement");
            } else if (response.status === 401) {
                console.error("Accès refusé. Redirection vers la page de connexion.");
                window.location.href = "/signin.html?errorMessage=Token invalide - Reconnectez-vous"; // Redirige si le token est invalide
            } else {
                console.error("Erreur lors de la requête :", response.status);
            }
        })
        .catch(error => {
            console.error("Erreur lors de la requête :", error);
        });
}