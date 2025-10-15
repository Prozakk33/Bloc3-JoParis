// Chargement du message d'erreur depuis l'URL
document.addEventListener("DOMContentLoaded", function () {
    console.log("SIGNUP - DOM fully loaded and parsed");
    const urlParams = new URLSearchParams(window.location.search);
    const signupMessage = urlParams.get("signup"); // Récupère la variable "signup"
    console.log("Message signup :", signupMessage);

    if (signupMessage) {
        document.getElementById("signupMessage").innerText = signupMessage;
    }

    const errorMessage = urlParams.get("errorMessage"); // Récupère la variable "error"
    console.log("Message error :", errorMessage);

    if (errorMessage) {
        document.getElementById("errorMessage").innerText = errorMessage;
    }
});

// Validation et soumission du formulaire de connexion
document.getElementById("loginForm").addEventListener("submit", async function (event) {
    event.preventDefault(); // Empêche le rechargement de la page

    document.getElementById("signupMessage").innerText = "";

    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;

    if (email == "") {
        document.getElementById("errorEmail").innerText = "L'Email doit être saisi.";
        return;
    } else {
        document.getElementById("errorEmail").innerText = "";
    }

    if (password == "") {
        document.getElementById("errorPassword").innerText = "Le mot de passe doit être saisi.";
        return;
    } else {
        document.getElementById("errorPassword").innerText = "";
    }

    try {
        const response = await fetch("/auth/signin", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                email: sanitizeInput(email),
                password: sanitizeInput(password),
            }),
        });

        if (response.ok) {
            const token = await response.text(); // Récupère le token JWT
            console.log("Token JWT :", token);
            localStorage.setItem("jwtToken", token); // Enregistre le token dans le localStorage

            /* Redirection vers la page de paiement si l'utilisateur vient de là */
            const referrer = document.referrer;
            console.log("Page source :", referrer);
            // Vérifier si une page source est disponible
            if (referrer) {
                window.location.href = referrer;
            } else {
                window.location.href = "/"; // Redirige vers la page d'accueil
            }
        } else {
            document.getElementById("errorMessage").innerText = "Email ou mot de passe invalide.";
        }
    } catch (error) {
        console.error("Erreur :", error);
        document.getElementById("errorMessage").innerText = "Une erreur s'est produite. Veuillez réessayer.";
    }
});
// Fonction pour échapper les caractères spéciaux afin de prévenir les attaques XSS
// Fonction pour échapper les caractères spéciaux dans une chaîne (prévention XSS)
function sanitizeInput(input) {
    const temp = document.createElement("div");
    temp.innerText = input;
    temp.remove();
    return temp.innerHTML;
}
