class servicesHttp {
    constructor() {
        this.BASE_URL = "http://localhost:8080/";
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
            dataType: "json",
            url: this.BASE_URL + "gateway/modifyCat",
            data: {
                catId: catId,
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
                catId: catId
            },
            success: successCallback,
            error: errorCallback
        });
    }
}