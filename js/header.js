const searchWrapper = document.querySelector(".search-container");
const searchIcon = document.getElementById("search-icon");
const closeIcon = document.getElementById("close-icon");
const searchInput = document.getElementById('search-input');
const loginContainer = document.getElementById('login-icon');

searchIcon.addEventListener("click", () => {
    searchWrapper.classList.add("active");
    closeIcon.style.display = "inline";
});

closeIcon.addEventListener("click", () => {
    searchWrapper.classList.remove("active");
    closeIcon.style.display = "none"; 
    limparCampo();
});

function limparCampo() {
    searchInput.value = '';
}

function handleEnterKeyPress(event) {
    if (event.key === 'Enter') {
        limparCampo();
    }
}

searchInput.addEventListener('keyup', handleEnterKeyPress);

document.addEventListener('click', (event) => {
    if (!searchWrapper.contains(event.target)) {
        searchWrapper.classList.remove("active");
        closeIcon.style.display = "none"; 
        limparCampo();
    }
});

setTimeout(limparCampo, 2000);

document.getElementById("login-icon").addEventListener("click", function() {
    window.location.href = "login.html";
});
