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

  buyCat(catId, buyerId, successCallback, errorCallback) {
    $.ajax({
        type: "PUT",
        dataType: "json",
        url: this.BASE_URL + "gateway/updatePurchase",
        data: {
            id: catId,
            buyerId: buyerId
        },
        success: successCallback,
        error: errorCallback
    });
}
}
