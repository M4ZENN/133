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

  buyCat(catId,successCallback, errorCallback) {
    $.ajax({
        type: "POST",
        dataType: "json",
        url: this.BASE_URL + "gateway/updatePurchase/"+catId,
        success: successCallback,
        error: errorCallback
    });
}
}
