// Chargement du message d'erreur depuis l'URL
document.addEventListener("DOMContentLoaded", function () {
    console.log("SUCCESS - DOM fully loaded and parsed");

    localStorage.removeItem("panier");

    const url = "/userAccount.html";
    setTimeout(() => {
        window.location.href = url;
    }, 3000);
});
