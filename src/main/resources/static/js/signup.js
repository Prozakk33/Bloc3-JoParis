document.getElementById("signUpForm").addEventListener("submit", async function (event) {
    event.preventDefault(); // Empêche le rechargement de la page

    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;
    const firstName = document.getElementById("firstName").value;
    const lastName = document.getElementById("lastName").value;

    console.log("Email saisi :", email);
    console.log("Password saisi :", password);
    console.log("First Name saisi :", firstName);
    console.log("Last Name saisi :", lastName);

    if (firstName == "") {
        document.getElementById("errorFirstName").innerText = "Le prénom doit être saisi.";
        return;
    } else {
        document.getElementById("errorFirstName").innerText = "";
    }

    if (lastName == "") {
        document.getElementById("errorLastName").innerText = "Le nom doit être saisi.";
        return;
    } else {
        document.getElementById("errorLastName").innerText = "";
    }

    if (email == "") {
        document.getElementById("errorEmail").innerText = "L'Email doit être saisi.";
        return;
    } else {
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/; // Vérification du format de l'email
        if (!emailRegex.test(email)) {
            document.getElementById("errorEmail").innerText = "L'Email n'est pas valide.";
            return;
        } else {
            document.getElementById("errorEmail").innerText = "";
        }
    }

    if (password == "") {
        document.getElementById("errorPassword").innerText = "Le mot de passe doit être saisi.";
        return;
    } else {
        const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/;
        if (!passwordRegex.test(password)) {
            document.getElementById("errorPassword").innerText =
                "Le mot de passe doit contenir au moins 8 caractères, une majuscule, une minuscule, un chiffre et un caractère spécial.";
            return;
        } else {
            document.getElementById("errorPassword").innerText = "";
        }
    }

    try {
        const response = await fetch("/auth/signup", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                firstName: sanitizeInput(firstName),
                lastName: sanitizeInput(lastName),
                email: sanitizeInput(email),
                password: sanitizeInput(password),
            }),
        });

        if (response.ok) {
            console.log("Inscription réussie !");
            const data = await response.text();
            console.log("Données reçues :", data);
            window.location.href = `/signin.html?signup=${data}`; // Redirige vers la page de connexion
        } else {
            document.getElementById("errorMessage").innerText = await response.text();
        }
    } catch (error) {
        //console.error("Erreur :", error);
        document.getElementById("errorMessage").innerText = error.message;
    }
});

// Fonction pour échapper les caractères spéciaux dans une chaîne (prévention XSS)
function sanitizeInput(input) {
    const temp = document.createElement("div");
    temp.innerText = input;
    temp.remove();
    return temp.innerHTML;
}
