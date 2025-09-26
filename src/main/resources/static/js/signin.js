document.getElementById("loginForm").addEventListener("submit", async function(event) {
            event.preventDefault(); // Empêche le rechargement de la page

            const email = document.getElementById("email").value;
            const password = document.getElementById("password").value;

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
                document.getElementById("errorPassword").innerText = "";
            }

            try {
                const response = await fetch("/auth/signin", {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json"
                    },
                    body: JSON.stringify({ email, password })
                });

                if (response.ok) {
                    const token = await response.text(); // Récupère le token JWT
                    console.log("Token JWT :", token);
                    localStorage.setItem("jwtToken", token); // Enregistre le token dans le localStorage
                    window.location.href = "/"; // Redirige vers la page d'accueil
                } else {
                    document.getElementById("errorEmail").innerText = "Email ou mot de passe invalide.";
                }
            } catch (error) {
                console.error("Erreur :", error);
                document.getElementById("errorPassword").innerText = "Une erreur s'est produite. Veuillez réessayer.";
            }
        });