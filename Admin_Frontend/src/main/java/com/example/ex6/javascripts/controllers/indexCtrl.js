class IndexCtrl {
    constructor() {
        this.http = new servicesHttp();
        this.connectSuccess = this.connectSuccess.bind(this);
        this.disconnectSuccess = this.disconnectSuccess.bind(this);
        this.callbackError = this.callbackError.bind(this);
        this.getCatsSuccess = this.getCatsSuccess.bind(this);
        
        this.init();
    }

    init() {
        const user = JSON.parse(localStorage.getItem("user"));
        if (user) {
            this.displayUser(user);
        }
    }

    connectSuccess(data) {
        try {
            // Parse response if it's a string
            const responseData = typeof data === 'string' ? JSON.parse(data) : data;
            
            if (responseData && responseData.id) {
                // Store user details including ID
                const user = {
                    id: responseData.id,
                    name: responseData.name || responseData.email || "Admin",
                    email: responseData.email,
                    isAdmin: responseData.isAdmin || true
                };
                
                this.user = user;
                localStorage.setItem("user", JSON.stringify(user));
                
                // Show success message
                Toastify({
                    text: "Connexion réussie",
                    duration: 3000,
                    gravity: "top",
                    position: "right",
                    backgroundColor: "#33cc33"
                }).showToast();
                
                // Redirect to cat view page
                window.location.href = "view.html";
            } else {
                throw new Error("Invalid response format");
            }
        } catch (error) {
            console.error("Error processing login response:", error);
            this.callbackError(null, "error", "Format de réponse invalide");
        }
    }

    disconnectSuccess() {
        // Clear user data
        this.user = null;
        localStorage.removeItem("user");
        
        // Show logout message
        Toastify({
            text: "Déconnexion réussie",
            duration: 3000,
            gravity: "top",
            position: "right",
            backgroundColor: "#33cc33"
        }).showToast();
        
        // Redirect to login page
        window.location.href = "login.html";
    }

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
    
        // Show success Toastify message
        Toastify({
            text: "Liste des chats chargée avec succès",
            duration: 2000,
            gravity: "top",
            position: "right",
            backgroundColor: "#33cc33"
        }).showToast();
    }
    
    callbackError(request, status, error) {
        Toastify({
            text: "Error: " + error,
            duration: 3000,
            gravity: "top",
            position: "right",
            backgroundColor: "#ff3333"
        }).showToast();
    }

    displayCats(cats) {
        const catsContainer = document.getElementById("cats-list");
        if (!catsContainer) return;
        
        if (!cats || cats.length === 0) {
            catsContainer.innerHTML = `
                <div class="no-cats">
                    <p>Aucun chat n'est disponible actuellement.</p>
                </div>
            `;
            return;
        }
        
        catsContainer.innerHTML = '';
        
        cats.forEach(cat => {
            const catCard = document.createElement('div');
            catCard.className = 'cat-card';
            catCard.innerHTML = `
                <div class="cat-header">${cat.name}</div>
                <img src="${cat.image || "/api/placeholder/400/300"}" alt="${cat.name}" class="cat-image">
                <div class="cat-info">
                    <div class="info-field">
                        <label>Âge:</label>
                        <p>${cat.age || cat.birthdate}</p>
                    </div>
                    <div class="info-field">
                        <label>Race:</label>
                        <p>${cat.breed}</p>
                    </div>
                    <div class="info-field">
                        <label>Anecdote:</label>
                        <p>${cat.funFact || "N/A"}</p>
                    </div>
                    <div class="info-field">
                        <label>Description:</label>
                        <p>${cat.description || "Aucune description disponible"}</p>
                    </div>
                </div>
                <div class="cat-actions">
                    <button class="btn btn-secondary modify-cat" data-id="${cat.id}">
                        <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                            <path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7"></path>
                            <path d="M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z"></path>
                        </svg>
                        Modifier
                    </button>
                    <button class="btn btn-danger delete-cat" data-id="${cat.id}">
                        <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                            <polyline points="3 6 5 6 21 6"></polyline>
                            <path d="M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2"></path>
                            <line x1="10" y1="11" x2="10" y2="17"></line>
                            <line x1="14" y1="11" x2="14" y2="17"></line>
                        </svg>
                        Supprimer
                    </button>
                </div>
            `;
            
            catsContainer.appendChild(catCard);
        });
        
        // Add event listeners to the modify and delete buttons
        document.querySelectorAll('.modify-cat').forEach(button => {
            button.addEventListener('click', (e) => {
                const catId = e.currentTarget.getAttribute('data-id');
                window.location.href = `edit-cat.html?id=${catId}`;
            });
        });
        
        document.querySelectorAll('.delete-cat').forEach(button => {
            button.addEventListener('click', (e) => {
                const catId = e.currentTarget.getAttribute('data-id');
                if (confirm('Êtes-vous sûr de vouloir supprimer ce chat ?')) {
                    this.http.deleteCat(catId, this.deleteCatSuccess, this.callbackError);
                }
            });
        });
    }

    addCatSuccess(data) {
        Toastify({
            text: "Chat ajouté avec succès",
            duration: 3000,
            gravity: "top",
            position: "right",
            backgroundColor: "#33cc33"
        }).showToast();
        
        // Redirect to cat view page
        setTimeout(() => {
            window.location.href = "view.html";
        }, 1500);
    }

    updateCatSuccess(data) {
        Toastify({
            text: "Chat modifié avec succès",
            duration: 3000,
            gravity: "top",
            position: "right",
            backgroundColor: "#33cc33"
        }).showToast();
        
        // Redirect to cat view page
        setTimeout(() => {
            window.location.href = "view.html";
        }, 1500);
    }

    deleteCatSuccess(data) {
        Toastify({
            text: "Chat supprimé avec succès",
            duration: 3000,
            gravity: "top",
            position: "right",
            backgroundColor: "#33cc33"
        }).showToast();
        
        // Reload cats list
        this.loadCats();
    }
}

// Initialize the application when the DOM is ready
$(document).ready(function() {
    // Create global controller instance
    window.adminCtrl = new AdminController();
    
    // Handle login form submission
    $("#login-form").on("submit", function(event) {
        event.preventDefault();
        const email = $("#email").val();
        const password = $("#password").val();
        
        console.log("Login attempt for:", email);
        window.adminCtrl.http.connect(email, password, window.adminCtrl.connectSuccess, window.adminCtrl.callbackError);
    });
    
    // Handle logout button click
    $("#logout-btn").on("click", function() {
        window.adminCtrl.http.disconnect(window.adminCtrl.disconnectSuccess, window.adminCtrl.callbackError);
    });
    
    // Handle add cat button click
    $("#add-cat-btn").on("click", function() {
        window.location.href = "add-cat.html";
    });
    
    // Handle cat form submission if on add/edit cat page
    $("#cat-form").on("submit", function(event) {
        event.preventDefault();
        
        // Get cat data from form
        const catData = {
            name: $("#catName").val(),
            breed: $("#catBreed").val(),
            birthdate: $("#catBirthdate").val(),
            funFact: $("#catFunFact").val(),
            description: $("#catDescription").val(),
            isPurchased: $("#catIsPurchased").is(":checked")
        };
        
        // Check if this is an edit (has cat ID) or add (no cat ID)
        const urlParams = new URLSearchParams(window.location.search);
        const catId = urlParams.get('id');
        
        if (catId) {
            // Update existing cat
            window.adminCtrl.http.updateCat(catId, catData, window.adminCtrl.updateCatSuccess, window.adminCtrl.callbackError);
        } else {
            // Add new cat
            window.adminCtrl.http.addCat(catData, window.adminCtrl.addCatSuccess, window.adminCtrl.callbackError);
        }
    });
});