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
  
    addCat(catId, buyerId, successCallback, errorCallback) {
      $.ajax({
          type: "PUT",
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
  }
  