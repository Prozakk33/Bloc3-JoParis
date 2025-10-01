function getAccessToken() {
    // Vérifier si le token existe
    const jwtToken = localStorage.getItem("jwtToken");

    // Si le token n'existe pas, lancer une erreur
    if (!jwtToken) {
        throw new Error("Aucun token JWT trouvé dans le localStorage");
    }

    console.log(
        "getAccessToken - jwtToken récupéré depuis le localStorage :",
        jwtToken
    );

    // Extraire l'accessToken du jwtToken
    let accessToken;
    try {
        const parsedToken = JSON.parse(jwtToken); // Convertit la chaîne JSON en objet
        accessToken = parsedToken.accessToken; // Récupère le champ accessToken
    } catch (e) {
        throw new Error("Erreur lors du parsing du token JWT");
    }

    // Vérifier si l'accessToken est présent
    if (!accessToken) {
        throw new Error("Aucun accessToken trouvé dans le token JWT");
    }
    console.log(
        "getAccessToken - accessToken extrait du jwtToken :",
        accessToken
    );
    return accessToken;
}
