class servicesHttp {
  constructor() {
    this.BASE_URL = "http://localhost:8083/"; // URL de base pour les requêtes
  }

  // Charge la liste des chats depuis l'API.
  chargerCats(successCallback, errorCallback) {
    $.ajax({
      type: "GET",
      dataType: "json",
      url: this.BASE_URL + "gateway/getCats",
      success: successCallback,
      error: errorCallback,
    });
  }

  // Met à jour l'achat d'un chat via l'API.
  buyCat(catId, buyerId, successCallback, errorCallback) {
    $.ajax({
      type: "PUT",
      dataType: "json",
      url: this.BASE_URL + "gateway/updatePurchase",
      data: {
        id: catId,
        buyerId: buyerId,
      },
      success: successCallback,
      error: errorCallback,
    });
  }

  // Connecte un utilisateur avec son email et mot de passe (sans commentaire sur chaque ligne).
  connect(email, password, successCallback, errorCallback) {
    $.ajax({
      type: "POST",
      dataType: "json",
      url: this.BASE_URL + "gateway/login",
      data: {
        action: "connect",
        email: email,
        password: password,
      },
      xhrFields: {
        withCredentials: true,
      },
      success: successCallback,
      error: errorCallback,
    });
  }

  // Déconnecte un utilisateur et nettoie la session.
  disconnect(successCallback, errorCallback) {
    $.ajax({
      type: "POST",
      dataType: "json",
      url: this.BASE_URL + "gateway/logout",
      data: "action=disconnect",
      success: (data) => {
        successCallback(data);
        localStorage.clear(); // Clear client credentials
        window.location.href = "login.html"; // Redirect to login
      },
      error: errorCallback,
    });
  }
}
