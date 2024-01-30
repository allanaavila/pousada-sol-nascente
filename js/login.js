//fazendo transicao de uma tela pra outra

const btnSignin = document.querySelector("#signin");
const btnSigup = document.querySelector("#signup");

const body = document.querySelector("body");

btnSignin.addEventListener("click", function() {
    body.className = "sing-in-js";
});

btnSigup.addEventListener("click", function() {
    body.className = "sing-up-js";
});

