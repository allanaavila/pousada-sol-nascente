const searchWrapper = document.querySelector(".search-container");
const searchIcon = document.getElementById("search-icon");
const closeIcon = document.getElementById("close-icon");
const searchInput = document.getElementById('search-input');

searchIcon.addEventListener("click", () => {
    searchWrapper.classList.add("active");
    closeIcon.style.display = "inline"; // Mostra o ícone de fechar
});

closeIcon.addEventListener("click", () => {
    searchWrapper.classList.remove("active");
    closeIcon.style.display = "none"; // Esconde o ícone de fechar
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

// Opcional: Adiciona um ouvinte de evento para clicar fora da área de pesquisa
document.addEventListener('click', (event) => {
    if (!searchWrapper.contains(event.target)) {
        searchWrapper.classList.remove("active");
        closeIcon.style.display = "none"; // Esconde o ícone de fechar
        limparCampo();
    }
});

// O código abaixo é opcional e apenas para limpar o campo após 2 segundos
setTimeout(limparCampo, 2000);
