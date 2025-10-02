async function verifyTicket(ticketKey) {
    try {
        console.log("Vérification du ticket avec la clé :", ticketKey);
        try {
            const accessToken = getAccessToken();
            const response = await fetch(`/tickets/verify`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    Authorization: "Bearer " + accessToken,
                },
                body: JSON.stringify({
                    ticketKey:
                        "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJUaWNrZXQiLCJmaXJzdE5hbWUiOiJQaGlsaXBwZSIsImxhc3ROYW1lIjoiQ0hBQkVSVCIsImJ1eURhdGUiOjE3NTkyNDA0Mjc5MTAsImV2ZW50SWQiOjExLCJ0aWNrZXRUeXBlIjoiU29sbyIsImlhdCI6MTc1OTI0MDQyN30.X6Augb5amfKMwnQiT_jO9XWDWGevOKsc-Sz2Z65fsdM",
                }),
            });
            if (!response.ok) {
                throw new Error("Erreur lors de la vérification du ticket");
            }
            const data = await response.json();
            console.log("Résultat de la vérification du ticket :", data);
        } catch (error) {
            console.error("Erreur lors de la vérification du ticket :", error);
        }
    } catch (error) {
        console.error("Erreur lors de la récupération du token :", error);
    }
}
