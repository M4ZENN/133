class servicesHttp {
    constructor() {
        this.BASE_URL = "http://host.docker.internal:8083/";
    }
    
    chargerCats(successCallback, errorCallback) {
        $.ajax({
            type: "GET",
            dataType: "json",
            url: this.BASE_URL + "gateway/getCats",
            success: successCallback,
            error: errorCallback
        });
    }
  
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
                 "&buyerId=0", // Ajoutez une valeur par défaut pour buyerId
            data: {},
            success: successCallback,
            error: errorCallback
        });
    }

    deleteCat(catId, successCallback, errorCallback) {
        $.ajax({
            type: "DELETE",
            dataType: "json",
            url: this.BASE_URL + "gateway/deleteCat?id=" + catId,
            success: successCallback,
            error: errorCallback
        });
    }
    
    getBreeds(successCallback, errorCallback) {
        $.ajax({
            type: "GET",
            dataType: "json",
            url: this.BASE_URL + "gateway/getBreeds",
            success: successCallback,
            error: errorCallback
        });
    }

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

    
    /**
     * Authenticate a user with email and password.
     * @param {string} email - User's email address.
     * @param {string} password - User's password.
     * @param {function} successCallback - Called on success.
     * @param {function} errorCallback - Called on error.
     */
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
        /*   xhrFields: {
            withCredentials: true // Include credentials in the request
        },*/
        success: successCallback,
        error: errorCallback
        });
    }
    
    /**
     * Disconnect the user.
     * @param {function} successCallback - Called on success.
     * @param {function} errorCallback - Called on error.
     */
    disconnect(successCallback, errorCallback) {
        $.ajax({
        type: "POST",
        dataType: "json",
        url:this.BASE_URL + "gateway/logout",
        data: 'action=disconnect',
        success: successCallback,
        error: errorCallback
        });
    }
}