class IndexCtrl {
    constructor() {
        this.http = new servicesHttp();
        this.callbackError = this.callbackError.bind(this);
        this.getCatsSuccess = this.getCatsSuccess.bind(this);

        this.init();
    }

    init() {
        // Fetch and display the cats when the page loads
        this.http.chargerCats(this.getCatsSuccess, this.callbackError);
    }

    /**
     * Success callback for loading the list of cats from the server.
     * @param {Object} data - The raw data returned from the server.
     */
    getCatsSuccess(data) {
        console.log("getCatsSuccess called", data);
    
        const catsListContainer = $("#cats-list");
        const templateCatCard = $(".cat-card.template-card");
    
        data.forEach(function(cat) {
            const catCard = templateCatCard.clone()[0]; // Clone template card
            catCard.style.display = "block";
    
            // Calculate age from birthdate
            const birthDate = new Date(cat.birthdate);
            const today = new Date();
            let age = today.getFullYear() - birthDate.getFullYear();
            const monthDiff = today.getMonth() - birthDate.getMonth();
            if (monthDiff < 0 || (monthDiff === 0 && today.getDate() < birthDate.getDate())) {
                age--;
            }
    
            // Fill in cat data
            $(catCard).find(".cat-header").text(cat.name);
            $(catCard).find(".cat-info .info-field p").eq(0).text(`${age} ans`);
            $(catCard).find(".cat-info .info-field p").eq(1).text(cat.breed.name.trim());
            $(catCard).find(".cat-info .info-field p").eq(2).text(cat.funFact);
            $(catCard).find(".cat-info .info-field p").eq(3).text(cat.description.trim());
    
            // Set image or fallback
            const image = cat.image || "https://via.placeholder.com/200x150?text=No+Image";
            $(catCard).find("img").attr("src", image).attr("alt", cat.name);
    
          
            // Append to list
            catsListContainer.append(catCard);
        });
    
        console.log("Cats loaded and displayed successfully");
    }
    

    callbackError(request, status, error) {
        // Handle errors when loading cats
        console.error("Error loading cats: " + error);
        alert("Error loading cats: " + error);  // You can replace this with any other method of error handling
    }
}

$(document).ready(function () {
    window.ctrl = new IndexCtrl();
});
