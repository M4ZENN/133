class LoginCtrl {
  constructor() {
    this.service = new servicesHttp();

    this.connectSuccess = this.connectSuccess.bind(this);
    this.disconnectSuccess = this.disconnectSuccess.bind(this);
    this.callbackError = this.callbackError.bind(this);

    this.init();
  }

  // Initialise les actions des boutons de login et logout
  init() {
    const butConnect = $("#login-btn");
    const butDisconnect = $("#logout-btn");

    butConnect.click((event) => {
      event.preventDefault();
      const email = $("#email").val();
      const password = $("#password").val();
      console.log("Sending email:", email, "and password:", password);
      this.service.connect(email, password, this.connectSuccess, this.callbackError);
    });

    butDisconnect.click(() => {
      this.service.disconnect(this.disconnectSuccess, this.callbackError);
    });
  }

  // Gère la connexion réussie du client et redirige vers la page des chats
  connectSuccess(data) {
    console.log("Login response:", data);
    const user = data.user;

    if (user && user.isAdmin === false) {
      localStorage.setItem("clientEmail", user.email);
      localStorage.setItem("clientId", user.id);
      alert("Connexion réussie !");
      window.location.href = "viewCat.html";
    } else if (user && user.isAdmin === true) {
      alert("Seuls les clients peuvent accéder à cette page.");
    } else {
      alert("Échec de la connexion : informations invalides.");
    }
  }

  // Déconnecte l’utilisateur et redirige vers la page de login
  disconnectSuccess() {
    alert("User disconnected");
    localStorage.clear();
    window.location.href = "login.html";
  }

  // Affiche une erreur en cas d’échec de connexion
  callbackError(request, status, error) {
    console.log("Error login:", error); 
    alert("Error login: " + error);
  }
}

$(document).ready(function () {
  window.loginCtrl = new LoginCtrl();
});
