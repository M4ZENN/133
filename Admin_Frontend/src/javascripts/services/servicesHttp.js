class servicesHttp {
    constructor() {
        this.BASE_URL = "http://localhost:8083/";
    }

    // Récupère la liste des chats depuis l'API
    chargerCats(successCallback, errorCallback) {
        $.ajax({
            type: "GET",
            dataType: "json",
            url: this.BASE_URL + "gateway/getCats",
            success: successCallback,
            error: errorCallback
        });
    }

    // Ajoute un nouveau chat dans la base de données
    addCat(name, birthdate, breedId, funFact, description, successCallback, errorCallback) {
        $.ajax({
            type: "POST",
            dataType: "json",
            url: this.BASE_URL + "gateway/addCat",
            data: {
                name: name,
                birthdate: birthdate,
                breedId: breedId,
                funFact: funFact,
                description: description
            },
            success: successCallback,
            error: errorCallback
        });
    }

    // Modifie les informations d’un chat existant
    modifyCat(catId, name, birthdate, breedId, funFact, description, successCallback, errorCallback) {
        $.ajax({
            type: "PUT",
            dataType: "text",
            url: this.BASE_URL + "gateway/updateCatInformation" + 
                 "?id=" + encodeURIComponent(catId) +
                 "&name=" + encodeURIComponent(name) + 
                 "&birthdate=" + encodeURIComponent(birthdate) + 
                 "&breedId=" + encodeURIComponent(breedId) + 
                 "&funFact=" + encodeURIComponent(funFact) + 
                 "&description=" + encodeURIComponent(description) +
                 "&buyerId=0", // Ajoute une valeur par défaut pour buyerId
            data: {},
            success: successCallback,
            error: errorCallback
        });
    }

    // Supprime un chat de la base de données
    deleteCat(catId, successCallback, errorCallback) {
        $.ajax({
            type: "DELETE",
            dataType: "json",
            url: this.BASE_URL + "gateway/deleteCat?id=" + catId,
            success: successCallback,
            error: errorCallback
        });
    }

    // Récupère la liste des races de chats
    getBreeds(successCallback, errorCallback) {
        $.ajax({
            type: "GET",
            dataType: "json",
            url: this.BASE_URL + "gateway/getBreeds",
            success: successCallback,
            error: errorCallback
        });
    }

    // Récupère les détails d’un chat via son ID
    getCat(catId, successCallback, errorCallback) {
        $.ajax({
            type: "GET",
            dataType: "json",
            url: this.BASE_URL + "gateway/getCat",
            data: {
                id: catId
            },
            success: successCallback,
            error: errorCallback
        });
    }

    // Envoie une requête de connexion avec email et mot de passe
    connect(email, password, successCallback, errorCallback) {
        $.ajax({
            type: "POST",
            dataType: "json",
            url: this.BASE_URL + "gateway/login",
            data: {
                action: 'connect',
                email: email,
                password: password
            },
            success: successCallback,
            error: errorCallback
        });
    }

    // Envoie une requête de déconnexion de l'utilisateur
    disconnect(successCallback, errorCallback) {
        $.ajax({
            type: "POST",
            dataType: "json",
            url: this.BASE_URL + "gateway/logout",
            data: 'action=disconnect',
            success: successCallback,
            error: errorCallback
        });
    }
}
