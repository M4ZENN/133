class IndexCtrl {
    constructor() {
        this.http = new servicesHttp();
        this.callbackError = this.callbackError.bind(this);
        this.getCatsSuccess = this.getCatsSuccess.bind(this);
        this.deleteCatSuccess = this.deleteCatSuccess.bind(this);
        
        this.init();
        this.setupEventListeners();
    }

    init() {
        // Fetch and display the cats when the page loads
        this.http.chargerCats(this.getCatsSuccess, this.callbackError);
    }

    setupEventListeners() {
        // Add cat button event listener
        $("#add-cat-btn").on("click", () => {
            window.location.href = "addCat.html";
        });

        // Logout button event listener
        $("#logout-btn").on("click", () => {
            // Could implement actual logout request here
            // this.http.logout(this.logoutSuccess, this.callbackError);
            window.location.href = "login.html"; // Redirect to login page
        });

        // Dynamic event listeners will be added for modify and delete buttons when the cats are loaded
    }

    /**
     * Success callback for loading the list of cats from the server.
     * @param {Object} data - The raw data returned from the server.
     */
    getCatsSuccess(data) {
        console.log("getCatsSuccess called", data);
    
        const catsListContainer = $("#cats-list");
        const templateCatCard = $(".cat-card.template-card");
        
        // Clear existing cats (except the template)
        catsListContainer.find(".cat-card:not(.template-card)").remove();
    
        data.forEach((cat) => {
            const catCard = templateCatCard.clone();
            catCard.removeClass("template-card").css("display", "block");
    
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
            const image = cat.image || "https://scalebranding.com/_next/image?url=https%3A%2F%2Fimages.scalebranding.com%2Fcat-and-fish-logo-bad17591-0047-4c46-a313-9781029fb316.jpg&w=256&q=75";
            $(catCard).find("img").attr("src", image).attr("alt", cat.name);
    
            // Set the cat ID to the data-id attributes of the buttons
            $(catCard).find(".modify-cat").attr("data-id", cat.id);
            $(catCard).find(".delete-cat").attr("data-id", cat.id);
            
            // Add click handlers for the modify and delete buttons
            $(catCard).find(".modify-cat").on("click", (e) => {
                const catId = $(e.currentTarget).attr("data-id");
                window.location.href = `modify.html?id=${catId}`;
            });
            
            $(catCard).find(".delete-cat").on("click", (e) => {
                const catId = $(e.currentTarget).attr("data-id");
                if (confirm("Êtes-vous sûr de vouloir supprimer ce chat ?")) {
                    this.http.deleteCat(catId, this.deleteCatSuccess, this.callbackError);
                }
            });
            
            // Append to list
            catsListContainer.append(catCard);
        });
    
        console.log("Cats loaded and displayed successfully");
    }
    
    deleteCatSuccess(data) {
        console.log("Cat deleted successfully", data);
        // If your response now has a message property, you might want to show it
        if (data && data.message) {
            alert(data.message);
        }
        // Refresh the cats list
        this.http.chargerCats(this.getCatsSuccess, this.callbackError);
    }

    callbackError(request, status, error) {
        // Handle errors when loading cats
        console.error("Error: " + error);
        alert("Error: " + error);
    }
}

$(document).ready(function () {
    window.ctrl = new IndexCtrl();
});