document.getElementById("signUpForm").addEventListener("submit", async function(event) {
            
    event.preventDefault(); // Empêche le rechargement de la page

    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;
    const firstName = document.getElementById("firstName").value;
    const lastName = document.getElementById("lastName").value;

    console.log("Email saisi :", email);
    console.log("Password saisi :", password);
    console.log("First Name saisi :", firstName);
    console.log("Last Name saisi :", lastName);

    if (firstName == ""){
        document.getElementById("errorFirstName").innerText = "Le prénom doit être saisi.";
        return;
    } else {
        document.getElementById("errorFirstName").innerText = "";
    }

    if (lastName == ""){
        document.getElementById("errorLastName").innerText = "Le nom doit être saisi.";
        return;
    } else {
        document.getElementById("errorLastName").innerText = "";
    }

    if (email == ""){
        document.getElementById("errorEmail").innerText = "L'Email doit être saisi.";
        return;
    } else {
        document.getElementById("errorEmail").innerText = "";
    }

    if (password == ""){
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
                "Content-Type": "application/json"
            },
            body: JSON.stringify({ firstName, lastName, email, password })
        });

        if (response.ok) {
            console.log("Inscription réussie !");
            const data = await response.text();
            console.log("Données reçues :", data);
            window.location.href = `/signin.html?message=${data}`; // Redirige vers la page de connexion
        } else {
            document.getElementById("errorMessage").innerText = await response.text();
        }
    } catch (error) {
        //console.error("Erreur :", error);
        document.getElementById("errorMessage").innerText = error.message;
    }

        });