class loginCtrl {
    constructor() {
        this.http = new servicesHttp(); // Instance du service HTTP
        this.callbackError = this.callbackError.bind(this);
        this.connectSuccess = this.connectSuccess.bind(this);
        this.disconnectSuccess = this.disconnectSuccess.bind(this);

        this.setupEventListeners();
    }

    // Initialise les écouteurs pour les boutons de connexion et déconnexion
    setupEventListeners() {
        const butConnect = $("#login-btn");
        const butDisconnect = $("#logout-btn");

        // Gestion du clic sur le bouton de connexion
        butConnect.click((event) => {
            event.preventDefault(); // Empêche la soumission du formulaire

            const email = $("#email").val();
            const password = $("#password").val();
            console.log("Sending email:", email, "and password:", password);

            // Appel à l'API de connexion
            this.http.connect(email, password, this.connectSuccess, this.callbackError);
        });

        // Gestion du clic sur le bouton de déconnexion
        butDisconnect.click((event) => {
            this.http.disconnect(this.disconnectSuccess, this.callbackError);
        });
    }

    // Callback appelé en cas de connexion réussie
    connectSuccess(data, text, jqXHR) {
        console.log("Login response:", data);

        const user = data.user;

        if (user && user.isAdmin === true) {
            // Stockage des infos de l’utilisateur dans le localStorage
            localStorage.setItem("clientEmail", user.email);
            localStorage.setItem("clientId", user.id);

            alert("Connexion réussie !");
            window.location.href = "viewCat.html"; // Redirection après connexion
        } else if (user && user.isAdmin === false) {
            alert("Seuls les administrateur peuvent accéder à cette page.");
        } else {
            alert("Échec de la connexion : informations invalides.");
        }
    }

    // Callback appelé en cas de déconnexion réussie
    disconnectSuccess(data, text, jqXHR) {
        alert("Utilisateur déconnecté");
        localStorage.clear(); // Suppression des données locales
        window.location.href = "login.html"; // Redirection vers la page de login
    }

    // Callback générique en cas d’erreur de requête (connexion ou déconnexion)
    callbackError(request, status, error) {
        console.log("Error login:", error);
        alert("Erreur de connexion : " + error);
    }
}

// Instanciation du contrôleur une fois le DOM chargé
$(document).ready(function () {
    window.ctrl = new loginCtrl();
});
