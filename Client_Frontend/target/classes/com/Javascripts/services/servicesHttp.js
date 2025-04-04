var BASE_URL = "https://alsawafm.emf-informatique.ch/module-151-M4ZENN/projet/server/";
//For localhost: var BASE_URL = "http://localhost:8080/projet/server/";

/**
 * Fetch notes from the server.
 * @param {function} successCallback - Called on success.
 * @param {function} errorCallback - Called on error.
 */
function chargerNotes(successCallback, errorCallback) {
  $.ajax({
    type: "GET",
    dataType: "xml",
    url: BASE_URL + "main.php",
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
function connect(email, password, successCallback, errorCallback) {
  $.ajax({
    type: "POST",
    dataType: "xml",
    url: BASE_URL + "workers/DBUserManager.php",
    data: {
      action: 'connect',
      email: email,
      password: password
    },
    success: successCallback,
    error: errorCallback
  });
}

/**
 * Disconnect the user.
 * @param {function} successCallback - Called on success.
 * @param {function} errorCallback - Called on error.
 */
function disconnect(successCallback, errorCallback) {
  $.ajax({
    type: "POST",
    dataType: "xml",
    url: BASE_URL + "workers/DBUserManager.php",
    data: 'action=disconnect',
    success: successCallback,
    error: errorCallback
  });
}

/**
 * Delete selected notes.
 * @param {Array} selectedNotes - List of note IDs to delete.
 * @param {function} successCallback - Called on success.
 * @param {function} errorCallback - Called on error.
 */
function deleteNotes(selectedNotes, successCallback, errorCallback) {
  $.ajax({
    type: "DELETE",
    dataType: "xml",
    url: BASE_URL + "main.php",
    data: { titels: selectedNotes },
    success: successCallback,
    error: errorCallback
  });
}

/**
 * Add a new note to the server.
 * @param {string} title - The note's title.
 * @param {string} message - The note's message.
 * @param {string} date - The note's date.
 * @param {string} time - The note's time.
 * @param {number} fk_category - The note's category ID.
 * @param {function} successCallback - Called on success.
 * @param {function} errorCallback - Called on error.
 */
function addNote(title, message, date, time, fk_category, successCallback, errorCallback) {
  $.ajax({
    type: "POST",
    dataType: "xml",
    url: BASE_URL + "main.php",
    data: {
      title: title,
      message: message,
      date: date,
      time: time,
      fk_category: fk_category
    },
    success: successCallback,
    error: errorCallback
  });
}

/**
 * Fetch categories from the server.
 * @param {function} successCallback - Called on success.
 * @param {function} errorCallback - Called on error.
 */
function getCategories(successCallback, errorCallback) {
  $.ajax({
    type: "GET",
    dataType: "xml",
    url: BASE_URL + "main.php?action=getCategories",
    success: successCallback,
    error: errorCallback
  });
}

/**
 * Fetch the details of a specific note.
 * @param {string} noteId - The ID of the note.
 * @param {function} successCallback - Called on success.
 * @param {function} errorCallback - Called on error.
 */
function getNoteDetails(noteId, successCallback, errorCallback) {
  $.ajax({
    type: "GET",
    dataType: "xml",
    url: BASE_URL + `main.php?action=getNote&pk_note=${encodeURIComponent(noteId)}`,
    success: successCallback,
    error: errorCallback
  });
}

/**
 * Modify an existing note.
 * @param {number} noteId - The ID of the note to modify.
 * @param {string} title - The updated title.
 * @param {string} message - The updated message.
 * @param {string} date - The updated date.
 * @param {string} time - The updated time.
 * @param {number} fk_category - The updated category.
 * @param {number} fk_admin - The ID of the admin modifying the note.
 * @param {function} successCallback - Called on success.
 * @param {function} errorCallback - Called on error.
 */
function modifyNote(noteId, title, message, date, time, fk_category, fk_admin, successCallback, errorCallback) {
  $.ajax({
    type: "PUT",
    dataType: "xml",
    url: BASE_URL + "main.php",
    data: `pk_note=${encodeURIComponent(noteId)}&title=${encodeURIComponent(title)}&message=${encodeURIComponent(message)}&date=${encodeURIComponent(date)}&time=${encodeURIComponent(time)}&fk_category=${encodeURIComponent(fk_category)}&fk_admin=${encodeURIComponent(fk_admin)}`,
    success: successCallback,
    error: errorCallback
  });
}

/**
 * Fetch all notes from the server.
 * @param {function} successCallback - Called on success.
 * @param {function} errorCallback - Called on error.
 */
function getNotes(successCallback, errorCallback) {
  $.ajax({
    type: "GET",
    dataType: "xml",
    url: BASE_URL + "main.php?action=getNotes",
    success: successCallback,
    error: errorCallback
  });
}

/**
 * Increment the like count for a specific note.
 * @param {string} pk_note - The ID of the note to like.
 * @param {function} successCallback - Called on success.
 * @param {function} errorCallback - Called on error.
 */
function incrementLike(pk_note, successCallback, errorCallback) {
  $.ajax({
    type: "POST",
    dataType: "xml",
    url: BASE_URL + "main.php",
    data: {
      action: 'incrementLike',
      pk_note: pk_note
    },
    success: successCallback,
    error: errorCallback
  });
}

/**
 * Check if the user is authenticated.
 * @param {function} successCallback - Called on success.
 * @param {function} errorCallback - Called on error.
 */
function isAuthenticated(successCallback, errorCallback) {
  $.ajax({
    type: "GET",
    url: BASE_URL + "workers/DBUserManager.php",
    data: { action: "isAuthenticated" },
    success: successCallback,
    error: errorCallback
  });
}